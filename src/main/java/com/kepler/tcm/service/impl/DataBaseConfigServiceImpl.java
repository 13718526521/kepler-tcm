package com.kepler.tcm.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kepler.tcm.client.DatabaseClient;
import com.kepler.tcm.core.server.Database;
import com.kepler.tcm.service.DataBaseConfigService;

@Service
public class DataBaseConfigServiceImpl implements DataBaseConfigService{

	private final Logger log = LoggerFactory.getLogger(DataBaseConfigServiceImpl.class);

	/**
	 * 测试连接
	 */
	@Override
	public boolean getConnection(String agentAndServer,HashMap map) throws Exception {
		DatabaseClient d = new DatabaseClient(agentAndServer);
		return d.testDatabaseConnection(map);
	}
	
	/**
	 * 添加
	 */
	@Override
	public boolean add(String agentAndServer,HashMap map) throws Exception{
		DatabaseClient d = new DatabaseClient(agentAndServer);
		return d.addDatabase(map);
	}

	/**
	 * 修改
	 */
	@Override
	public boolean modify(String agentAndServer,String dbId, HashMap map) throws Exception {
		DatabaseClient d = new DatabaseClient(agentAndServer);
		return d.editDatabase(dbId, map);
	}

	/**
	 * 删除
	 */
	@Override
	public boolean remove(String agentAndServer,String dbId) throws Exception {
		DatabaseClient d = new DatabaseClient(agentAndServer);
		return d.removeDatabase(dbId);
		
	}

	@Override
	public Map pages(String agentAndServer, String name, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 分页-条件查询
	 */
	/*@Override
	public Map pages(String agentAndServer,String name, int pageNum, int pageSize){
		
		//定义返回值map
		Map map = new HashMap<>();
		try {
			//pageNum 页码   pageSize 查询个数
			DatabaseClient d = new DatabaseClient(agentAndServer);
			//拉取数据库信息列表数据
			List<Database> databases = d.getDatabases();
			if(databases == null|| databases.size()==0) {
				return null;
			}
			Collections.reverse(databases);
			List<Database> lDatabases = new ArrayList<>();
			
			if(StringUtils.isNoneBlank(name)) {
				for(Database database : databases) {
					if(database.getDbName().equals(name)) {
						lDatabases.add(database);
						break;
					}
				}
				map.put("data", lDatabases);
				return map;
			}
			//分页
			for(int i= (pageNum-1)*pageSize; i<(pageNum-1)*pageSize+pageSize; i++) {
				lDatabases.add(databases.get(i));
			}
			map.put("data", lDatabases);
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			map.put("error", e.getMessage());
			return map;
		}
	}*/

	

}
