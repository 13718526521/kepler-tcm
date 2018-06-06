package com.kepler.tcm.server.tcm.agent;

import com.kepler.tcm.server.core.util.Resource;
import com.kepler.tcm.server.tcm.server.Config;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.UnmarshalException;
import org.apache.log4j.Logger;

public class Shutdown {
	static final Logger logger = Logger.getLogger(Shutdown.class);
	private static String host = "127.0.0.1";
	private static int agentPort = 1098;
	private static RemoteAgent agent;

	public static void main(String[] args) throws Exception {
		Config.setBase("..", 0, "");
		Resource resource = Config.getAgentConfig(true);

		host = resource.getStr("host", host);
		agentPort = resource.getInt("agentPort", agentPort);

		boolean success = false;
		boolean maybeLive = true;
		try {
			logger.info("lookup rmi://" + host + ":" + agentPort + "/tcm_agent");
			agent = (RemoteAgent) Naming.lookup("rmi://" + host + ":" + agentPort + "/tcm_agent");
			logger.info(System.getProperty("line.separator") + "Stop TCM Agent!(rmi://" + host + ":" + agentPort + ")");
			try {
				agent.exit();
			} catch (UnmarshalException e) {
				if ((e.getCause() != null) && ((e.getCause() instanceof SocketException)))
					maybeLive = false;

			} catch (Exception localException1) {
			}

		} catch (Exception e) {
			maybeLive = false;
			logger.error(e);
		}

		try {
			Socket socket = new Socket(host, agentPort);

			socket.close();
		} catch (Exception e) {
			success = true;
		}

		if (!success) {
			try {
				if (maybeLive)
					logger.info("Try to stop agent from  monitor port");
				int nMonitorPort = resource.getInt("monitorPort", -1);
				if (nMonitorPort == -1) {
					throw new Exception("Can not find monitor port from  agent.conf");
				}

				Socket socket = new Socket(host, nMonitorPort);
				PrintWriter os = new PrintWriter(socket.getOutputStream(), true);
				os.println("#StopMe!");
				os.flush();
				os.close();
				socket.close();
			} catch (Exception e) {
				if (maybeLive)
					logger.error(e);
			}
		}
		logger.info("Stopping Agent ends.");
	}
}