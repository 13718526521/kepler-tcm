package com.kepler.tcm.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface ServerService{

	Map<String, Object> query(HttpServletRequest request,HttpServletResponse response,String agentName) throws Exception;
	
	Map<String, Object> querystate(HttpServletRequest request,HttpServletResponse response,String agentName) throws Exception;
	
	Map<String, Object> add(HttpServletRequest request,HttpServletResponse response,String agentName,String serverName,String autoRestart,String monitorInterval,String serverPort,String monitorPort,String memo) throws Exception;
	
	Map<String, Object> edit(HttpServletRequest request,HttpServletResponse response,String agentName,String serverName,String autoRestart,String monitorInterval,String serverPort,String monitorPort,String memo) throws Exception;
	
	Map<String, Object> get(HttpServletRequest request,HttpServletResponse response,String agentName,String serverName) throws Exception;
	
	Map<String, Object> start(HttpServletRequest request,HttpServletResponse response,String agentName,String serverName) throws Exception;
	
	Map<String, Object> restart(HttpServletRequest request,HttpServletResponse response,String agentName,String serverName) throws Exception;
	
	Map<String, Object> stop(HttpServletRequest request,HttpServletResponse response,String agentName,String serverName) throws Exception;
	
	boolean started(HttpServletRequest request,HttpServletResponse response,String agentName,String serverName) throws Exception;
	
	Map<String, Object> serverhost(HttpServletRequest request,HttpServletResponse response,String agentName,String serverName) throws Exception;
	
	Map<String, Object> serverport(HttpServletRequest request,HttpServletResponse response,String agentName,String serverName) throws Exception;
	
	Map<String, Object> monitorport(HttpServletRequest request,HttpServletResponse response,String agentName,String serverName) throws Exception;
	
}
