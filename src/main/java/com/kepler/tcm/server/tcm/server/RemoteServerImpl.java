package com.kepler.tcm.server.tcm.server;

import com.kepler.tcm.server.core.util.Convert;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.quartz.CronTrigger;

public class RemoteServerImpl extends UnicastRemoteObject implements RemoteServer {
	public RemoteServerImpl() throws RemoteException {
	}

	public String getServerName() throws Exception {
		return Server.SERVER;
	}

	public String[][] getPlugins() throws Exception {
		return Server.pluginManager.getPlugins();
	}

	public boolean addPlugin(HashMap propertyMap) throws Exception {
		return Server.pluginManager.addPlugin(propertyMap);
	}

	public boolean modifyPlugin(HashMap propertyMap) throws Exception {
		return Server.pluginManager.modifyPlugin(propertyMap);
	}

	public boolean removePlugin(String strPluginId) throws Exception {
		return Server.pluginManager.removePlugin(strPluginId);
	}

	public void deletePluginFile(String strPluginId, String file) throws Exception {
		Server.pluginManager.deletePluginFile(strPluginId, file);
	}

	public void reloadPlugin(String strPluginId) throws Exception {
		Server.pluginManager.setClassLastModify(strPluginId, System.currentTimeMillis());
	}

	public HashMap getPluginPropertyById(String id) throws Exception {
		return Server.pluginManager.getPropertyById(id);
	}

	public String[] getDatabase() throws Exception {
		return Server.databaseManager.getDatabase();
	}

	public HashMap getDatabasePropertyByName(String strDatabaseName) throws Exception {
		return Server.databaseManager.getPropertyByName(strDatabaseName);
	}

	public HashMap getTasks() throws Exception {
		return Server.taskManager.getTasks();
	}

	public RemoteTask getTask(String taskID) throws Exception {
		return Server.taskManager.getTask(taskID);
	}

	public Object getT1(String taskID) throws Exception {
		return Server.taskManager.getTask(taskID);
	}

	public HashMap getT2(String taskID) throws Exception {
		HashMap map = new HashMap();
		map.put("1", Server.taskManager.getTask(taskID));
		return map;
	}

	public String getTaskID() throws Exception {
		return Server.taskManager.getTaskID();
	}

	public void addTask(HashMap propertyMap) throws Exception {
		Server.taskManager.addTask(propertyMap);
	}

	public void removeTask(String strTaskID) throws Exception {
		Server.taskManager.removeTask(strTaskID);
	}

	public void modifyTask(HashMap propertyMap) throws Exception {
		Server.taskManager.modifyTask(propertyMap);
	}

	public void verifyTaskCron(String cron) throws Exception {
		/*new CronTrigger("t", "g", "j", "g", new Date(), null, cron);*/
	}

	public void addDatabase(HashMap propertyMap) throws Exception {
		Server.databaseManager.addDatabase(propertyMap);
	}

	public void modifyDatabase(String oldName, HashMap propertyMap) throws Exception {
		Server.databaseManager.modifyDatabase(oldName, propertyMap);
	}

	public void removeDatabase(String strDatabaseName) throws Exception {
		Server.databaseManager.removeDatabase(strDatabaseName);
	}

	public boolean testDatabaseConnection(HashMap propertyMap) throws Exception {
		try {
			new Database((String) propertyMap.get("name"), (String) propertyMap.get("driver"),
					(String) propertyMap.get("url"), (String) propertyMap.get("user"), (String) propertyMap.get("pass"))
							.getConnection().close();
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public String testConnection(HashMap propertyMap) throws Exception {
		Connection conn = null;
		try {
			Class.forName(Convert.toString(propertyMap.get("driver")));

			conn = DriverManager.getConnection(Convert.toString(propertyMap.get("url")),
					Convert.toString(propertyMap.get("user")), Convert.toString(propertyMap.get("pass")));
			return "";
		} catch (Exception e) {
			return "失败:" + e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception localException3) {
				}
		}
	}

	public String getCharset() throws Exception {
		return Util.getCharset();
	}

	public void exit() throws Exception {
		System.exit(0);
	}

	public HashMap getMemoInfo() throws Exception {
		long m = 1024L;
		long nFreeMemory = Runtime.getRuntime().freeMemory() / m;
		long nTotalMemory = Runtime.getRuntime().totalMemory() / m;
		long nUseMemory = nTotalMemory - nFreeMemory;
		HashMap map = new HashMap();
		map.put("total", new Long(nTotalMemory));
		map.put("use", new Long(nUseMemory));
		map.put("free", new Long(nFreeMemory));
		map.put("min", new Long(ServerThread.minMem / m));
		map.put("max", new Long(ServerThread.maxMem / m));
		return map;
	}

	public String[] getCounterNames() throws Exception {
		return Counter.getCounterNames();
	}

	public String[][] getCounters() throws Exception {
		return Counter.getCounters();
	}

	public long getCounterValue(String name) throws Exception {
		return Counter.getCounter(name).getValue();
	}

	public void modifyCounter(String name, long value) throws Exception {
		Counter.getCounter(name).setValue(value);
	}

	public void addCounter(String name, long initValue) throws Exception {
		Counter.z_addCounter(name, initValue);
	}

	public void removeCounter(String name) throws Exception {
		Counter.z_removeCounter(name);
	}

	public void addCategory(String name, String memo, String paramStruct) throws Exception {
		ProfileManager.addCategory(name, memo, paramStruct);
	}

	public void addProfile(String category, String profile, String memo, Map params) throws Exception {
		ProfileManager.addProfile(category, profile, memo, params);
	}

	public boolean removeCaegory(String name) throws Exception {
		return ProfileManager.removeCaegory(name);
	}

	public boolean removeProfile(String category, String profile) throws Exception {
		return ProfileManager.removeProfile(category, profile);
	}

	public String[] getCategoryNames() throws Exception {
		return ProfileManager.getCategoryNames();
	}

	public String[][] getCategories() throws Exception {
		return ProfileManager.getCategories();
	}

	public String[][] getCategoryParamStruct(String category) throws Exception {
		return ProfileManager.getCategoryParamStruct(category);
	}

	public String getCategoryMemo(String category) throws Exception {
		return ProfileManager.getCategoryMemo(category);
	}

	public String[][] getCategoryProfiles(String category) throws Exception {
		return ProfileManager.getCategoryProfiles(category);
	}

	public String[][] getProfile(String category, String profile) throws Exception {
		return ProfileManager.getProfile(category, profile);
	}

	public Map getProfileMap(String category, String profile) throws Exception {
		return ProfileManager.getProfileMap(category, profile);
	}

	public void modifyCategory(String name, String memo, String paramStruct) throws Exception {
		ProfileManager.modifyCategory(name, memo, paramStruct);
	}

	public void modifyProfile(String category, String profile, String memo, Map params) throws Exception {
		ProfileManager.modifyProfile(category, profile, memo, params);
	}

	public boolean renameCategory(String oldname, String newname) throws Exception {
		return ProfileManager.renameCategory(oldname, newname);
	}

	public boolean renameProfile(String category, String oldProfile, String newProfile) throws Exception {
		return ProfileManager.renameProfile(category, oldProfile, newProfile);
	}

	public String getProfileMemo(String category, String profile) throws Exception {
		return ProfileManager.getProfileMemo(category, profile);
	}
}
