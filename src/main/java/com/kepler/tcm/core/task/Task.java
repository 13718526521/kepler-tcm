package com.kepler.tcm.core.task;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kepler.tcm.core.server.Counter;
import com.kepler.tcm.core.util.Convert;

public abstract class Task implements TaskListener {
	public static final String WEB_TCM_SERVER_TASK = null;

	static final ThreadLocal taskThreadLocal = new ThreadLocal();

	protected TaskListener taskListener = null;

	protected TaskMessage message = new TaskMessage();

	private List connectionList = new ArrayList();

	public void service() {
		taskThreadLocal.set(this);
		int i;
		Connection conn;
		try {
			work();
		} finally {
			for (i = 0; i < this.connectionList.size(); i++) {
				conn = (Connection) this.connectionList.get(i);
				if (conn != null)
					try {
						conn.close();
					} catch (Exception localException) {
					}
				this.connectionList.clear();
			}
		}
	}

	public abstract void work();

	public TaskMessage getMessage() {
		return this.message;
	}

	public String[][] getInfo() {
		String strState = interrupted() ? "未启动" : "运行中";
		String[][] info = { { "任务名称: ", getTaskMap().get("taskName") + "(" + strState + ")" },
				{ "引用插件: ",
						getTaskMap().get("pluginName") + " " + getVersion() + " (" + getTaskMap().get("pluginId")
								+ ")" },
				{ "任务启动时间: ", getFirstTime() }, { "最后运行时间: ", getLastTime() }, { "运行次数: ", getExecuteNum() + "" },
				{ "成功: ", this.message.getSuccNum() + " (" + this.message.getLastSuccTime() + ")" },
				{ "失败: ", this.message.getFailNum() + " (" + this.message.getLastFailTime() + ")" },
				{ "等待: ", this.message.getWaitNum() + " (" + this.message.getLastWaitTime() + ")" },
				{ "忽略: ", this.message.getPassNum() + " (" + this.message.getLastPassTime() + ")" } };

		return info;
	}

	public String[][] getConfig() {
		Object[] paramArray = (Object[]) null;
		try {
			Field f = getClass().getDeclaredField("params");
			f.setAccessible(true);
			Object o = f.get(this);
			if (o.getClass().isArray()) {
				paramArray = (Object[]) f.get(this);
			} else {
				String s = (String) o;
				s = s.replaceAll("[ 　;,；，]+", ",");
				paramArray = s.split("[,]");
			}

		} catch (Exception localException) {
		}

		if (paramArray == null)
			return null;

		String[][] config = new String[paramArray.length][3];

		for (int i = 0; i < paramArray.length; i++) {
			Object o = paramArray[i];
			String name = "";
			String memo = "";
			if (o.getClass().isArray()) {
				String[] os = (String[]) o;
				name = os[0];
				memo = os[1];
			} else {
				name = (String) paramArray[i];
			}
			if (name.length() == 0)
				name = "NoName" + i;
			config[i][0] = name;
			config[i][1] = Convert.toString(getConfigValue(name));
			config[i][2] = memo;
		}

		return config;
	}

	/** @deprecated */
	public void log(String message) {
		this.taskListener.log(message);
	}

	public void debug(String message) {
		this.taskListener.debug(message);
	}

	public void info(String message) {
		this.taskListener.info(message);
	}

	public void error(String message) {
		this.taskListener.error(message);
	}

	public void fatal(String message) {
		this.taskListener.fatal(message);
	}

	public void warn(String message) {
		this.taskListener.warn(message);
	}

	public void debug(Object message) {
		this.taskListener.debug(message);
	}

	/** @deprecated */
	public void log(Object message) {
		this.taskListener.info(message);
	}

	public void info(Object message) {
		this.taskListener.info(message);
	}

	public void error(Object message) {
		this.taskListener.error(message);
	}

	public void error(Object message, Throwable t) {
		this.taskListener.error(message, t);
	}

	public void warn(Object message) {
		this.taskListener.warn(message);
	}

	public void fatal(Object message) {
		this.taskListener.fatal(message);
	}

	public void fatal(Object message, Throwable t) {
		this.taskListener.fatal(message, t);
	}

	public Connection getConnection() throws Exception {
		Connection conn = this.taskListener.getConnection();
		if (conn != null)
			this.connectionList.add(conn);
		return conn;
	}

	public Connection getStandbyConnection() throws Exception {
		Connection conn = this.taskListener.getStandbyConnection();
		if (conn != null)
			this.connectionList.add(conn);
		return conn;
	}

	public HashMap getTaskMap() {
		return this.taskListener.getTaskMap();
	}

	public String getConfigValue(String strKey) {
		return this.taskListener.getConfigValue(strKey);
	}

	public boolean interrupted() {
		return this.taskListener.interrupted();
	}

	public String getFirstTime() {
		return this.taskListener.getFirstTime();
	}

	public String getLastTime() {
		return this.taskListener.getLastTime();
	}

	public long getExecuteNum() {
		return this.taskListener.getExecuteNum();
	}

	public void addTaskListener(TaskListener taskListener) {
		this.taskListener = taskListener;
	}

	public void removeTaskListener(TaskListener taskListener) {
		this.taskListener = null;
	}

	public void alert(String message) {
		this.taskListener.alert(message);
	}

	public String getAlert() {
		return this.taskListener.getAlert();
	}

	public boolean onAlert() {
		return this.taskListener.onAlert();
	}

	public void clearAlert() {
		this.taskListener.clearAlert();
	}

	public String getTaskID() {
		return ((String) getTaskMap().get("taskID")).trim();
	}

	public String getTaskPath() {
		return "./working/" + getTaskID();
	}

	public Map getProfileMap(String category, String profile) throws IOException {
		return this.taskListener.getProfileMap(category, profile);
	}

	public Counter getCounter(String name) throws IOException {
		return this.taskListener.getCounter(name);
	}

	public static String getVersion() {
		return "No Version";
	}

	public static String getName() {
		return "No Name";
	}

	public static String getMemo() {
		return "No Memo";
	}

	public static Task getCurrent() {
		return (Task) taskThreadLocal.get();
	}
}
