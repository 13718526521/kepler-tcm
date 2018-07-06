package com.kepler.tcm.service.impl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kepler.tcm.client.TaskClient;
import com.kepler.tcm.service.TasksService;
@Service
public class TasksServiceImpl implements TasksService{

	private final Logger log = LoggerFactory.getLogger(TasksServiceImpl.class);

	@Override
	public void add(String agentAndServer, HashMap map) throws Exception {
		TaskClient t = new TaskClient(agentAndServer);
		t.addTask(map);
	}

	@Override
	public void editTask(String agentAndServer, HashMap map) throws Exception {
		TaskClient t = new TaskClient(agentAndServer);
		t.editTask(map);
	}

	@Override
	public void remove(String agentAndServer, String taskId) throws Exception {
		TaskClient t = new TaskClient(agentAndServer);
		t.removeTask(taskId);
	}

	@Override
	public HashMap pages(String agentAndServer, String name, int pageNum, int pageSize) {
		
		
		return null;
	}

	@Override
	public void startTask(String agentAndServer,String taskId) throws Exception {
		TaskClient t = new TaskClient(agentAndServer);
		t.startTask(taskId);
	}

	@Override
	public void stopTask(String agentAndServer, String taskId) throws Exception {
		TaskClient t = new TaskClient(agentAndServer);
		t.stopTask(taskId);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
