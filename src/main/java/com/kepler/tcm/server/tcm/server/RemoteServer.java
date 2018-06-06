package com.kepler.tcm.server.tcm.server;

import java.rmi.Remote;
import java.util.HashMap;
import java.util.Map;

public abstract interface RemoteServer extends Remote {
	public abstract String getServerName() throws Exception;

	public abstract String[][] getPlugins() throws Exception;

	public abstract boolean addPlugin(HashMap paramHashMap) throws Exception;

	public abstract boolean modifyPlugin(HashMap paramHashMap) throws Exception;

	public abstract boolean removePlugin(String paramString) throws Exception;

	public abstract void deletePluginFile(String paramString1, String paramString2) throws Exception;

	public abstract void reloadPlugin(String paramString) throws Exception;

	public abstract HashMap getPluginPropertyById(String paramString) throws Exception;

	public abstract String[] getDatabase() throws Exception;

	public abstract HashMap getDatabasePropertyByName(String paramString) throws Exception;

	public abstract HashMap getTasks() throws Exception;

	public abstract RemoteTask getTask(String paramString) throws Exception;

	public abstract Object getT1(String paramString) throws Exception;

	public abstract HashMap getT2(String paramString) throws Exception;

	public abstract String getTaskID() throws Exception;

	public abstract void addTask(HashMap paramHashMap) throws Exception;

	public abstract void removeTask(String paramString) throws Exception;

	public abstract void modifyTask(HashMap paramHashMap) throws Exception;

	public abstract void verifyTaskCron(String paramString) throws Exception;

	public abstract void addDatabase(HashMap paramHashMap) throws Exception;

	public abstract void modifyDatabase(String paramString, HashMap paramHashMap) throws Exception;

	public abstract void removeDatabase(String paramString) throws Exception;

	public abstract boolean testDatabaseConnection(HashMap paramHashMap) throws Exception;

	public abstract String testConnection(HashMap paramHashMap) throws Exception;

	public abstract String getCharset() throws Exception;

	public abstract HashMap getMemoInfo() throws Exception;

	public abstract String[] getCounterNames() throws Exception;

	public abstract String[][] getCounters() throws Exception;

	public abstract long getCounterValue(String paramString) throws Exception;

	public abstract void modifyCounter(String paramString, long paramLong) throws Exception;

	public abstract void addCounter(String paramString, long paramLong) throws Exception;

	public abstract void removeCounter(String paramString) throws Exception;

	public abstract void addCategory(String paramString1, String paramString2, String paramString3) throws Exception;

	public abstract boolean renameCategory(String paramString1, String paramString2) throws Exception;

	public abstract void modifyCategory(String paramString1, String paramString2, String paramString3) throws Exception;

	public abstract boolean removeCaegory(String paramString) throws Exception;

	public abstract String[] getCategoryNames() throws Exception;

	public abstract String[][] getCategories() throws Exception;

	public abstract String[][] getCategoryParamStruct(String paramString) throws Exception;

	public abstract String getCategoryMemo(String paramString) throws Exception;

	public abstract String[][] getCategoryProfiles(String paramString) throws Exception;

	public abstract void addProfile(String paramString1, String paramString2, String paramString3, Map paramMap)
			throws Exception;

	public abstract void modifyProfile(String paramString1, String paramString2, String paramString3, Map paramMap)
			throws Exception;

	public abstract boolean removeProfile(String paramString1, String paramString2) throws Exception;

	public abstract boolean renameProfile(String paramString1, String paramString2, String paramString3)
			throws Exception;

	public abstract String[][] getProfile(String paramString1, String paramString2) throws Exception;

	public abstract String getProfileMemo(String paramString1, String paramString2) throws Exception;

	public abstract Map getProfileMap(String paramString1, String paramString2) throws Exception;

	public abstract void exit() throws Exception;
}