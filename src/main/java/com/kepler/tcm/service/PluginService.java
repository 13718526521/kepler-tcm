package com.kepler.tcm.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PluginService {		
	
	Map query(HttpServletRequest request, HttpServletResponse response,String agentAndServer) throws Exception;
	
	Map<String, Object> add(HttpServletRequest request,HttpServletResponse response,String agentAndServer,String id,String pluginName,String pluginMemo,String entryClass) throws Exception;

	Map<String, Object> edit(HttpServletRequest request,HttpServletResponse response,String agentAndServer,String id,String pluginName,String pluginMemo,String entryClass) throws Exception;

	Map<String, Object> delete(HttpServletRequest request,HttpServletResponse response,String agentAndServer,String id) throws Exception;

	Map<String, Object> reload(HttpServletRequest request,HttpServletResponse response,String agentAndServer,String id) throws Exception;

	Map<String, Object> getpropertybyid(HttpServletRequest request,HttpServletResponse response,String agentAndServer,String id) throws Exception;

}
