package com.kepler.tcm.server.tcm.server;

import com.kepler.tcm.server.core.util.StringList;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LocalTask implements TaskListener {
	private Logger taskLogger = null;
	protected TaskListener taskListener = null;
	private Properties properties = null;

	private Connection conn1 = null;

	private Connection conn2 = null;
	String[] connectionString1 = null;
	String[] connectionString2 = null;

	private HashMap map = new HashMap();

	private String className = "";

	private String classPath = "";

	private boolean error = false;

	private Map configMap = null;

	private String charset = "UTF-8";

	private String ERROR = "LocalTask不能在正式环境下面使用，请把LocalTask改回Task";

	TaskListener taskInstance = null;
	public static final int LEVEL_DEBUG = 0;
	public static final int LEVEL_INFO = 1;
	public static final int LEVEL_WARN = 2;
	public static final int LEVEL_ERROR = 3;
	public static final int LEVEL_FATAL = 4;
	public static final String[] LEVELS = { "DEBUG", "INFO", "WARN", "ERROR", "FATAL" };
	public static final int[] LOG4j_LEVELS = { 10000, 20000, 30000, 40000, 50000 };
	public static final int LOG_TYPE_CONSOLE = 0;
	public static final int LOG_TYPE_LOG4j = 1;
	public static final int LOG_TYPE_BOTH = 2;
	private int level = 0;
	private String configFile = "config";

	private static int logType = 0;

	private String taskID = null;

	int nExecuteNum = 0;

	StringList list = new StringList();

	public static void setLogType(int logType) {
		logType = logType;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getCharset() {
		return this.charset;
	}

	public void doLog(Object s, int level) {
		if (level < this.level)
			return;

		String name = LEVELS[level];
		if ((logType == 0) || (logType == 2))
			System.out.println(new Date().toLocaleString() + "[" + name + "]" + s);
		if ((logType == 1) || (logType == 2)) {
			this.taskLogger.log(Level.toLevel(LOG4j_LEVELS[level]), s);
		}
	}

	public void log(String strMessage) {
		doLog(strMessage, 1);
	}

	public void debug(String strMessage) {
		doLog(strMessage, 0);
	}

	public void info(String strMessage) {
		doLog(strMessage, 1);
	}

	public void error(String strMessage) {
		doLog(strMessage, 3);
	}

	public void fatal(String strMessage) {
		doLog(strMessage, 4);
	}

	public void warn(String strMessage) {
		doLog(strMessage, 2);
	}

	public void log(Object s) {
		doLog(s, 1);
	}

	public void debug(Object strMessage) {
		doLog(strMessage, 0);
	}

	public void info(Object strMessage) {
		doLog(strMessage, 1);
	}

	public void error(Object strMessage) {
		doLog(strMessage, 3);
	}

	public void error(Object strMessage, Throwable t) {
		doLog(strMessage + "\r\n" + Util.eToString(t), 3);
	}

	public void warn(Object strMessage) {
		doLog(strMessage, 2);
	}

	public void fatal(Object strMessage) {
		doLog(strMessage, 4);
	}

	public void fatal(Object strMessage, Throwable t) {
		doLog(strMessage + "\r\n" + Util.eToString(t), 4);
	}

	public void out(String s) {
		System.out.println(s);
	}

	public boolean interrupted() {
		return false;
	}

	private ConnectionInfo getConnectionInfo(String connectionName) {
		try {
			ResourceBundle rb = ResourceBundle.getBundle("connection");
			ConnectionInfo ci = new ConnectionInfo();
			ci.driver = rb.getString(connectionName + ".driver");
			ci.url = rb.getString(connectionName + ".url");
			ci.user = rb.getString(connectionName + ".user");
			ci.password = rb.getString(connectionName + ".password");
			return ci;
		} catch (Exception e) {
			out("需要在根路径创建 connection.properties ，并且创建相应的键值：" + connectionName + ".driver , " + connectionName
					+ ".url , " + connectionName + ".user , " + connectionName + ".password");
			throw new RuntimeException(e);
		}
	}

	public void setConnection(String connectionName) {
		setConnection(getConnectionInfo(connectionName));
	}

	public void setConnection(String[] connectionString) {
		this.connectionString1 = connectionString;
	}

	public void setConnection(ConnectionInfo ci) {
		String[] connectionString = { ci.driver, ci.url, ci.user, ci.password };
		setConnection(connectionString);
	}

	public void setStandbyConnection(String connectionName) {
		setStandbyConnection(getConnectionInfo(connectionName));
	}

	public void setStandbyConnection(ConnectionInfo ci) {
		String[] connectionString = { ci.driver, ci.url, ci.user, ci.password };
		setStandbyConnection(connectionString);
	}

	public void setStandbyConnection(String[] connectionString) {
		this.connectionString2 = connectionString;
	}

	public Connection getConnection() throws Exception {
		if (this.connectionString1 == null)
			throw new Exception("配置为null，请先配置Connection");
		Class.forName(this.connectionString1[0]);
		String url = this.connectionString1[1];
		String user = this.connectionString1[2];
		String password = this.connectionString1[3];
		this.conn1 = DriverManager.getConnection(url, user, password);

		if (this.conn1 == null)
			throw new Exception("conn1 is null");
		return this.conn1;
	}

	public Connection getStandbyConnection() throws Exception {
		if (this.connectionString2 == null)
			throw new Exception("配置为null，请先配置Connection");
		Class.forName(this.connectionString2[0]);
		String url = this.connectionString2[1];
		String user = this.connectionString2[2];
		String password = this.connectionString2[3];
		this.conn2 = DriverManager.getConnection(url, user, password);

		if (this.conn2 == null)
			throw new Exception("conn2 is null");
		return this.conn2;
	}

	public String getConfigValue(String strName) {
		if (this.configMap != null)
			return (String) this.configMap.get(strName);
		if (this.properties == null)
			config();
		return this.properties.getProperty(strName);
	}

	public void setConfig(Map map) {
		this.configMap = new HashMap();
		this.configMap.putAll(map);
	}

	public void setConfig(String name) {
		this.configMap = null;
		this.configFile = name;
		System.out.println("配置文件：" + this.classPath + name + ".properties");
	}

	public void service() {
		System.out.println("Please use run()");
	}

	private void close() {
		if (this.conn1 != null)
			try {
				this.conn1.close();
			} catch (Exception localException) {
			}
		if (this.conn2 != null)
			try {
				this.conn2.close();
			} catch (Exception localException1) {
			}
	}

	public HashMap getTaskMap() {
		return this.map;
	}

	public String getFirstTime() {
		return new Date().toLocaleString();
	}

	public String getLastTime() {
		return new Date().toLocaleString();
	}

	public long getExecuteNum() {
		return ++this.nExecuteNum;
	}

	private void config() {
		try {
			this.properties = new Properties();

			String configPath = this.classPath + this.configFile;
			if (!this.configFile.endsWith(".properties"))
				configPath = configPath + ".properties";
			System.out.println("查找配置文件" + configPath + "(charset=" + this.charset + ")");

			this.properties.load(new FileInputStream(configPath));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public LocalTask() {
		Exception ee = new Exception();
		StackTraceElement[] element = ee.getStackTrace();
		String className = element[1].getClassName();
		init(className, className);
	}

	public LocalTask(String taskID, String className) {
		init(taskID, className);
	}

	public void init(String taskID, String className) {
		setTaskID(taskID);
		this.taskLogger = Logger.getLogger(taskID);

		if (this.taskListener != null) {
			this.error = true;
			log(this.ERROR);
		}

		try {
			this.taskInstance = ((TaskListener) Class.forName(className).newInstance());
			this.taskInstance.addTaskListener(this);

			System.out.println("插件主类：" + this.taskInstance.getClass().getName());

			this.classPath = this.taskInstance.getClass().getResource("").getPath();
			this.className = className;
			this.map.put("taskName", className);
			this.map.put("taskID", taskID);
			System.out.println("程序运行路径：" + this.classPath);
			System.out.println("缺省配置文件：" + this.classPath + "config.properties");
		} catch (Exception e) {
			System.out.println("LocalTask():" + e);
			e.printStackTrace(System.out);
		}
	}

	public static void run(String taskId, String className, String configName, String connName, String standByConnName,
			int times, int sleepSeconds) {
		new Thread(new Runnable() {
			private final String val$className=null;
			private final String val$configName=null;
			private final String val$connName=null;
			private final String val$standByConnName=null;
			private final int val$times=0;
			private final int val$sleepSeconds=0;

			public void run() {
				/*LocalTask dt = new LocalTask(LocalTask.this, this.val$className);*/
				LocalTask dt = new LocalTask();
				if (!LocalTask.isEmpty(this.val$configName))
					dt.setConfig(this.val$configName);
				if (!LocalTask.isEmpty(this.val$connName))
					dt.setConnection(this.val$connName);
				if (!LocalTask.isEmpty(this.val$standByConnName))
					dt.setStandbyConnection(this.val$standByConnName);
				dt.run(this.val$times, this.val$sleepSeconds);
			}
		}).start();
	}

	private static boolean isEmpty(String s) {
		return (s == null) || (s.length() == 0);
	}

	public void run() {
		run(1, 0);
	}

	public void run(int sleepSeconds) {
		run(0, sleepSeconds);
	}

	public void run(int times, int sleepSeconds) {
		if (this.error) {
			log(this.ERROR);
			log("不能在正式环境中运行测试的work()");
			return;
		}

		try {
			int n = times;
			try {
				this.taskInstance.service();
				log("本次运行结束");

				if (times > 0) {
					n--;
					if (n <= 0)
						;
				} else {
					log("等待" + sleepSeconds + "秒再次处理");
					Thread.sleep(sleepSeconds * 1000);
				}

			} finally {
				close();
			}

		} catch (Exception e) {
			System.out.println("run():" + e);
			e.printStackTrace(System.out);
		}
		log("程序运行结束");
	}

	public void addTaskListener(TaskListener taskListener) {
		System.out.println("do nothing");
	}

	public void alert(String strMessage) {
		this.list.add(strMessage);
	}

	public void clearAlert() {
		this.list.clear();
	}

	public String getAlert() {
		return this.list.toString();
	}

	public String[][] getConfig() {
		return null;
	}

	public Counter getCounter(String name) throws IOException {
		return Counter.getCounter(name);
	}

	public String[][] getInfo() {
		return this.taskInstance.getInfo();
	}

	public TaskMessage getMessage() {
		return this.taskInstance.getMessage();
	}

	public Map getProfileMap(String category, String profile) throws IOException {
		return ProfileManager.getProfileMap(category, profile);
	}

	public boolean onAlert() {
		return this.list.size() > 0;
	}

	public void removeTaskListener(TaskListener taskListener) {
		System.out.println("do nothing");
	}

	public static void main(String[] args) throws Exception {
		setLogType(1);

		run("001", "com.trs.tcm.demo.Demo1", "config", "conn1", "conn2", 1, 100);

		run("002", "com.trs.tcm.demo.Demo1", "config2", null, null, 1, 100);
	}
}