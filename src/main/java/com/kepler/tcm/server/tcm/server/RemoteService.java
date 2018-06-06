package com.kepler.tcm.server.tcm.server;

import java.util.HashMap;

public abstract interface RemoteService {
	public abstract String[] getPlugins();

	public abstract HashMap getPluginPropertyByName(String paramString);

	public abstract String[] getDatabase();

	public abstract HashMap getDatabasePropertyByName(String paramString);

	public abstract HashMap getTasks();

	public abstract String getTaskID();

	public abstract void addTask(HashMap paramHashMap);

	public abstract void removeTask(String paramString);

	public abstract void modifyTask(HashMap paramHashMap);

	public abstract void addDatabase(HashMap paramHashMap);

	public abstract void modifyDatabase(String paramString, HashMap paramHashMap);

	public abstract void removeDatabase(String paramString);

	public abstract boolean testDatabaseConnection(HashMap paramHashMap);

	public abstract void exit();
}