package com.kepler.tcm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DataBaseConfigService {
	
	String getConnection(String agentAndServer,HashMap map) throws Exception;

	boolean add(String agentAndServer,HashMap map) throws Exception;

	boolean modify(String agentAndServer,String id, HashMap map) throws Exception;

	boolean remove(String agentAndServer,String id) throws Exception;

	Map pages(String agentAndServer,String name);

	List findAll(String agentAndServer);

}
