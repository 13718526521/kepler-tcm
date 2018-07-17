package com.kepler.tcm.domain;

public class Plugin {
	private String pluginid;
	private String pluginMemo;
	private String pluginName;
	private String entryClass;
	private String version;
	private String error;
	private String pluginPath;
	private String fileList;
	
	public String getPluginid() {
		return pluginid;
	}
	public void setPluginid(String pluginid) {
		this.pluginid = pluginid;
	}
	public String getPluginMemo() {
		return pluginMemo;
	}
	public void setPluginMemo(String pluginMemo) {
		this.pluginMemo = pluginMemo;
	}
	public String getPluginName() {
		return pluginName;
	}
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	public String getEntryClass() {
		return entryClass;
	}
	public void setEntryClass(String entryClass) {
		this.entryClass = entryClass;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getPluginPath() {
		return pluginPath;
	}
	public void setPluginPath(String pluginPath) {
		this.pluginPath = pluginPath;
	}
	public String getFileList() {
		return fileList;
	}
	public void setFileList(String fileList) {
		this.fileList = fileList;
	}

}
