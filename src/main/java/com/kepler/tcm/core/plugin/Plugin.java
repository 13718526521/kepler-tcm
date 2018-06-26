package com.kepler.tcm.core.plugin;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kepler.tcm.core.loader.TCMClassLoader;
import com.kepler.tcm.core.task.ErrorClassHandleTask;
import com.kepler.tcm.core.task.TaskListener;

public class Plugin implements Cloneable, Serializable {
	
	
	private Logger serverLogger = LoggerFactory.getLogger("logger_server." + getClass());

	private String strPluginName = "";

	private String strPluginMemo = "";

	private String strEntryClass = "";

	private String strPluginPath = "";

	private String strFileList = "";

	private String strLibList = "";

	private long classLastModify = -1L;

	private Class taskClass = null;

	private String id = "";

	private String error = "";

	private String version = "1.0";

	private TCMClassLoader taskClassLoader = null;

	private Throwable loadError = null;

	public Plugin() {
	}

	public Plugin(String id, String strPluginName, String strPluginMemo, String strEntryClass, String strPluginPath,
			String classList, String libList, long classLastModify) {
		this.id = id;
		this.strPluginName = strPluginName;
		this.strPluginMemo = strPluginMemo;
		this.strEntryClass = strEntryClass;
		this.strPluginPath = strPluginPath;
		this.strFileList = classList;
		this.strLibList = libList;
		this.classLastModify = classLastModify;
		try {
			Class c = loadPlugin(strEntryClass);
			if (c == null)
				throw new Exception("插件不存在");

			try {
				c.getField("WEB_TCM_SERVER_TASK");
				this.version = ((String) c.getMethod("getVersion", null).invoke(null, null));
			} catch (Exception e) {
				this.serverLogger.error(""+e);
				throw new Exception("插件不正确");
			}

		} catch (Exception e) {
			this.error = e + "";
			this.serverLogger.error("验证插件" + strEntryClass + "报错：", e);
		}
	}

	public String getEntryClass() {
		return this.strEntryClass;
	}

	public String getPluginMemo() {
		return this.strPluginMemo;
	}

	public String getPluginName() {
		return this.strPluginName;
	}

	public String getPluginPath() {
		return this.strPluginPath;
	}

	public void setEntryClass(String string) {
		this.strEntryClass = string;
	}

	public void setPluginMemo(String string) {
		this.strPluginMemo = string;
	}

	public void setPluginName(String string) {
		this.strPluginName = string;
	}

	public void setFileList(String s) {
		this.strFileList = s;
	}

	public String getFileList() {
		return this.strFileList;
	}

	public void setLibList(String s) {
		this.strLibList = s;
	}

	public String getLibList() {
		return this.strLibList;
	}

	public long getClassLastModify() {
		return this.classLastModify;
	}

	public synchronized void setClassLastModify(long classLastModify) {
		try {
			reloadTaskInstance(this.strEntryClass, true);
		} catch (Throwable e) {
			this.serverLogger.error("加载任务类" + this.strEntryClass + "时发生错误，将用ErrorClassHandleTask来接管此任务", e);

			this.taskClass = ErrorClassHandleTask.class;
			this.loadError = e;
		}

		this.classLastModify = classLastModify;
	}

	public synchronized TaskListener getTaskInstance() {
		TaskListener result = null;
		try {
			if (this.taskClass == null) {
				reloadTaskInstance(this.strEntryClass, false);
			}
			result = (TaskListener) this.taskClass.newInstance();

			result.getClass().getDeclaredFields();
		} catch (Throwable e) {
			this.serverLogger
					.error("加载任务类实例" + this.strEntryClass + "时发生错误，将用ErrorClassHandleTask来接管此任务。请配置正确的任务插件并重启任务", e);

			this.taskClass = ErrorClassHandleTask.class;
			this.loadError = e;
			result = new ErrorClassHandleTask();
		}

		if ((result instanceof ErrorClassHandleTask))
			((ErrorClassHandleTask) result).setError(this.strEntryClass, this.loadError);
		return result;
	}

	private Class loadPlugin(String className) throws Exception {
		this.taskClassLoader = new TCMClassLoader(getClass().getClassLoader());

		String[][] paths = { { getPluginPath(), "classes" }, { getPluginPath(), "lib" } };

		this.taskClassLoader.setPath(paths);
		return this.taskClassLoader.loadClass(className);
	}

	private synchronized void reloadTaskInstance(String className, boolean force) throws Exception {
		if ((this.taskClass == null) || (force)) {
			this.serverLogger.info((this.taskClass == null ? "Load" : "Reload") + " Plugin " + className
					+ " (Plugin.reloadTaskInstance)");
			this.taskClass = loadPlugin(className);
		}
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getError() {
		return this.error;
	}

	public void setError(String error) {
		this.error = error;
	}
}