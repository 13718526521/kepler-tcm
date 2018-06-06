package com.kepler.tcm.server.tcm.server;

import com.kepler.tcm.server.core.util.Resource;
import com.kepler.tcm.server.tcm.agent.RemoteAgent;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.Naming;

public class Shutdown {
	private static String host = "";
	private static String agentPort = "";
	private static String serverPort = "";
	private static RemoteServer remoteServer;
	private static String className = "(@com.trs.tcm.server.Shutdown)";

	public static void main(String[] args) throws Exception {
		if ((args == null) || (args.length == 0)) {
			System.out.println("Need argument: server name");
			return;
		}
		Config.setBase("..", 0, "");

		stopMonitor(args[0]);
		stopServer(args[0]);
	}

	private static void stopMonitor(String server) throws Exception {
		Resource resource = Config.getServerConfig(server, true);
		if (resource.getStr("autoRestart", "").equals("1")) {
			try {
				resource = Config.getAgentConfig(true);
				host = resource.getStr("host", Server.host);
				agentPort = resource.getStr("agentPort", agentPort);
				System.out.println("Try to stop monitor. " + className);

				RemoteAgent agent = (RemoteAgent) Naming.lookup("rmi://" + host + ":" + agentPort + "/tcm_agent");

				agent.stopMonitor(server);
			} catch (Exception localException) {
			}
		}
	}

	public static void stopServer(String server) throws Exception {
		boolean success = false;

		host = Config.getAgentConfig(true).getStr("host", Server.host);

		Resource resource = Config.getServerConfig(server, true);

		serverPort = resource.getStr("serverPort", "-1");

		boolean maybeLive = true;
		try {
			if (serverPort.equals("-1"))
				throw new Exception("Can not find server port from @" + server);
			System.out.println("begin to stop server." + className);

			System.out.println("lookup rmi://" + host + ":" + serverPort + "/tcm_server");
			remoteServer = (RemoteServer) Naming.lookup("rmi://" + host + ":" + serverPort + "/tcm_server");
			System.out.println(System.getProperty("line.separator") + "TCM server shutdown!(rmi://" + host + ":"
					+ serverPort + ")");
			try {
				remoteServer.exit();
				success = true;
			} catch (Exception localException1) {
			}

		} catch (Exception e) {
			maybeLive = false;
			System.out.println(e);
		}

		if (!success) {
			try {
				if (maybeLive)
					System.out.println("Try to stop server from  monitor port" + className);
				int nMonitorPort = resource.getInt("monitorPort", -1);
				if (nMonitorPort == -1) {
					throw new Exception("Can not find monitor port from " + server + "'s tcm.conf");
				}

				Socket socket = new Socket(host, nMonitorPort);
				PrintWriter os = new PrintWriter(socket.getOutputStream(), true);
				os.println("#StopMe!");
				os.flush();
				os.close();
				socket.close();
			} catch (Exception e) {
				if (maybeLive)
					System.out.println(e);
			}

		}

		System.out.println("stopping server ends" + className);
	}
}