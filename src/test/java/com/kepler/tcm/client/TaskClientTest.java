package com.kepler.tcm.client;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.kepler.tcm.core.task.RemoteTask;

public class TaskClientTest {
	
	TaskClient taskClient = null ;

	@Before
	public void setUp() throws Exception {
		String agentAndServer = "127.0.0.1:1098@server01";
		taskClient = taskClient == null ? new TaskClient(agentAndServer) : taskClient;
	}

	@Test
	public void testGetTasks() throws Exception {
		HashMap tasks = taskClient.getTasks();
		
		Iterator it = tasks.keySet().iterator();
		while(it.hasNext()){
			String key = (String )it.next();
			System.out.println(key+":"+tasks.get(key));
		}
	}

	@Test
	public void testAddTask() throws Exception {
		HashMap map = new HashMap();
		map.put("taskName", "test");
		map.put("alertType", "0");
		map.put("cron", "");
		map.put("databaseId", "");
		map.put("day", "");
		map.put("hour", "");
		map.put("hour4", "");
		map.put("keepAlertTime", "10");
		map.put("logBackNums", "5");
		map.put("logLevel", "DEBUG");
		map.put("logLevel", "ERROR");
		map.put("logMaxSize", "2");
		map.put("logType", "2");
		map.put("minute", "");
		map.put("minute4", "");
		map.put("month", "");
		map.put("mxf", "0.5");
		map.put("mxt", "");
		map.put("notSuccTime", "10");
		map.put("planType", "3");
		map.put("pluginId", "10001");
		map.put("second", "3");
		map.put("second4", "3");
		map.put("databaseId", "1001");
		map.put("standbyDatabaseId", "1002");
		map.put("taskAlert", "1");
		map.put("taskTimeout", "0");
		map.put("year", "3");
		
		taskClient.addTask(map);
	}

	@Test
	public void testGetTask() throws Exception {
		RemoteTask map = taskClient.getTask("00000001");
		System.out.println(map.isStarted());
	}

	@Test
	public void testRemoveTask() throws Exception {
		taskClient.removeTask("00000001");
	}

	@Test
	public void testEditTask() throws Exception {
		HashMap map = new HashMap();
		
		map.put("taskID", "00000001"); //必填，否则错误
		map.put("taskName", "test edit ");
		map.put("alertType", "0");
		map.put("cron", "");
		map.put("databaseId", "");
		map.put("day", "");
		map.put("hour", "");
		map.put("hour4", "");
		map.put("keepAlertTime", "10");
		map.put("logBackNums", "5");
		map.put("logLevel", "DEBUG");
		map.put("logLevel", "ERROR");
		map.put("logMaxSize", "2");
		map.put("logType", "2");
		map.put("minute", "");
		map.put("minute4", "");
		map.put("month", "");
		map.put("mxf", "0.5");
		map.put("mxt", "");
		map.put("notSuccTime", "10");
		map.put("planType", "3");
		map.put("pluginId", "10001");
		map.put("second", "3");
		map.put("second4", "3");
		map.put("databaseId", "1001");
		map.put("standbyDatabaseId", "1002");
		map.put("taskAlert", "1");
		map.put("taskTimeout", "0");
		map.put("year", "3");
		
		taskClient.editTask(map);
	}
	
	@Test
	public void testStartTask() throws Exception {
		taskClient.startTask("00000001");	
	}
	
	@Test
	public void testStopTask() throws Exception {
		taskClient.stopTask("00000001");	
	}

}
