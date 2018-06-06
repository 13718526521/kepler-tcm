package com.kepler.tcm.server.tcm.server;

import com.kepler.tcm.server.core.util.FileTools;
import com.kepler.tcm.server.core.util.Resource;
import com.kepler.tcm.server.tcm.agent.RemoteAgent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.kepler.tcm.server.tcm.util.LoggingOutputStream;

public class Server {
	public static String host = "127.0.0.1";

	public static int nServerPort = 1099;

	public static int nAgentPort = 1098;

	public static int nMonitorPort = 6688;

	public static Log4jManager log4jManager = null;

	public static PluginManager pluginManager = null;

	public static DatabaseManager databaseManager = null;

	public static RemoteTaskManager taskManager = null;
	public static final String PLUGIN_PATH = "plugins";
	public static final String PLUGIN_PROPERTY_NAME = "plugin.properties";
	public static final String LOG_SEPARATOR = "|";
	public static final String TASKID_NAME = "./conf/TaskID";
	public static final String DATABASE_CONFIG_NAME = "./conf/database.xml";
	public static final int TASKID_LENGTH = 8;
	public static final String CONF_PATH = "./conf";
	public static final String COUNTER_PATH = "./conf/counters";
	public static final String PROFILE_PATH = "./conf/profiles";
	public static final String WORK_PATH = "./working";
	public static final String TASK_PROPERTY_NAME = "task.properties";
	public static final String CONFIG_PROPERTY_NAME = "config.properties";
	public static final String TASK_LOG_NAME1 = "out.log";
	public static final String TASK_LOG_NAME2 = "error.log";
	public static final String SERVER_LOG_ROOT = "root.log";
	public static final String SERVER_LOG_SERVER = "server.log";
	public static final String SERVER_LOG_SYSTEM_OUT = "system_out.log";
	public static final String SERVER_LOG_SYSTEM_ERR = "system_err.log";
	public static final String TASK_LOG_CONVENSION = "%d|%p|%m%n";
	public static final String ROOT_LOG_CONVENSION = "%d %p %t - %m%n";
	public static final String OTHER_LOG_CONVENSION = "%d %p %t %l - %m%n";
	public static String SERVER_PROCESS = "./process";
	public static final String LOG4J_CONFIG_NAME = "./conf/log4j.xml";
	public static final String PREFIX_LOGGER = "logger_";
	public static final String PREFIX_APPENDER = "appender_";
	public static final String PREFIX_APPENDER1 = "appender1_";
	public static final String PREFIX_APPENDER2 = "appender2_";
	public static final String LOGGER_SERVER = "server";
	public static final String LOGGER_AGENT = "agent";
	public static final String CHARSET = "UTF-8";
	public static final File LOG4J_CONFIG_FILE = new File("./conf/log4j.xml");
	public static final int UNSTARTED = 0;
	public static final int STARTED_REASON = 1;
	public static final int UNKNOWN_REASON = 2;
	public static Logger logger = null;

	private static String className = "(@Server)";
	public static String SERVER = "";

	public static String logPath = "";

	public static void main(String[] args) {
		try {
			System.out.println("Run Path=" + new File(".").getCanonicalPath());

			SERVER = FileTools.extractFileName(new File("./").getCanonicalPath());
			System.out.println("Server=" + SERVER);
			System.setProperty("server.name", SERVER);

			Config.setBase("../..", 2, SERVER);

			Resource r = Config.getAgentConfig(true);
			host = r.getStr("host", host);

			logPath = r.getStr("logPath", "");
			if (logPath.length() == 0)
				logPath = "../../logs";
			System.setProperty("log.path", logPath);

			Resource resource = Config.getServerConfig(SERVER, true);

			nServerPort = Integer.parseInt(resource.getStr("serverPort", "-1"));
			if (nServerPort <= 0)
				throw new Exception("应用服务器端口没有定义，服务将终止");

			nMonitorPort = Integer.parseInt(resource.getStr("monitorPort", "-1"));
			if (nMonitorPort <= 0)
				throw new Exception("应用服务器监控端口没有定义，服务将终止");

			if (System.getProperty("logDefault") != null) {
				LOG4J_CONFIG_FILE.deleteOnExit();
			}

			log4jManager = new Log4jManager();
			log4jManager.addRootLogger("${log.path}/${server.name}/root.log");
			log4jManager.addLogger("server", "${log.path}/${server.name}/server.log", "%d %p %t %l - %m%n", true);

			log4jManager.addLogger("system_out", "${log.path}/${server.name}/system_out.log", "%d %t - %m%n", false);
			log4jManager.addLogger("system_err", "${log.path}/${server.name}/system_err.log", "%d %t - %m%n", false);
			log4jManager.load();

			logger = Logger.getLogger("logger_server");

			if (!new File(logPath + "/" + SERVER).exists()) {
				new File(logPath + "/" + SERVER).mkdirs();
			}
			if (!new File("./conf").getParentFile().exists()) {
				new File("./conf").getParentFile().mkdirs();
			}
			if (!new File("./working").getParentFile().exists()) {
				new File("./working").getParentFile().mkdirs();
			}

			new File("plugins").mkdirs();

			LockState lockState = new Server().new LockState();

			new Server().new LockThread().start();

			while (!lockState.isFinshed) {
				synchronized (Thread.currentThread()) {
					Thread.currentThread().wait(100L);
				}
			}

			int nState = lockState.getState();

			if (nState == 1) {
				logger.error("TCM Server was fail to start beacuse only unique instance can be started!");
				System.out.println(System.getProperty("line.separator")
						+ "TCM Server was fail to start because only unique instance can be started!");
				System.exit(1);
			} else if (nState == 2) {
				logger.error("TCM Server was fail to start  with unknown reason!");
				System.out.println(
						System.getProperty("line.separator") + "TCM Server was fail to start  with unknown reason!");
				System.exit(2);
			}

			pluginManager = new PluginManager();
			pluginManager.load();
			databaseManager = new DatabaseManager();
			databaseManager.load();
			taskManager = new RemoteTaskManager();
			taskManager.load();

			new ServerListener(resource.getInt("monitorPort", nMonitorPort), true).start();

			Registry registry = LocateRegistry.createRegistry(nServerPort);
			RemoteServerImpl server = new RemoteServerImpl();

			Naming.rebind("rmi://" + host + ":" + nServerPort + "/tcm_server", server);

			com.kepler.tcm.server.startup.Bootstrap.bStarted = true;
			try {
				Socket socket = null;
				int n = 0;
				while (n++ < 5)
					try {
						socket = new Socket(host, nMonitorPort);
						socket.close();
					} catch (Exception e) {
						try {
							Thread.sleep(1000L);
						} catch (Exception localException1) {
						}
					}
				RemoteAgent agent = null;
				try {
					agent = (RemoteAgent) Naming.lookup("rmi://" + host + ":" + nAgentPort + "/tcm_agent");
				} catch (Exception localException2) {
				}
				if (agent != null) {
					System.out.println("Inform TCM Agent of monitoring the Server." + className);

					if (resource.getStr("autoRestart", "").equals("1"))
						agent.restartMonitor(SERVER);
					else {
						agent.stopMonitor(SERVER);
					}

				}

			} catch (Exception e) {
				System.out.println(e + className);
			}

			System.out.println("TCM server 2.6 started!(rmi://" + host + ":" + nServerPort + ")");
			logger.info("TCM server 2.6 started!(rmi://" + host + ":" + nServerPort + ")");

			new ServerThread().start();

			boolean defaultStreamEnable = System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0;
			if (defaultStreamEnable)
				System.out.println("System output write to " + new File(new StringBuffer(String.valueOf(logPath))
						.append("/").append(SERVER).append("/system_out.log").toString()).getCanonicalPath());
			else {
				System.out.println("System output redirect to " + new File(new StringBuffer(String.valueOf(logPath))
						.append("/").append(SERVER).append("/system_out.log").toString()).getCanonicalPath());
			}
			System.setOut(new PrintStream(
					new LoggingOutputStream(Logger.getLogger("logger_system_out"), Level.INFO, defaultStreamEnable),
					true));
			System.setErr(new PrintStream(
					new LoggingOutputStream(Logger.getLogger("logger_system_err"), Level.ERROR, defaultStreamEnable),
					true));
		} catch (Exception e) {
			if (logger != null) {
				logger.error(e);
			}
			e.printStackTrace();
		}
	}

	class LockState {
		private int state = 0;

		private boolean isFinshed = false;

		public boolean isFinshed() {
			return this.isFinshed;
		}

		public void setFinshed(boolean isFinshed) {
			this.isFinshed = isFinshed;
		}

		public int getState() {
			return this.state;
		}

		public void setState(int state) {
			this.state = state;
		}

	}

	class LockThread extends Thread {
		public LockThread() {
			/* this.lockState = Server.this; */
		}

		public void run() {
			FileOutputStream fileOutputStream = null;

			FileLock fileLock = null;
			try {
				File flagFile = new File(Server.SERVER_PROCESS);

				if (!flagFile.exists()) {
					flagFile.createNewFile();
				}

				fileOutputStream = new FileOutputStream(flagFile);

				fileLock = fileOutputStream.getChannel().tryLock();

				if ((fileLock != null) && (fileLock.isValid())) {
					fileOutputStream.write(Server.host.getBytes());
					fileOutputStream.write(System.getProperty("line.separator").getBytes());
					/* fileOutputStream.write(Server.nServerPort.getBytes()); */
					fileOutputStream.flush();
					/* Server.this.setState(0); */
					/* Server.this.setFinshed(true); */
					synchronized (this) {
						wait();
					}
				}
				/* Server.this.setState(1); */
			} catch (Exception ex) {
				/* Server.this.setState(2); */
				if (Server.logger != null) {
					Server.logger.error(ex);
				}
				ex.printStackTrace();
			}
			/* Server.this.setFinshed(true); */
		}
	}
}