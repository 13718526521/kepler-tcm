package com.kepler.tcm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TasksService {

	void add(String agentAndServer, HashMap map) throws Exception;

	void editTask(String agentAndServer, HashMap map) throws Exception;

	void remove(String agentAndServer, String taskId) throws Exception;

	Map pages(String agentAndServer, int pageNum, int pageSize);

	void startTask(String agentAndServer,String taskId) throws Exception;

	void stopTask(String agentAndServer, String taskId) throws Exception;

	List findAll(String agentAndServer) throws Exception;

	boolean isTaskStarted(String agentAndServer, String taskId);
	
}
