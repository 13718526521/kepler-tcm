package com.kepler.tcm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DataBaseConfigService {
	
	boolean getConnection(String agentAndServer,HashMap map) throws Exception;

	boolean add(String agentAndServer,HashMap map) throws Exception;

	boolean modify(String agentAndServer,String dbId, HashMap map) throws Exception;

	boolean remove(String agentAndServer,String dbId) throws Exception;

	Map pages(String agentAndServer,String name, int pageNum, int pageSize);

}
