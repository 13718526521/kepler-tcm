package com.kepler.tcm.core.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kepler.tcm.core.log.LogA;
import com.kepler.tcm.core.server.Counter;
import com.kepler.tcm.core.server.ProfileManager;
import com.kepler.tcm.core.server.PropertyResourceBundle;
import com.kepler.tcm.core.server.Server;
import com.kepler.tcm.core.util.Config;
import com.kepler.tcm.core.util.Convert;
import com.kepler.tcm.core.util.DateTool;
import com.kepler.tcm.core.util.StringList;
import com.kepler.tcm.core.util.StringTools;
import com.kepler.tcm.core.util.Util;

public class RemoteTaskImpl extends UnicastRemoteObject implements RemoteTask, Runnable, TaskListener {
	private Thread thisTask = null;

	private String strTaskID = "";

	protected long lSleepTimer = 1000L;

	protected long lSleepTimeMS = 60000L;

	protected Calendar cRunTime = Calendar.getInstance();

	protected String cron = null;

	protected int lSleepTimeDAY = 1;

	protected String strFirstRuntime = "";

	protected String strLastRuntime = "";

	protected boolean bIsStarted = false;

	protected long lExecuteNum = 0L;

	protected long lExecuteTime = 0L;

	protected int nLogMaxSize = 2;

	protected int nReadLogLine = 1000;

	protected long lTimeOut = 0L;
	private HashMap propertyMap;
	private TaskListener taskInstance = null;

	private boolean bInterrupted = true;

	private Logger taskLogger = null;

	private Logger serverLogger = LoggerFactory.getLogger("logger_server." + getClass());

	private long classLastModify = -1L;

	private StringList lastAlert = new StringList();
	private StringList currAlert = new StringList();
	private long lastAlertTime = 0L;
	private int lastFailNum = 0;

	private long lastClearAlertTime = 0L;

	private int alertState = 0;
	private final long OneMinute = 60000L;

	public RemoteTaskImpl(String strTaskID) throws Exception, RemoteException {
		this.strTaskID = strTaskID;
		this.propertyMap = new HashMap();
		Collections.synchronizedMap(this.propertyMap);
		readTaskProperty();
		this.taskLogger = LoggerFactory.getLogger("logger_" + this.strTaskID);

		taskLoad();
	}


	public String getConnectRMI() throws Exception {
		String msg = "server connect success!";
		return msg ;
	}
	
	public void start() throws Exception, RemoteException {
		if (isDisabled())
			throw new RemoteException("任务已被禁用.请先从任务属性中去掉‘禁用任务’选项 ");

		if (!this.bIsStarted) {
			readTaskProperty();

			this.lExecuteNum = 1L;
			this.lExecuteTime = 0L;
			this.bInterrupted = false;

			if (this.taskInstance != null) {
				if ((this.taskInstance instanceof ErrorClassHandleTask)) {
					this.serverLogger.info("实例为ErrorClassHandleTask，将尝试重新加载正确的入口类");
					this.taskInstance = null;
				} else if (!this.taskInstance.getClass().getName().equals(this.propertyMap.get("entryClass"))) {
					this.serverLogger.info("入口类已经变化，将会启用新的入口类");
					this.taskInstance = null;
				}

			}

			this.thisTask = new Thread(this);
			this.thisTask.setName((String) this.propertyMap.get("taskName"));
			this.thisTask.start();
			this.bIsStarted = true;
		}
	}

	private void taskLoad() {
		try {
			String pluginId = (String) this.propertyMap.get("pluginId");
			long lastModify = Server.pluginManager.getClassLastModify(pluginId);

			if ((this.taskInstance == null) || (this.classLastModify != lastModify)) {
				this.classLastModify = lastModify;

				this.taskInstance = Server.pluginManager.getTaskInstance(pluginId);

				if (this.taskInstance == null)
					throw new Exception("任务加载失败，可能插件不存在:" + pluginId + " - " + this.propertyMap.get("entryClass"));
				this.taskInstance.addTaskListener(this);
				this.serverLogger.info("Reloaded plugin instance" + pluginId + " from Task " + this.strTaskID);
			}

		} catch (Exception e) {
			this.serverLogger.error("加载类实例时发生错误：" + (String) this.propertyMap.get("entryClass"), e);
		}
	}

	public void stop() throws RemoteException {
		if (this.bIsStarted) {
			this.thisTask.interrupt();
			this.bInterrupted = true;
			clearAlert();
			this.currAlert.clear();
		}
	}

	public void forceStop() throws Exception {
		if (this.bIsStarted) {
			stop();
			Thread.sleep(1000L);
			if (this.bIsStarted)
				this.thisTask.stop();
			if (this.bIsStarted) {
				Thread.sleep(1000L);
				if (this.bIsStarted)
					this.thisTask.destroy();
			}
		}
	}

	public String getTaskName() throws RemoteException {
		return (String) this.propertyMap.get("taskName");
	}

	public String getTaskID() throws RemoteException {
		return (String) this.propertyMap.get("taskID");
	}

	public boolean isStarted() throws RemoteException {
		return this.bIsStarted;
	}

	public TaskMessage getTaskMessage() throws RemoteException {
		TaskMessage taskMessage = getMessage();

		if (this.lExecuteNum > 0L) {
			taskMessage.setOther("第 " + this.lExecuteNum + " 次执行,耗时 " + this.lExecuteTime + " 毫秒");
		}

		return taskMessage;
	}

	public String getFirstRuntime() throws RemoteException {
		return this.strFirstRuntime;
	}

	public String getLastRuntime() throws RemoteException {
		return this.strLastRuntime;
	}

	public String[][] getTaskInfo() throws RemoteException {
		return getInfo();
	}

	public String[][] getTaskConfig() throws RemoteException {
		return getConfig();
	}

	public HashMap getTaskProperty() throws RemoteException {
		return this.propertyMap;
	}

	public HashMap getTaskMap() {
		return this.propertyMap;
	}

	public void run() {
		this.strFirstRuntime = DateTool.getCurrentDate();
		try {
			while (!Thread.interrupted()) {
				try {
					this.lTimeOut = (Integer.parseInt((String) this.propertyMap.get("taskTimeout")) * 60 * 1000);
				} catch (Exception fe) {
					this.serverLogger.error("读取配置taskTimeout时出现异常!" + fe);
				}

				if (this.propertyMap.get("planType").equals("1")) {
					if (this.lTimeOut <= 0L) {
						long lBeginTime = System.currentTimeMillis();
						taskWork();
						long lEndTime = System.currentTimeMillis();

						this.lExecuteTime = (lEndTime <= 0L ? 0L : lEndTime - lBeginTime);
					} else {
						Working working = new Working();
						Thread thread = new Thread(working);
						thread.start();
						thread.join(this.lTimeOut);
						this.lExecuteTime = working.getWorkingHour();
					}

					this.lExecuteNum += 1L;
					this.strLastRuntime = DateTool.getCurrentDate();
					break;
				}

				if (this.propertyMap.get("planType").equals("2")) {
					while (!Thread.interrupted()) {
						Calendar rightNow = Calendar.getInstance();
						if (rightNow.after(this.cRunTime)) {
							if (this.lTimeOut <= 0L) {
								long lBeginTime = System.currentTimeMillis();
								taskWork();
								long lEndTime = System.currentTimeMillis();

								this.lExecuteTime = (lEndTime <= 0L ? 0L : lEndTime - lBeginTime);
								break;
							}

							Working working = new Working();
							Thread thread = new Thread(working);
							thread.start();
							thread.join(this.lTimeOut);

							this.lExecuteTime = working.getWorkingHour();

							break;
						}
						if (interrupted()) {
							throw new InterruptedException();
						}
						synchronized (this) {
							wait(this.lSleepTimer);
						}

					}

					this.lExecuteNum += 1L;
					this.strLastRuntime = DateTool.getCurrentDate();
					break;
				}

				if (this.propertyMap.get("planType").equals("3")) {
					if (this.lTimeOut <= 0L) {
						long lBeginTime = System.currentTimeMillis();
						taskWork();
						long lEndTime = System.currentTimeMillis();

						this.lExecuteTime = (lEndTime <= 0L ? 0L : lEndTime - lBeginTime);
					} else {
						Working working = new Working();
						Thread thread = new Thread(working);
						thread.start();
						thread.join(this.lTimeOut);

						this.lExecuteTime = working.getWorkingHour();
					}

					this.lExecuteNum += 1L;
					this.strLastRuntime = DateTool.getCurrentDate();
					if (interrupted()) {
						throw new InterruptedException();
					}
					synchronized (this) {
						wait(this.lSleepTimeMS);
					}
				}

				if (this.propertyMap.get("planType").equals("4")) {
					while (!Thread.interrupted()) {
						Calendar rightNow = Calendar.getInstance();
						if (rightNow.after(this.cRunTime)) {
							if (this.lTimeOut <= 0L) {
								long lBeginTime = System.currentTimeMillis();
								taskWork();
								long lEndTime = System.currentTimeMillis();

								this.lExecuteTime = (lEndTime <= 0L ? 0L : lEndTime - lBeginTime);
							} else {
								Working working = new Working();
								Thread thread = new Thread(working);
								thread.start();
								thread.join(this.lTimeOut);

								this.lExecuteTime = working.getWorkingHour();
							}

							this.lExecuteNum += 1L;
							this.strLastRuntime = DateTool.getCurrentDate();

							DateTool dateTool = new DateTool(this.lSleepTimeDAY);

							if (this.lSleepTimeDAY <= 0) {
								break;
							}
							int nYear = dateTool.getYear();
							int nMonth = dateTool.getMonth();
							int nDay = dateTool.getDay();
							int nHour = Integer.parseInt((String) this.propertyMap.get("hour4"));
							int nMinute = Integer.parseInt((String) this.propertyMap.get("minute4"));
							int nSecond = Integer.parseInt((String) this.propertyMap.get("second4"));

							this.cRunTime.set(nYear, nMonth - 1, nDay, nHour, nMinute, nSecond);
						}
						if (interrupted()) {
							throw new InterruptedException();
						}
						synchronized (this) {
							wait(this.lSleepTimer);
						}
					}
					break;
				}

				if (this.propertyMap.get("planType").equals("5")) {/*
					CronTrigger ct = new CronTrigger("t", "g", "j", "g", new Date(), null, this.cron);

					Date nextFireTime = ct.computeFirstFireTime(null);
					if (nextFireTime == null) {
						this.serverLogger.info("任务'" + this.strTaskID + "'不需要执行!");
						break;
					}

					while (!Thread.interrupted()) {
						Date rightNow = new Date();
						if (rightNow.after(nextFireTime)) {
							ct.triggered(null);
							try {
								if (this.lTimeOut <= 0L) {
									long lBeginTime = System.currentTimeMillis();
									taskWork();
									long lEndTime = System.currentTimeMillis();

									this.lExecuteTime = (lEndTime <= 0L ? 0L : lEndTime - lBeginTime);
								} else {
									Working working = new Working();
									Thread thread = new Thread(working);
									thread.start();
									thread.join(this.lTimeOut);

									this.lExecuteTime = working.getWorkingHour();
								}
							} finally {
								nextFireTime = ct.getNextFireTime();
								if ((nextFireTime != null) && (rightNow.after(nextFireTime))) {
									this.serverLogger.info("任务'" + this.strTaskID + "'执行时间已经超出下次的执行时间，将重新计算!");
									ct = new CronTrigger("t", "g", "j", "g", new Date(), null, this.cron);
									nextFireTime = ct.computeFirstFireTime(null);
								}

								if (nextFireTime == null) {
									this.serverLogger.info("任务'" + this.strTaskID + "'已经结束执行!");
									break;
								}
							}

						}

						if (interrupted()) {
							throw new InterruptedException();
						}
						synchronized (this) {
							wait(this.lSleepTimer);
						}

					}

					this.lExecuteNum += 1L;
					this.strLastRuntime = DateTool.getCurrentDate();
					break;
				*/}
			}

		} catch (InterruptedException localInterruptedException) {
		} catch (Exception e) {
			this.serverLogger.error("任务'" + this.strTaskID + "'执行时出现异常!", e);
		} finally {
			this.bIsStarted = false;
		}
	}

	private void taskWork() {
		taskLoad();
		int notSuccTime;
		long now;
		long minutes1;
		long minutes2;
		String alertType;
		int keepAlertTime;
		try {
			this.taskInstance.service();
		} catch (Throwable e) {
			String msg = "插件的work()方法抛出异常，导致任务执行中断，异常原因：" + e;
			this.serverLogger.error(msg, e);
			error(msg);
			System.out.println(msg);
			alert(msg);
		} finally {
			/* int notSuccTime; */
			/* long now; */
			/* long minutes1; */
			/* long minutes2; */
			/* String alertType; */
			/* int keepAlertTime; */
			if (Convert.toString(this.propertyMap.get("taskAlert")).equals("1")) {
				if (Convert.toString(this.propertyMap.get("notSuccAlert")).equals("1")) {
					notSuccTime = Convert.toInt(this.propertyMap.get("notSuccTime"), 0);
					now = Calendar.getInstance().getTime().getTime();
					minutes1 = (now - DateTool.parse(getMessage().getLastSuccTime()).getTime()) / 60000L;
					minutes2 = (now - this.lastClearAlertTime) / 60000L;
					if (minutes2 < minutes1)
						minutes1 = minutes2;

					if (minutes1 >= notSuccTime) {
						alert("在" + minutes1 + "分钟内“成功计数”没有增加");
					}

				}

				if (Convert.toString(this.propertyMap.get("failAlert")).equals("1")) {
					if ((this.lastFailNum > 0) && (this.lastFailNum != getMessage().getFail())) {
						alert("当前的“失败计数”为" + getMessage().getFailNum());
						this.lastFailNum = getMessage().getFail();
					}

				}

				if (this.currAlert.size() > 0) {
					this.lastAlert.clear();
					this.lastAlert.addStrings(this.currAlert);
					this.currAlert.clear();
				} else {
					alertType = Convert.toString(this.propertyMap.get("alertType"), "0");
					if (alertType.equals("0")) {
						this.lastAlert.clear();
					}
					if (alertType.equals("1")) {
						keepAlertTime = Convert.toInt(this.propertyMap.get("keepAlertTime"), 10);

						if ((this.lastAlertTime > 0L)
								&& (System.currentTimeMillis() - this.lastAlertTime > 60000L * keepAlertTime)) {
							this.lastAlert.clear();
						}

					}

				}

				this.lastAlertTime = System.currentTimeMillis();
			}
		}
	}

	public void service() {
	}

	public boolean interrupted() {
		return this.bInterrupted;
	}

	public String getFirstTime() {
		return this.strFirstRuntime;
	}

	public String getLastTime() {
		return this.strLastRuntime;
	}

	public long getExecuteNum() {
		return this.lExecuteNum;
	}

	public void addTaskListener(TaskListener taskListener) {
	}

	public void removeTaskListener(TaskListener taskListener) {
	}

	public Connection getConnection() throws Exception {
		if (Server.databaseManager.getDatabaseByName((String) this.propertyMap.get("databaseName")) != null) {
			return Server.databaseManager.getDatabaseByName((String) this.propertyMap.get("databaseName"))
					.getConnection();
		}

		return null;
	}

	public Connection getStandbyConnection() throws Exception {
		if (Server.databaseManager.getDatabaseByName((String) this.propertyMap.get("standbyDatabaseName")) != null) {
			return Server.databaseManager.getDatabaseByName((String) this.propertyMap.get("standbyDatabaseName"))
					.getConnection();
		}

		return null;
	}

	public TaskMessage getMessage() {
		return this.taskInstance.getMessage();
	}

	public String[][] getInfo() {
		return this.taskInstance.getInfo();
	}

	public String getVersion() {
		return ((Task) this.taskInstance).getVersion();
	}

	public String[][] getConfig() {
		taskLoad();

		return this.taskInstance.getConfig();
	}

	private String[][] getConfigDefault() {
		String[][] config = (String[][]) null;
		try {
			String strValue = "";
			String strPath = "./working/" + this.strTaskID + "/" + "config.properties";

			if (new File(strPath).exists()) {
				StringList list = new StringList();
				list.loadFromFile(strPath, Util.getCharset());
				config = new String[list.size()][2];
				for (int i = 0; i < list.size(); i++) {
					config[i][0] = list.getName(i).trim();
					config[i][1] = list.getValue(i).trim();
				}
			}

		} catch (Exception ex) {
			this.serverLogger.error("getConfigDefault()时出现异常!" + ex);
		}

		return config;
	}

	private void init() {
		try {
			if (this.propertyMap.get("planType").equals("2")) {
				int nYear = Integer.parseInt((String) this.propertyMap.get("year"));
				int nMonth = Integer.parseInt((String) this.propertyMap.get("month"));
				int nDay = Integer.parseInt((String) this.propertyMap.get("day"));
				int nHour = Integer.parseInt((String) this.propertyMap.get("hour"));
				int nMinute = Integer.parseInt((String) this.propertyMap.get("minute"));
				int nSecond = Integer.parseInt((String) this.propertyMap.get("second"));

				this.cRunTime.set(nYear, nMonth - 1, nDay, nHour, nMinute, nSecond);
			}
			if (this.propertyMap.get("planType").equals("3")) {
				this.lSleepTimeMS = ((long) (Float.parseFloat((String) this.propertyMap.get("mxf")) * 1000.0F * 60.0F));
			}
			if (this.propertyMap.get("planType").equals("4")) {
				int nYear = Calendar.getInstance().get(1);
				int nMonth = Calendar.getInstance().get(2);
				int nDay = Calendar.getInstance().get(5);
				int nHour = Integer.parseInt((String) this.propertyMap.get("hour4"));
				int nMinute = Integer.parseInt((String) this.propertyMap.get("minute4"));
				int nSecond = Integer.parseInt((String) this.propertyMap.get("second4"));

				this.cRunTime.set(nYear, nMonth, nDay, nHour, nMinute, nSecond);

				this.lSleepTimeDAY = Integer.parseInt((String) this.propertyMap.get("mxt"));
			}
			if (this.propertyMap.get("planType").equals("5")) {
				this.cron = ((String) this.propertyMap.get("cron"));
			}

		} catch (Exception e) {
			this.serverLogger.error("任务'" + this.strTaskID + "'初始化时出现异常!" + e);
		}
	}

	public void readTaskProperty() {
		try {
			PropertyResourceBundle prb = new PropertyResourceBundle(
					"./working/" + this.strTaskID + "/" + "task.properties");

			String pluginId = prb.getString("pluginId");
			String pluginName = prb.getString("pluginName");
			String entryClass = "";

			if ((pluginId == null) || (pluginId.length() == 0)) {
				if ((pluginName != null) && (pluginName.length() > 0)) {
					String[][] ps = Server.pluginManager.getPlugins();
					for (int i = 0; i < ps.length; i++)
						if ((ps[i][1] != null) && (ps[i][1].equals(pluginName))) {
							pluginId = ps[i][0];
							break;
						}
				}
			}

			if ((pluginId != null) && (pluginId.length() > 0)) {
				Map p = Server.pluginManager.getPropertyById(pluginId);
				if (p == null) {
					this.serverLogger.error("找不到插件：id=" + pluginId);
				} else {
					pluginName = (String) p.get("pluginName");
					entryClass = (String) p.get("entryClass");
				}
			}

			if (entryClass != null)
				entryClass = entryClass.trim();
			if ((entryClass == null) || (entryClass.length() == 0))
				this.serverLogger.error("插件配置不正确，入口类为空");

			this.propertyMap.clear();

			this.propertyMap.put("pluginId", pluginId);
			this.propertyMap.put("pluginName", pluginName);
			this.propertyMap.put("entryClass", entryClass);

			this.propertyMap.put("taskName", prb.getString("taskName"));
			this.propertyMap.put("taskID", prb.getString("taskID"));
			this.propertyMap.put("databaseName", prb.getString("databaseName"));
			this.propertyMap.put("standbyDatabaseName", prb.getString("standbyDatabaseName"));
			this.propertyMap.put("planType", prb.getString("planType"));
			this.propertyMap.put("mxf", prb.getString("mxf"));
			this.propertyMap.put("mxt", prb.getString("mxt"));
			this.propertyMap.put("year", prb.getString("year"));
			this.propertyMap.put("month", prb.getString("month"));
			this.propertyMap.put("day", prb.getString("day"));
			this.propertyMap.put("hour", prb.getString("hour"));
			this.propertyMap.put("minute", prb.getString("minute"));
			this.propertyMap.put("second", prb.getString("second"));
			this.propertyMap.put("hour4", prb.getString("hour4"));
			this.propertyMap.put("minute4", prb.getString("minute4"));
			this.propertyMap.put("second4", prb.getString("second4"));
			this.propertyMap.put("logType", prb.getString("logType"));
			this.propertyMap.put("logBackNums", prb.getString("logBackNums"));
			this.propertyMap.put("logLevel", prb.getString("logLevel"));
			this.propertyMap.put("logLevel2", prb.getString("logLevel2"));
			this.propertyMap.put("logMerge", prb.getString("logMerge"));
			this.propertyMap.put("logMaxSize", prb.getString("logMaxSize"));
			this.propertyMap.put("logLineNum", prb.getString("logLineNum"));
			this.propertyMap.put("taskTimeout", prb.getString("taskTimeout"));
			this.propertyMap.put("autoRun", prb.getString("autoRun"));
			this.propertyMap.put("disabled", prb.getString("disabled"));
			this.propertyMap.put("taskAlert", prb.getString("taskAlert"));
			this.propertyMap.put("alertType", prb.getString("alertType"));
			this.propertyMap.put("keepAlertTime", prb.getString("keepAlertTime"));
			this.propertyMap.put("notSuccAlert", prb.getString("notSuccAlert"));
			this.propertyMap.put("notSuccTime", prb.getString("notSuccTime"));
			this.propertyMap.put("failAlert", prb.getString("failAlert"));
			this.propertyMap.put("cron", prb.getString("cron"));

			prb.close();

			init();
		} catch (Exception ex) {
			this.serverLogger.error("读取任务'" + this.strTaskID + "'属性时出现异常!" + ex);
		}
	}

	public void saveConfigProperty(HashMap propertyMap) throws RemoteException {
		try {
			Iterator it = propertyMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry) it.next();
				String value = (String) e.getValue();
				if (value != null) {
					value = StringTools.encodeEnter(value);
					e.setValue(value);
				}

			}

			Util.writePropertyFile(propertyMap, "./working/" + this.strTaskID + "/" + "config.properties");
		} catch (Exception ex) {
			this.serverLogger.error("保存任务'" + this.strTaskID + "'配置时出现异常!" + ex);
		}
	}

	public String getConfigValue(String strKey) {
		try {
			String strValue = "";
			String strPath = "./working/" + this.strTaskID + "/" + "config.properties";

			if (!new File(strPath).exists()) {
				return "";
			}

			PropertyResourceBundle prb = new PropertyResourceBundle(strPath);
			strValue = prb.getString(strKey);
			if (strValue != null)
				strValue = StringTools.decodeEnter(strValue);
			prb.close();
			return strValue;
		} catch (Exception ex) {
			this.serverLogger.error("读取任务'" + this.strTaskID + "'配置时出现异常!" + ex);
		}
		return "";
	}

	public long getTotalLogSize(String name) throws RemoteException {
		String taskLogPath = Server.logPath + "/" + Server.SERVER + "/" + this.strTaskID + "/";
		LogA log = new LogA(taskLogPath, name, Config.getCharset(Server.SERVER));
		return log.getTotalSize();
	}

	public String getTaskLog2(String name, int pageNo, int pageSize) throws RemoteException {
		String taskLogPath = Server.logPath + "/" + Server.SERVER + "/" + this.strTaskID + "/";
		LogA log = new LogA(taskLogPath, name, Config.getCharset(Server.SERVER));
		StringWriter w = new StringWriter(pageSize);
		try {
			log.displayReverse(w, -1, pageNo, pageSize);
			return w.toString();
		} catch (Exception e) {
		}
		return "";
	}

	public String[] getTaskLog() throws RemoteException {
		try {
			try {
				this.nReadLogLine = Integer.parseInt((String) this.propertyMap.get("logLineNum"));
			} catch (Exception e) {
				this.serverLogger.warn("读取配置logLineNum出现异常!" + e);
			}

			String strLogPath = "./working/" + this.strTaskID + "/" + "out.log";

			if (!new File(strLogPath).exists()) {
				return new String[0];
			}

			String[] logs = new String[0];
			int nLineNum = 0;
			String strLine = "";

			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(strLogPath), Util.getCharset()));
			while (br.readLine() != null) {
				nLineNum++;
			}
			br.close();

			if ((this.nReadLogLine <= 0) || (nLineNum <= this.nReadLogLine)) {
				logs = new String[nLineNum];
				br = new BufferedReader(new FileReader(strLogPath));
				for (int i = 0; i < logs.length; i++) {
					logs[i] = br.readLine();
				}
				br.close();
			} else {
				logs = new String[this.nReadLogLine];
				int nOldLine = nLineNum - logs.length;

				int nNewLine = 0;
				nLineNum = 0;
				br = new BufferedReader(new FileReader(strLogPath));
				while ((strLine = br.readLine()) != null) {
					nLineNum++;
					if ((nLineNum > nOldLine) && (nNewLine < logs.length)) {
						logs[(nNewLine++)] = strLine;
					}
				}
				br.close();
			}

			return logs;
		} catch (Exception ex) {
			this.serverLogger.error("读取任务'" + this.strTaskID + "'日志时出现异常!" + ex);
		}
		return new String[0];
	}

	public void log(String strMessage) {
		this.taskLogger.info(strMessage);
	}

	public void debug(String strMessage) {
		if (this.taskLogger.isDebugEnabled())
			this.taskLogger.debug(strMessage);
	}

	public void info(String strMessage) {
		this.taskLogger.info(strMessage);
	}

	public void error(String strMessage) {
		this.taskLogger.error(strMessage);
	}

	

	public void warn(String strMessage) {
		this.taskLogger.warn(strMessage);
	}

	public void debug(Object strMessage) {
		if (this.taskLogger.isDebugEnabled())
			this.taskLogger.debug(""+strMessage);
	}

	public void info(Object message) {
		this.taskLogger.info(""+message);
	}

	public void warn(Object message) {
		this.taskLogger.warn(""+message);
	}


	public void log(Object strMessage) {
		info(strMessage);
	}

	private String[] formatMessage(String strMessage) {
		if ((strMessage == null) || ("".equals(strMessage))) {
			return new String[] { strMessage };
		}
		strMessage = strMessage.replaceAll("\\|", "-");
		return strMessage.split("[\\r|\\n]");
	}

	public void alert(String strMessage) {
		this.currAlert.add(DateTool.getCurrentDate() + " " + strMessage);
	}

	public String getAlert() {
		if (Convert.toString(this.propertyMap.get("taskAlert")).equals("1"))
			return this.lastAlert.getLimitText(-20);
		return "";
	}

	public boolean onAlert() {
		return (Convert.toString(this.propertyMap.get("taskAlert")).equals("1")) && (this.lastAlert.size() > 0);
	}

	public void clearAlert() {
		this.lastClearAlertTime = Calendar.getInstance().getTime().getTime();
		this.lastAlert.clear();
	}

	public Counter getCounter(String name) throws IOException {
		return Counter.getCounter(name);
	}

	public Map getProfileMap(String category, String profile) throws IOException {
		return ProfileManager.getProfileMap(category, profile);
	}

	public String getErrorClassInfo() throws RemoteException {
		if ((this.taskInstance != null) && ((this.taskInstance instanceof ErrorClassHandleTask))) {
			return ((ErrorClassHandleTask) this.taskInstance).getError();
		}
		return null;
	}

	public boolean isDisabled() throws RemoteException {
		return "1".equals(this.propertyMap.get("disabled"));
	}

	class Working implements Runnable

	{
		private long lBeginTime = 0L;

		private long lEndTime = 0L;

		Working() {
		}

		public void run() {
			this.lBeginTime = System.currentTimeMillis();
			RemoteTaskImpl.this.taskWork();
			this.lEndTime = System.currentTimeMillis();
		}

		public long getWorkingHour() {
			if (this.lEndTime <= 0L) {
				return 0L;
			}

			return this.lEndTime - this.lBeginTime;
		}
	}

	public void fatal(String paramString) {
		// TODO Auto-generated method stub
		
	}

	public void error(Object paramObject) {
		// TODO Auto-generated method stub
		
	}

	public void error(Object paramObject, Throwable paramThrowable) {
		// TODO Auto-generated method stub
		
	}

	public void fatal(Object paramObject) {
		// TODO Auto-generated method stub
		
	}

	public void fatal(Object paramObject, Throwable paramThrowable) {
		// TODO Auto-generated method stub
		
	}
}