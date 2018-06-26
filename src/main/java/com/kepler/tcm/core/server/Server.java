package com.kepler.tcm.core.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.channels.FileLock;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kepler.tcm.core.agent.RemoteAgent;
import com.kepler.tcm.core.log.Log4jManager;
import com.kepler.tcm.core.plugin.PluginManager;
import com.kepler.tcm.core.startup.Bootstrap;
import com.kepler.tcm.core.task.RemoteTaskManager;
import com.kepler.tcm.core.util.Config;
import com.kepler.tcm.core.util.FileTools;
import com.kepler.tcm.core.util.LoggingOutputStream;
import com.kepler.tcm.core.util.Resource;

public class Server {
	
	
	private final static Logger log = LoggerFactory.getLogger(Server.class);
	
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
			
			log.info("Run Path=" + new File(".").getCanonicalPath());

			SERVER = FileTools.extractFileName(new File("./").getCanonicalPath());
			
			log.info("Server Name = " + SERVER);
			
			System.setProperty("server.name", SERVER);

			Config.setBasePath("../..", 2, SERVER);
			Resource r = Config.getAgentConfig(true);
			
			
			host = r.getStr("host", host);

			logPath = r.getStr("logPath", "");
			
			if (logPath.length() == 0)
				logPath = "../../logs";
			
			System.setProperty("log.path", logPath);

			//读取服务配置
			Resource resource = Config.getServerConfig(SERVER, true);
			
			nServerPort = Integer.parseInt(resource.getStr("serverPort", "-1"));
			
			if (nServerPort <= 0)
				throw new Exception("应用服务器端口没有定义，服务将终止");

			nMonitorPort = Integer.parseInt(resource.getStr("monitorPort", "-1"));

			
			if (nMonitorPort <= 0)
				throw new Exception("应用服务器监控端口没有定义，服务将终止");

			/*if (System.getProperty("logDefault") != null) {
				LOG4J_CONFIG_FILE.deleteOnExit();
			}*/

			/*log4jManager = new Log4jManager();
			log4jManager.addRootLogger("${log.path}/${server.name}/root.log");
			log4jManager.addLogger("server", "${log.path}/${server.name}/server.log", "%d %p %t %l - %m%n", true);

			log4jManager.addLogger("system_out", "${log.path}/${server.name}/system_out.log", "%d %t - %m%n", false);
			log4jManager.addLogger("system_err", "${log.path}/${server.name}/system_err.log", "%d %t - %m%n", false);
			log4jManager.load();

			logger = LoggerFactory.getLogger("logger_server");*/

			/*if (!new File(logPath + "/" + SERVER).exists()) {
				new File(logPath + "/" + SERVER).mkdirs();
			}*/
			if (!new File("./conf").getParentFile().exists()) {
				new File("./conf").getParentFile().mkdirs();
			}
			if (!new File("./working").getParentFile().exists()) {
				new File("./working").getParentFile().mkdirs();
			}

			new File("plugins").mkdirs();

			//实例化状态锁
			Server.LockState lockState = new Server().new LockState();

			//
			new Server().new LockThread(lockState).start();

			
			//文件未被释放，主线程等待
			while (!lockState.isFinshed) {
				synchronized (Thread.currentThread()) {
					
					Thread.currentThread().wait(100L);
				}
			}

			int nState = lockState.getState();


			if (nState == 1) {
				//log.error("TCM Server was fail to start beacuse only unique instance can be started!");
				System.out.println(System.getProperty("line.separator")
						+ "TCM Server was fail to start because only unique instance can be started!");
				System.exit(1);
			} else if (nState == 2) {
				//log.error("TCM Server was fail to start  with unknown reason!");
				System.out.println(
						System.getProperty("line.separator") + "TCM Server was fail to start  with unknown reason!");
				System.exit(2);
			}
			
			//加载插件
			pluginManager = new PluginManager();
			pluginManager.load();
			//加载数据库
			databaseManager = new DatabaseManager();
			databaseManager.load();
			
			//加载任务
			taskManager = new RemoteTaskManager();
			taskManager.load();

			//开启服务监听
			new ServerListener(resource.getInt("monitorPort", nMonitorPort), true).start();
			
			//注册服务
			LocateRegistry.createRegistry(nServerPort);
			RemoteServerImpl server = new RemoteServerImpl();
			String rmi = "rmi://" + host + ":" + nServerPort + "/tcm_server" ;
			Naming.rebind(rmi , server);

			Bootstrap.bStarted = true;
			
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
					//获取应用服务器代理服务实例
					agent = (RemoteAgent) Naming.lookup("rmi://" + host + ":" + nAgentPort + "/tcm_agent");
				} catch (Exception localException2) {
				}
				if (agent != null) {
					log.info("Inform TCM Agent of monitoring the Server." + className);

					if (resource.getStr("autoRestart", "").equals("1"))
						agent.restartMonitor(SERVER);
					else {
						agent.stopMonitor(SERVER);
					}

				}

			} catch (Exception e) {
				log.info(e + className);
			}

			log.info("TCM server 2.6 started!(rmi://" + host + ":" + nServerPort + ")");

			//
			new ServerThread().start();

			boolean defaultStreamEnable = System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0;
			
			if (defaultStreamEnable)
				log.info("System output write to " + new File(new StringBuffer(String.valueOf(logPath))
						.append("/").append(SERVER).append("/system_out.log").toString()).getCanonicalPath());
			else {
				log.info("System output redirect to " + new File(new StringBuffer(String.valueOf(logPath))
						.append("/").append(SERVER).append("/system_out.log").toString()).getCanonicalPath());
			}
			/*System.setOut(new PrintStream(
					new LoggingOutputStream(LoggerFactory.getLogger("logger_system_out"), Level.INFO, defaultStreamEnable),
					true));
			System.setErr(new PrintStream(
					new LoggingOutputStream(LoggerFactory.getLogger("logger_system_err"), Level.ERROR, defaultStreamEnable),
					true));*/
		} catch (Exception e) {
			log.error("异常信息："+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 状态锁
	 * @author wangsp
	 * @date 2018年6月25日
	 * @version V1.0
	 */
	class LockState {
		 public LockState(){
			 
		 }
		
		  private int state = 0;

		  private boolean isFinshed = false;

		  public boolean isFinshed()
		  {
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
	/**
	 * 线程锁
	 * @author wangsp
	 * @date 2018年6月25日
	 * @version V1.0
	 */
	class LockThread extends Thread {
		
	   private Server.LockState lockState = null;

	   public LockThread(Server.LockState LockState) {
		  this.lockState = LockState;
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
					
					fileOutputStream.write((Server.nServerPort+"").getBytes());
					
					fileOutputStream.flush();
					
					this.lockState.setState(0); 
					
					this.lockState.setFinshed(true); 
					
					synchronized (this) {
						wait();
					}
				}
				
				this.lockState.setState(1);
			} catch (Exception ex) {
				
				this.lockState.setState(2);
				if (Server.log != null) {
					Server.log.error(""+ex);
				}
				ex.printStackTrace();
			}
			
			this.lockState.setFinshed(true);
		}
	}
}