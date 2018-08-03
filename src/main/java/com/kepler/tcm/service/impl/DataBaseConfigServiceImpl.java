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
	public String getConnection(String agentAndServer,HashMap map) throws Exception {
		DatabaseClient d = new DatabaseClient(agentAndServer);
		return d.testConnection(map);
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
	public boolean modify(String agentAndServer,String id, HashMap map) throws Exception {
		DatabaseClient d = new DatabaseClient(agentAndServer);
		return d.editDatabase(id, map);
	}

	/**
	 * 删除
	 */
	@Override
	public boolean remove(String agentAndServer,String id) throws Exception {
		DatabaseClient d = new DatabaseClient(agentAndServer);
		return d.removeDatabase(id);
		
	}

	/**
	 * 分页-条件查询
	 */
	@Override
	public Map pages(String agentAndServer,String name){
		
		//定义返回值map
		Map map = new HashMap<>();
		try {
			//pageNum 页码   pageSize 查询个数
			DatabaseClient d = new DatabaseClient(agentAndServer);
			//拉取数据库信息列表  id  String[]
			//List<Database> databases = d.getDatabases();
			String[] database = d.getDatabase();
			if(database == null|| database.length==0) {
				return null;
			}
			List<HashMap> lDatabases = new ArrayList<>();
			
			if(StringUtils.isNoneBlank(name)) {
				for(String id : database) {
					HashMap databaseById = d.getDatabasePropertyById(id);
					if(databaseById.get("name").toString().indexOf(name)>-1) {
						lDatabases.add(databaseById);
						break;
					}
				}
				map.put("data", lDatabases);
				return map;
			}else {
				for(String id : database) {
					HashMap databaseById = d.getDatabasePropertyById(id);
					lDatabases.add(databaseById);
				}
				Collections.reverse(lDatabases);
				map.put("data", lDatabases);
				return map;
			}
			/*//分页
			List<HashMap> databases = new ArrayList<>();
			for(String id : database) {
				databases.add(d.getDatabasePropertyById(id));
			}
			Collections.reverse(databases);
			int length = 0;
			if(pageNum*pageSize+pageSize<=databases.size()) {
				length = pageNum*pageSize+pageSize;
			}else {
				length = databases.size();
			}
			for(int i= pageNum*pageSize; i<length; i++) {
				lDatabases.add(databases.get(i));
			}
			map.put("data", lDatabases);
			return map;*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			map.put("error", e.getMessage());
			return map;
		}
	}

	@Override
	public List findAll(String agentAndServer) {
		DatabaseClient d = new DatabaseClient(agentAndServer);
		String[] database;
		List list = new ArrayList<>();
		try {
			database = d.getDatabase();
			if(database == null|| database.length==0) {
				return null;
			}
			for(String dbId : database) {
				list.add(d.getDatabasePropertyById(dbId));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}

	

}
