package com.kepler.tcm.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.sockjs.transport.handler.JsonpPollingTransportHandler;

import com.alibaba.fastjson.JSON;
import com.kepler.tcm.client.TaskClient;
import com.kepler.tcm.core.server.Server;
import com.kepler.tcm.core.task.RemoteTask;
import com.kepler.tcm.core.util.Convert;
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
	public Map pages(String agentAndServer, int pageNum, int pageSize) {
		TaskClient t = new TaskClient(agentAndServer);
		Map map = new HashMap<>();
		try {
			HashMap tasks = t.getTasks();
			if(tasks == null || tasks.size() == 0) {
				return null;
			}
			List<Map> list = new ArrayList<>();
			List<Map> lists = new ArrayList<>();
			for(Object obj : tasks.keySet()) {
				String taskId =  (String) obj;
				Map map1 = new HashMap<>();
				map1=JSON.parseObject(JSON.toJSONString(tasks.get(taskId)),Map.class);
				if(t.isTaskStarted(taskId)) {
					map1.put("state", "0");
				}else {
					map1.put("state", "1");
				}
				list.add(map1);
			}
			Collections.reverse(list);
			int length = 0;
			if(pageNum*pageSize+pageSize<=list.size()) {
				length = pageNum*pageSize+pageSize;
			}else {
				length = list.size();
			}
			for(int i= pageNum*pageSize; i<length; i++) {
				lists.add(list.get(i));
			}
			map.put("data", lists);
		} catch (Exception e) {
			map.put("error", e.getMessage());
			return map;
		}
		return map;
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

	@Override
	public RemoteTask getTask(String agentAndServer, String taskId) throws Exception {
		TaskClient t = new TaskClient(agentAndServer);
		RemoteTask task = t.getTask(taskId);
		return task;
	}

	@Override
	public List getTaskLog(String agentAndServer,String type, String taskId, int pageSize, int pageNum) throws Exception {
		String nameLog = null;
		if("0".equals(type)) {
			nameLog = Server.TASK_LOG_NAME1;
		}else {
			nameLog = Server.TASK_LOG_NAME2;
		}
		TaskClient t = new TaskClient(agentAndServer);
		int totalSize = (int) t.getTotalLogSize(taskId,nameLog);
		int pageCount = (totalSize - 1) / (pageSize * 1024) + 1;
		if (pageNum == -2 || pageNum > pageCount)
			pageNum = pageCount;
		else if (pageNum < 1) pageNum = 1;
		
		String taskLog = t.getTaskLog(taskId, nameLog, pageNum, pageSize * 1024);
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
