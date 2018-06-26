package com.kepler.tcm.core.task;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.kepler.tcm.core.server.Counter;


public abstract interface TaskListener {
	public abstract void service();

	public abstract void log(String paramString);

	public abstract void debug(String paramString);

	public abstract void info(String paramString);

	public abstract void error(String paramString);

	public abstract void fatal(String paramString);

	public abstract void warn(String paramString);

	public abstract void log(Object paramObject);

	public abstract void debug(Object paramObject);

	public abstract void info(Object paramObject);

	public abstract void error(Object paramObject);

	public abstract void error(Object paramObject, Throwable paramThrowable);

	public abstract void fatal(Object paramObject);

	public abstract void fatal(Object paramObject, Throwable paramThrowable);

	public abstract void warn(Object paramObject);

	public abstract void alert(String paramString);

	public abstract String getAlert();

	public abstract boolean onAlert();

	public abstract void clearAlert();

	public abstract Connection getConnection() throws Exception;

	public abstract Connection getStandbyConnection() throws Exception;

	public abstract HashMap getTaskMap();

	public abstract TaskMessage getMessage();

	public abstract String[][] getInfo();

	public abstract String[][] getConfig();

	public abstract String getConfigValue(String paramString);

	public abstract boolean interrupted();

	public abstract String getFirstTime();

	public abstract String getLastTime();

	public abstract long getExecuteNum();

	public abstract void addTaskListener(TaskListener paramTaskListener);

	public abstract void removeTaskListener(TaskListener paramTaskListener);

	public abstract Map getProfileMap(String paramString1, String paramString2) throws IOException;

	public abstract Counter getCounter(String paramString) throws IOException;
}
