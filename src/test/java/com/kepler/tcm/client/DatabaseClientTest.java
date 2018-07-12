package com.kepler.tcm.client;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class DatabaseClientTest {

	DatabaseClient databaseClient = null ;
	
	@Before
	public void setUp() throws Exception {
		
		String agentAndServer = "127.0.0.1:1098@server01";
		databaseClient = databaseClient == null ? 
				new DatabaseClient(agentAndServer) : databaseClient;
	}

	@Test
	public void testDatabaseClient() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDatabase() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddDatabase() throws Exception {
		//{id : "id",name ： "" ,driver : "" , url : "",user : "",pass:""}

		HashMap map = new HashMap();
		map.put("id", "1001");
		map.put("name", "testDatabase01");
		map.put("driver", "com.mysql.jdbc.Driver");
		map.put("url", "jdbc:mysql://172.16.3.49:31318/kepler_core");
		map.put("user", "root");
		map.put("pass", "hyyyzx");
		System.out.println(databaseClient.addDatabase(map));
	}

	@Test
	public void testEditDatabase() throws Exception {
		HashMap map = new HashMap();
		map.put("name", "te222222");
		map.put("driver", "com.mysql.jdbc.Driver");
		map.put("url", "jdbc:mysql://172.16.3.49:31318/kepler_core");
		map.put("user", "root");
		map.put("pass", "Admin789456123");
		System.out.println(databaseClient.editDatabase("1001",map));
	}

	@Test
	public void testRemoveDatabase() throws Exception {
		System.out.println(databaseClient.removeDatabase("1001"));
	}

	@Test
	public void testTestConnection() throws Exception {
		HashMap map = new HashMap();
		map.put("driver", "com.mysql.jdbc.Driver");
		map.put("url", "jdbc:mysql://172.16.3.49:31318/kepler_core");
		map.put("user", "root");
		map.put("pass", "hyyyzx");
		System.out.println(databaseClient.testConnection(map));
	}

	@Test
	public void testTestDatabaseConnection() throws Exception {
		HashMap map = new HashMap();
		map.put("driver", "com.mysql.jdbc.Driver");
		map.put("url", "jdbc:mysql://172.16.3.49:31318/kepler_core");
		map.put("user", "root");
		map.put("pass", "hyyyzx");
		System.out.println(databaseClient.testDatabaseConnection(map));
	}

	@Test
	public void testGetDatabasePropertyById() throws Exception {
		HashMap<String , String > map  = databaseClient.getDatabasePropertyById("1001");
		if(map == null) {
			
			System.out.println("没获取到信息") ;
			return;
		}
 		for (Map.Entry<String , String > entry : map.entrySet()) {
			//Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
			//entry.getKey() ;entry.getValue(); entry.setValue();
			//map.entrySet()  返回此映射中包含的映射关系的 Set视图。
			System.out.println("key= " + entry.getKey() + " and value= "+ entry.getValue());
		}
	}

}
