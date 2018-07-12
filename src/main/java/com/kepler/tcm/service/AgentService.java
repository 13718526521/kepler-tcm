package com.kepler.tcm.service;

import java.util.Map;

public interface AgentService{
	
	public Map add(String agentName,String memo) throws Exception;
	
	public Map query() throws Exception;
	
	public Map edit(String oldagent,String agentName,String memo) throws Exception;
	
	public Map delete(String agentName) throws Exception;
	
	public Map connect(String agent,String port) throws Exception;

	public Map querystate() throws Exception;
	
}
