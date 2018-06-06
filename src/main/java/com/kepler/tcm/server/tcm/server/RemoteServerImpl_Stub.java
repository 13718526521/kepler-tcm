package com.kepler.tcm.server.tcm.server;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;
import java.util.HashMap;
import java.util.Map;

public final class RemoteServerImpl_Stub extends RemoteStub implements RemoteServer, Remote {
	private static final long serialVersionUID = 2L;
	private static Method $method_addCategory_0;
	private static Method $method_addCounter_1;
	private static Method $method_addDatabase_2;
	private static Method $method_addPlugin_3;
	private static Method $method_addProfile_4;
	private static Method $method_addTask_5;
	private static Method $method_deletePluginFile_6;
	private static Method $method_exit_7;
	private static Method $method_getCategories_8;
	private static Method $method_getCategoryMemo_9;
	private static Method $method_getCategoryNames_10;
	private static Method $method_getCategoryParamStruct_11;
	private static Method $method_getCategoryProfiles_12;
	private static Method $method_getCharset_13;
	private static Method $method_getCounterNames_14;
	private static Method $method_getCounterValue_15;
	private static Method $method_getCounters_16;
	private static Method $method_getDatabase_17;
	private static Method $method_getDatabasePropertyByName_18;
	private static Method $method_getMemoInfo_19;
	private static Method $method_getPluginPropertyById_20;
	private static Method $method_getPlugins_21;
	private static Method $method_getProfile_22;
	private static Method $method_getProfileMap_23;
	private static Method $method_getProfileMemo_24;
	private static Method $method_getServerName_25;
	private static Method $method_getT1_26;
	private static Method $method_getT2_27;
	private static Method $method_getTask_28;
	private static Method $method_getTaskID_29;
	private static Method $method_getTasks_30;
	private static Method $method_modifyCategory_31;
	private static Method $method_modifyCounter_32;
	private static Method $method_modifyDatabase_33;
	private static Method $method_modifyPlugin_34;
	private static Method $method_modifyProfile_35;
	private static Method $method_modifyTask_36;
	private static Method $method_reloadPlugin_37;
	private static Method $method_removeCaegory_38;
	private static Method $method_removeCounter_39;
	private static Method $method_removeDatabase_40;
	private static Method $method_removePlugin_41;
	private static Method $method_removeProfile_42;
	private static Method $method_removeTask_43;
	private static Method $method_renameCategory_44;
	private static Method $method_renameProfile_45;
	private static Method $method_testConnection_46;
	private static Method $method_testDatabaseConnection_47;
	private static Method $method_verifyTaskCron_48;

	static {
		try {
			$method_addCategory_0 = Remote.class.getMethod("addCategory",
					new Class[] { String.class, String.class, String.class });
			$method_addCounter_1 = Remote.class.getMethod("addCounter", new Class[] { String.class, Long.TYPE });
			$method_addDatabase_2 = Remote.class.getMethod("addDatabase", new Class[] { HashMap.class });
			$method_addPlugin_3 = Remote.class.getMethod("addPlugin", new Class[] { HashMap.class });
			$method_addProfile_4 = Remote.class.getMethod("addProfile",
					new Class[] { String.class, String.class, String.class, Map.class });
			$method_addTask_5 = Remote.class.getMethod("addTask", new Class[] { HashMap.class });
			$method_deletePluginFile_6 = Remote.class.getMethod("deletePluginFile",
					new Class[] { String.class, String.class });
			$method_exit_7 = Remote.class.getMethod("exit", new Class[0]);
			$method_getCategories_8 = Remote.class.getMethod("getCategories", new Class[0]);
			$method_getCategoryMemo_9 = Remote.class.getMethod("getCategoryMemo", new Class[] { String.class });
			$method_getCategoryNames_10 = Remote.class.getMethod("getCategoryNames", new Class[0]);
			$method_getCategoryParamStruct_11 = Remote.class.getMethod("getCategoryParamStruct",
					new Class[] { String.class });
			$method_getCategoryProfiles_12 = Remote.class.getMethod("getCategoryProfiles",
					new Class[] { String.class });
			$method_getCharset_13 = Remote.class.getMethod("getCharset", new Class[0]);
			$method_getCounterNames_14 = Remote.class.getMethod("getCounterNames", new Class[0]);
			$method_getCounterValue_15 = Remote.class.getMethod("getCounterValue", new Class[] { String.class });
			$method_getCounters_16 = Remote.class.getMethod("getCounters", new Class[0]);
			$method_getDatabase_17 = Remote.class.getMethod("getDatabase", new Class[0]);
			$method_getDatabasePropertyByName_18 = Remote.class.getMethod("getDatabasePropertyByName",
					new Class[] { String.class });
			$method_getMemoInfo_19 = Remote.class.getMethod("getMemoInfo", new Class[0]);
			$method_getPluginPropertyById_20 = Remote.class.getMethod("getPluginPropertyById",
					new Class[] { String.class });
			$method_getPlugins_21 = Remote.class.getMethod("getPlugins", new Class[0]);
			$method_getProfile_22 = Remote.class.getMethod("getProfile", new Class[] { String.class, String.class });
			$method_getProfileMap_23 = Remote.class.getMethod("getProfileMap",
					new Class[] { String.class, String.class });
			$method_getProfileMemo_24 = Remote.class.getMethod("getProfileMemo",
					new Class[] { String.class, String.class });
			$method_getServerName_25 = Remote.class.getMethod("getServerName", new Class[0]);
			$method_getT1_26 = Remote.class.getMethod("getT1", new Class[] { String.class });
			$method_getT2_27 = Remote.class.getMethod("getT2", new Class[] { String.class });
			$method_getTask_28 = Remote.class.getMethod("getTask", new Class[] { String.class });
			$method_getTaskID_29 = Remote.class.getMethod("getTaskID", new Class[0]);
			$method_getTasks_30 = Remote.class.getMethod("getTasks", new Class[0]);
			$method_modifyCategory_31 = Remote.class.getMethod("modifyCategory",
					new Class[] { String.class, String.class, String.class });
			$method_modifyCounter_32 = Remote.class.getMethod("modifyCounter", new Class[] { String.class, Long.TYPE });
			$method_modifyDatabase_33 = Remote.class.getMethod("modifyDatabase",
					new Class[] { String.class, HashMap.class });
			$method_modifyPlugin_34 = Remote.class.getMethod("modifyPlugin", new Class[] { HashMap.class });
			$method_modifyProfile_35 = Remote.class.getMethod("modifyProfile",
					new Class[] { String.class, String.class, String.class, Map.class });
			$method_modifyTask_36 = Remote.class.getMethod("modifyTask", new Class[] { HashMap.class });
			$method_reloadPlugin_37 = Remote.class.getMethod("reloadPlugin", new Class[] { String.class });
			$method_removeCaegory_38 = Remote.class.getMethod("removeCaegory", new Class[] { String.class });
			$method_removeCounter_39 = Remote.class.getMethod("removeCounter", new Class[] { String.class });
			$method_removeDatabase_40 = Remote.class.getMethod("removeDatabase", new Class[] { String.class });
			$method_removePlugin_41 = Remote.class.getMethod("removePlugin", new Class[] { String.class });
			$method_removeProfile_42 = Remote.class.getMethod("removeProfile",
					new Class[] { String.class, String.class });
			$method_removeTask_43 = Remote.class.getMethod("removeTask", new Class[] { String.class });
			$method_renameCategory_44 = Remote.class.getMethod("renameCategory",
					new Class[] { String.class, String.class });
			$method_renameProfile_45 = Remote.class.getMethod("renameProfile",
					new Class[] { String.class, String.class, String.class });
			$method_testConnection_46 = Remote.class.getMethod("testConnection", new Class[] { HashMap.class });
			$method_testDatabaseConnection_47 = Remote.class.getMethod("testDatabaseConnection",
					new Class[] { HashMap.class });
			$method_verifyTaskCron_48 = Remote.class.getMethod("verifyTaskCron", new Class[] { String.class });
		} catch (NoSuchMethodException localNoSuchMethodException) {
			throw new NoSuchMethodError("stub class initialization failed");
		}
	}

	public RemoteServerImpl_Stub(RemoteRef paramRemoteRef) {
		super(paramRemoteRef);
	}

	public void addCategory(String paramString1, String paramString2, String paramString3) throws Exception {
		this.ref.invoke(this, $method_addCategory_0, new Object[] { paramString1, paramString2, paramString3 },
				6012660408956169697L);
	}

	public void addCounter(String paramString, long paramLong) throws Exception {
		this.ref.invoke(this, $method_addCounter_1, new Object[] { paramString, new Long(paramLong) },
				9049277982669049861L);
	}

	public void addDatabase(HashMap paramHashMap) throws Exception {
		this.ref.invoke(this, $method_addDatabase_2, new Object[] { paramHashMap }, 1186370965205888791L);
	}

	public boolean addPlugin(HashMap paramHashMap) throws Exception {
		Object localObject = this.ref.invoke(this, $method_addPlugin_3, new Object[] { paramHashMap },
				-1333367180492662172L);
		return ((Boolean) localObject).booleanValue();
	}

	public void addProfile(String paramString1, String paramString2, String paramString3, Map paramMap)
			throws Exception {
		this.ref.invoke(this, $method_addProfile_4, new Object[] { paramString1, paramString2, paramString3, paramMap },
				452473037835380596L);
	}

	public void addTask(HashMap paramHashMap) throws Exception {
		this.ref.invoke(this, $method_addTask_5, new Object[] { paramHashMap }, 5607804359229055303L);
	}

	public void deletePluginFile(String paramString1, String paramString2) throws Exception {
		this.ref.invoke(this, $method_deletePluginFile_6, new Object[] { paramString1, paramString2 },
				893516057979348131L);
	}

	public void exit() throws Exception {
		this.ref.invoke(this, $method_exit_7, null, -6307240473358936408L);
	}

	public String[][] getCategories() throws Exception {
		Object localObject = this.ref.invoke(this, $method_getCategories_8, null, -3445315085453000059L);
		return (String[][]) localObject;
	}

	public String getCategoryMemo(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_getCategoryMemo_9, new Object[] { paramString },
				3205405948662520115L);
		return (String) localObject;
	}

	public String[] getCategoryNames() throws Exception {
		Object localObject = this.ref.invoke(this, $method_getCategoryNames_10, null, -4721097493578354250L);
		return (String[]) localObject;
	}

	public String[][] getCategoryParamStruct(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_getCategoryParamStruct_11, new Object[] { paramString },
				3870729922650678621L);
		return (String[][]) localObject;
	}

	public String[][] getCategoryProfiles(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_getCategoryProfiles_12, new Object[] { paramString },
				-4154458539464598849L);
		return (String[][]) localObject;
	}

	public String getCharset() throws Exception {
		Object localObject = this.ref.invoke(this, $method_getCharset_13, null, -2893021585677417125L);
		return (String) localObject;
	}

	public String[] getCounterNames() throws Exception {
		Object localObject = this.ref.invoke(this, $method_getCounterNames_14, null, -468235043098233021L);
		return (String[]) localObject;
	}

	public long getCounterValue(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_getCounterValue_15, new Object[] { paramString },
				4745773516786260632L);
		return ((Long) localObject).longValue();
	}

	public String[][] getCounters() throws Exception {
		Object localObject = this.ref.invoke(this, $method_getCounters_16, null, -3199285746080349265L);
		return (String[][]) localObject;
	}

	public String[] getDatabase() throws Exception {
		Object localObject = this.ref.invoke(this, $method_getDatabase_17, null, -5174917944849204641L);
		return (String[]) localObject;
	}

	public HashMap getDatabasePropertyByName(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_getDatabasePropertyByName_18, new Object[] { paramString },
				-6894844510218884756L);
		return (HashMap) localObject;
	}

	public HashMap getMemoInfo() throws Exception {
		Object localObject = this.ref.invoke(this, $method_getMemoInfo_19, null, -7904822573509671842L);
		return (HashMap) localObject;
	}

	public HashMap getPluginPropertyById(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_getPluginPropertyById_20, new Object[] { paramString },
				-660930929378248809L);
		return (HashMap) localObject;
	}

	public String[][] getPlugins() throws Exception {
		Object localObject = this.ref.invoke(this, $method_getPlugins_21, null, -4118666747277562274L);
		return (String[][]) localObject;
	}

	public String[][] getProfile(String paramString1, String paramString2) throws Exception {
		Object localObject = this.ref.invoke(this, $method_getProfile_22, new Object[] { paramString1, paramString2 },
				-6096224377681923213L);
		return (String[][]) localObject;
	}

	public Map getProfileMap(String paramString1, String paramString2) throws Exception {
		Object localObject = this.ref.invoke(this, $method_getProfileMap_23,
				new Object[] { paramString1, paramString2 }, 1967941889944444232L);
		return (Map) localObject;
	}

	public String getProfileMemo(String paramString1, String paramString2) throws Exception {
		Object localObject = this.ref.invoke(this, $method_getProfileMemo_24,
				new Object[] { paramString1, paramString2 }, -190586823122906742L);
		return (String) localObject;
	}

	public String getServerName() throws Exception {
		Object localObject = this.ref.invoke(this, $method_getServerName_25, null, -8860807504110107708L);
		return (String) localObject;
	}

	public Object getT1(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_getT1_26, new Object[] { paramString },
				-327741126455313671L);
		return localObject;
	}

	public HashMap getT2(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_getT2_27, new Object[] { paramString },
				6341095419195825798L);
		return (HashMap) localObject;
	}

	public RemoteTask getTask(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_getTask_28, new Object[] { paramString },
				-4658104436280498833L);
		return (RemoteTask) localObject;
	}

	public String getTaskID() throws Exception {
		Object localObject = this.ref.invoke(this, $method_getTaskID_29, null, 3995362375840528697L);
		return (String) localObject;
	}

	public HashMap getTasks() throws Exception {
		Object localObject = this.ref.invoke(this, $method_getTasks_30, null, -9057754621158270004L);
		return (HashMap) localObject;
	}

	public void modifyCategory(String paramString1, String paramString2, String paramString3) throws Exception {
		this.ref.invoke(this, $method_modifyCategory_31, new Object[] { paramString1, paramString2, paramString3 },
				2154488431181056401L);
	}

	public void modifyCounter(String paramString, long paramLong) throws Exception {
		this.ref.invoke(this, $method_modifyCounter_32, new Object[] { paramString, new Long(paramLong) },
				-5994462891018972335L);
	}

	public void modifyDatabase(String paramString, HashMap paramHashMap) throws Exception {
		this.ref.invoke(this, $method_modifyDatabase_33, new Object[] { paramString, paramHashMap },
				1631527871471928400L);
	}

	public boolean modifyPlugin(HashMap paramHashMap) throws Exception {
		Object localObject = this.ref.invoke(this, $method_modifyPlugin_34, new Object[] { paramHashMap },
				2390069716353541538L);
		return ((Boolean) localObject).booleanValue();
	}

	public void modifyProfile(String paramString1, String paramString2, String paramString3, Map paramMap)
			throws Exception {
		this.ref.invoke(this, $method_modifyProfile_35,
				new Object[] { paramString1, paramString2, paramString3, paramMap }, -246378760512653964L);
	}

	public void modifyTask(HashMap paramHashMap) throws Exception {
		this.ref.invoke(this, $method_modifyTask_36, new Object[] { paramHashMap }, 5015444221283188839L);
	}

	public void reloadPlugin(String paramString) throws Exception {
		this.ref.invoke(this, $method_reloadPlugin_37, new Object[] { paramString }, 4948637763793885620L);
	}

	public boolean removeCaegory(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_removeCaegory_38, new Object[] { paramString },
				-1407438606699272364L);
		return ((Boolean) localObject).booleanValue();
	}

	public void removeCounter(String paramString) throws Exception {
		this.ref.invoke(this, $method_removeCounter_39, new Object[] { paramString }, 3656669777200381449L);
	}

	public void removeDatabase(String paramString) throws Exception {
		this.ref.invoke(this, $method_removeDatabase_40, new Object[] { paramString }, 5682865806734087546L);
	}

	public boolean removePlugin(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_removePlugin_41, new Object[] { paramString },
				-8009704788976555713L);
		return ((Boolean) localObject).booleanValue();
	}

	public boolean removeProfile(String paramString1, String paramString2) throws Exception {
		Object localObject = this.ref.invoke(this, $method_removeProfile_42,
				new Object[] { paramString1, paramString2 }, 9064027793467530023L);
		return ((Boolean) localObject).booleanValue();
	}

	public void removeTask(String paramString) throws Exception {
		this.ref.invoke(this, $method_removeTask_43, new Object[] { paramString }, 5948053330821666120L);
	}

	public boolean renameCategory(String paramString1, String paramString2) throws Exception {
		Object localObject = this.ref.invoke(this, $method_renameCategory_44,
				new Object[] { paramString1, paramString2 }, 2689949907206340799L);
		return ((Boolean) localObject).booleanValue();
	}

	public boolean renameProfile(String paramString1, String paramString2, String paramString3) throws Exception {
		Object localObject = this.ref.invoke(this, $method_renameProfile_45,
				new Object[] { paramString1, paramString2, paramString3 }, -2803314375837720303L);
		return ((Boolean) localObject).booleanValue();
	}

	public String testConnection(HashMap paramHashMap) throws Exception {
		Object localObject = this.ref.invoke(this, $method_testConnection_46, new Object[] { paramHashMap },
				-804185822063825709L);
		return (String) localObject;
	}

	public boolean testDatabaseConnection(HashMap paramHashMap) throws Exception {
		Object localObject = this.ref.invoke(this, $method_testDatabaseConnection_47, new Object[] { paramHashMap },
				-4958794707231729003L);
		return ((Boolean) localObject).booleanValue();
	}

	public void verifyTaskCron(String paramString) throws Exception {
		this.ref.invoke(this, $method_verifyTaskCron_48, new Object[] { paramString }, -6449806871607840585L);
	}
}