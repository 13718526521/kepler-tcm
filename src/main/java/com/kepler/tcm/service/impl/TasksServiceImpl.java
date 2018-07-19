package com.kepler.tcm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.sockjs.transport.handler.JsonpPollingTransportHandler;

import com.alibaba.fastjson.JSON;
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

	@Override
	public List findAll(String agentAndServer) throws Exception {
		TaskClient t = new TaskClient(agentAndServer);
		HashMap tasks = t.getTasks();
		
		List list = new ArrayList<>();
		for(Object obj : tasks.keySet()) {
			String taskId =  (String) obj;
			Map map = new HashMap<>();
			map=JSON.parseObject(JSON.toJSONString(tasks.get(taskId)),Map.class);
			if(t.isTaskStarted(taskId)) {
				map.put("state", "0");
			}else {
				map.put("state", "1");
			}
			list.add(map);
		}
		return list;
	}

	@Override
	public boolean isTaskStarted(String agentAndServer, String taskId) {
		TaskClient t = new TaskClient(agentAndServer);
		try {
			return t.isTaskStarted(taskId);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
