package com.kepler.tcm.client;

import org.junit.Before;
import org.junit.Test;

import com.kepler.tcm.core.plugin.PluginManager;
import com.kepler.tcm.core.server.DatabaseManager;
import com.kepler.tcm.core.task.RemoteTaskManager;

public class DatabaseManagerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDatabaseManager() {
		//加载数据库
		DatabaseManager databaseManager = new DatabaseManager();
		databaseManager.load();
	}
	
	@Test
	public void testPluginManager() {
		//加载插件
		PluginManager pluginManager = new PluginManager();
		pluginManager.load();
	}
	
	

	
	@Test
	public void testTaskManager() {
		//加载任务
		RemoteTaskManager taskManager = new RemoteTaskManager();
		taskManager.load();
	}

}
