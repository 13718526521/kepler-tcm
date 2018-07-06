package com.kepler.tcm.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AgentService{
	
	public Map add(HttpServletRequest request, HttpServletResponse response,String agentName,String memo) throws Exception;
	
	public Map query(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public Map edit(HttpServletRequest request, HttpServletResponse response,String oldagent,String agentName,String memo) throws Exception;
	
	public Map delete(HttpServletRequest request, HttpServletResponse response,String agentName) throws Exception;
	
	public Map connect(HttpServletRequest request, HttpServletResponse response,String agent,String port) throws Exception;

	public Map querystate(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
}
