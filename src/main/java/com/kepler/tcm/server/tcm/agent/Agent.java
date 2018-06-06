package com.kepler.tcm.server.tcm.agent;

import com.kepler.tcm.server.core.util.Resource;
import com.kepler.tcm.server.tcm.server.Config;
import com.kepler.tcm.server.tcm.server.Server;
import com.kepler.tcm.server.tcm.server.ServerListener;
import java.io.File;
import java.io.PrintStream;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import com.kepler.tcm.server.tcm.util.LoggingOutputStream;

public class Agent {
	static final Logger logger = Logger.getLogger(Agent.class);
	public static final String TCM_BASE = "..";
	public static String agentPort = "1098";
	public static String host = "";
	public static String logPath = "";
	public static String charset = "UTF-8";

	static RemoteAgent remoteAgent = null;

	public static void main(String[] args) throws Exception {
		Config.setBase("..", 1, "");

		Resource resource = Config.getAgentConfig(true);

		host = resource.getStr("host", Server.host);
		agentPort = resource.getStr("agentPort", agentPort);

		logPath = resource.getStr("logPath", "");
		if (logPath.length() == 0)
			logPath = "../logs";
		System.setProperty("log.path", logPath);
		DOMConfigurator.configure("../conf/log4j.xml");

		charset = resource.getStr("charset", charset);

		Registry registry = LocateRegistry.createRegistry(Integer.parseInt(agentPort));

		remoteAgent = new RemoteAgentImpl();

		String rmi = "rmi://" + host + ":" + agentPort + "/tcm_agent";

		Naming.rebind(rmi, remoteAgent);
		System.out.println("TCM Agent 2.6 started at " + rmi);
		logger.info("TCM Agent 2.6 started at " + rmi);

		new ServerListener(resource.getInt("monitorPort", 1097), true).start();
		System.out.println(new Date().toLocaleString() + " TCM Agent listening on " + agentPort);
		logger.info("TCM Agent listening on " + agentPort);

		System.out.println("System logs write to "
				+ new File(new StringBuffer(String.valueOf(logPath)).append("/agent.log").toString())
						.getCanonicalPath());
		boolean defaultStreamEnable = System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0;

		System.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO, defaultStreamEnable), true));
		System.setErr(new PrintStream(new LoggingOutputStream(logger, Level.ERROR, defaultStreamEnable), true));
	}
}