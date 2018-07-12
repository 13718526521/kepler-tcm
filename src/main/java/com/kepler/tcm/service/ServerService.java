package com.kepler.tcm.service;

import java.util.Map;


public interface ServerService{

	Map<String, Object> query(String agentName) throws Exception;
	
	Map<String, Object> querystate(String agentName) throws Exception;
	
	Map<String, Object> add(String agentName,String serverName,String autoRestart,String monitorInterval,String serverPort,String monitorPort,String memo) throws Exception;
	
	Map<String, Object> edit(String agentName,String serverName,String autoRestart,String monitorInterval,String serverPort,String monitorPort,String memo) throws Exception;
	
	Map<String, Object> get(String agentName,String serverName) throws Exception;
	
	Map<String, Object> start(String agentName,String serverName) throws Exception;
	
	Map<String, Object> restart(String agentName,String serverName) throws Exception;
	
	Map<String, Object> stop(String agentName,String serverName) throws Exception;
	
	boolean started(String agentName,String serverName) throws Exception;
	
	Map<String, Object> serverhost(String agentName,String serverName) throws Exception;
	
	Map<String, Object> serverport(String agentName,String serverName) throws Exception;
	
	Map<String, Object> monitorport(String agentName,String serverName) throws Exception;
	
}
