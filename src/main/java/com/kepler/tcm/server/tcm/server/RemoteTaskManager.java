package com.kepler.tcm.server.tcm.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;

public class RemoteTaskManager {
	private Logger serverLogger = Logger.getLogger("logger_server." + getClass());

	private HashMap hmTasks = null;

	public RemoteTaskManager() {
		this.hmTasks = new LinkedHashMap();
		Collections.synchronizedMap(this.hmTasks);
	}

	public void load() {
		String taskID = "";
		try {
			File file = new File("./working");
			if (!file.exists()) {
				file.mkdirs();
			}

			this.serverLogger.info("Server.WORK_PATH is " + file.getCanonicalPath());

			File[] tasks = file.listFiles();
			Arrays.sort(tasks);
			for (int i = 0; i < tasks.length; i++)
				if (tasks[i].getName().length() == 8) {
					try {
						if (!new File(tasks[i].getPath() + "/" + "task.properties").exists()) {
							this.serverLogger.error("加载任务'" + tasks[i].getPath() + "'时出现异常!找不到任务属性文件，将忽略此任务");
						} else {
							PropertyResourceBundle prb = new PropertyResourceBundle(
									tasks[i].getPath() + "/" + "task.properties");

							taskID = prb.getString("taskID");
							Log4jProperties lp = new Log4jProperties();

							lp.setLogType(prb.getStr("logType", "2"));

							lp.setLogMaxSize(prb.getStr("logMaxSize", "2"));
							lp.setLogBackNums(prb.getStr("logBackNums", "5"));
							lp.setLogLevel(prb.getStr("logLevel", "DEBUG"));
							lp.setLogLevel2(prb.getStr("logLevel2", "ERROR"));
							lp.setMerged(prb.getString("logMerge"));

							Server.log4jManager.addTaskLogger(taskID, lp);

							RemoteTaskImpl task = new RemoteTaskImpl(taskID);

							if (("1".equals(prb.getString("autoRun"))) && (!task.isDisabled()))
								task.start();

							this.hmTasks.put(taskID, task);
							prb.close();
						}
					} catch (Exception ex) {
						this.serverLogger.error("加载任务'" + tasks[i].getPath() + "'时出现异常!", ex);
					}
				}
		} catch (Exception e) {
			this.serverLogger.error("加载任务时出现异常!", e);
		}
	}

	public void reload() {
		this.hmTasks.clear();
		load();
	}

	public HashMap getTasks() {
		return this.hmTasks;
	}

	public synchronized String getTaskID() {
		String strTaskID = "";
		try {
			if (!new File("./conf/TaskID").exists()) {
				Util.write("./conf/TaskID", false, "1");
			}

			BufferedReader br = new BufferedReader(new FileReader("./conf/TaskID"));
			strTaskID = br.readLine().trim();
			br.close();

			Util.write("./conf/TaskID", false, Long.parseLong(strTaskID) + 1L + "");
		} catch (Exception e) {
			this.serverLogger.error("获取任务ID异常!", e);
		}

		return Util.fillZero(strTaskID, 8);
	}

	public RemoteTask getTask(String taskID) {
		return (RemoteTask) this.hmTasks.get(taskID);
	}

	public void addTask(HashMap propertyMap) throws Exception {
		try {
			Util.writePropertyFile(propertyMap, "./working/" + propertyMap.get("taskID") + "/" + "task.properties");
			System.out.println("add task:" + propertyMap.get("taskID"));
			this.hmTasks.put(propertyMap.get("taskID"), new RemoteTaskImpl((String) propertyMap.get("taskID")));
			Log4jProperties lp = new Log4jProperties();
			lp.setLogType((String) propertyMap.get("logType"));
			lp.setLogMaxSize((String) propertyMap.get("logMaxSize"));
			lp.setLogBackNums((String) propertyMap.get("logBackNums"));
			lp.setLogLevel((String) propertyMap.get("logLevel"));
			lp.setLogLevel2((String) propertyMap.get("logLevel2"));
			lp.setMerged((String) propertyMap.get("logMerge"));

			Server.log4jManager.addTaskLogger((String) propertyMap.get("taskID"), lp);
		} catch (Exception e) {
			this.serverLogger.error("添加新任务异常!", e);
			throw e;
		}
	}

	public void removeTask(String strTaskID) throws Exception {
		try {
			RemoteTaskImpl remoteTask = (RemoteTaskImpl) this.hmTasks.get(strTaskID);
			if (remoteTask != null)
				remoteTask.forceStop();

			File taskDir = new File("./working/" + strTaskID);
			if (taskDir.isDirectory()) {
				File[] files = taskDir.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
				taskDir.delete();
			}
			this.hmTasks.remove(strTaskID);
			Server.log4jManager.removeTaskLogger(strTaskID);
		} catch (Exception e) {
			this.serverLogger.error("删除任务异常!", e);
			throw e;
		}
	}

	public void modifyTask(HashMap propertyMap) throws Exception {
		try {
			RemoteTaskImpl remoteTask = (RemoteTaskImpl) this.hmTasks.get(propertyMap.get("taskID"));
			HashMap oldPropertyMap = remoteTask.getTaskMap();
			Log4jProperties lp1 = new Log4jProperties();
			lp1.setLogType((String) oldPropertyMap.get("logType"));
			lp1.setLogMaxSize((String) oldPropertyMap.get("logMaxSize"));
			lp1.setLogBackNums((String) oldPropertyMap.get("logBackNums"));
			lp1.setLogLevel((String) oldPropertyMap.get("logLevel"));
			lp1.setLogLevel2((String) oldPropertyMap.get("logLevel2"));
			lp1.setMerged((String) oldPropertyMap.get("logMerge"));

			Util.writePropertyFile(propertyMap, "./working/" + propertyMap.get("taskID") + "/" + "task.properties");

			remoteTask.readTaskProperty();

			Log4jProperties lp2 = new Log4jProperties();
			lp2.setLogType((String) propertyMap.get("logType"));
			lp2.setLogMaxSize((String) propertyMap.get("logMaxSize"));
			lp2.setLogBackNums((String) propertyMap.get("logBackNums"));
			lp2.setLogLevel((String) propertyMap.get("logLevel"));
			lp2.setLogLevel2((String) propertyMap.get("logLevel2"));
			lp2.setMerged((String) propertyMap.get("logMerge"));

			if (lp1.compareTo(lp2) != 0)
				Server.log4jManager.modifyTaskLogger((String) propertyMap.get("taskID"), lp2);
		} catch (Exception e) {
			this.serverLogger.error("修改任务异常!", e);
			throw e;
		}
	}
}