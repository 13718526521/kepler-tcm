package com.kepler.tcm.client;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.kepler.tcm.core.server.RemoteServer;

public class ServerClientTest {
	
	ServerClient serverClient  = null ;

	@Before
	public void setUp() throws Exception {
		
		String agentAndServer = "127.0.0.1:1098";
		
		//String agentAndServer = "127.0.0.1:1098";
		
		serverClient = serverClient ==  null ? new ServerClient(agentAndServer): serverClient ;
	}

	@Test
	public void testGetServerMap() throws Exception {
		Map serverMap = serverClient.getServerMap();
		if(serverMap == null ) return;
		Set set = serverMap.keySet();
		
		Iterator it = set.iterator();
		 
		while(it.hasNext()){
			String key = (String )it.next();
			System.out.println(key + ":"+ serverMap.get(key));
		}
	}

	@Test
	public void testAddServer() throws Exception {
		
		Map map = new HashMap();
		map.put("autoRestart","1");
		map.put("monitorInterval",""+300);
		map.put("serverPort",""+1044);
		map.put("monitorPort",1055);
		map.put("memo", "测试服务01");
		serverClient.addServer("server01", map);
	}

	
	@Test
	public void testGetServerConfig() throws Exception {
		Map serverInfo = serverClient.getServerConfig("server01", false);
		Iterator it = serverInfo.keySet().iterator();
		while(it.hasNext()){
			Object val = it.next();
			System.out.println(val+":"+serverInfo.get(val));
		}
	}
	
	@Test
	public void testGetServer() throws Exception {
		RemoteServer remoteServer = serverClient.getServer("server01");
		System.out.println(remoteServer.getConnectRMI());
	}

	@Test
	public void testStartServer() throws Exception {
		serverClient.startServer("server01");
	}

	@Test
	public void testRestartServer() throws Exception {
		serverClient.restartServer("server01",false);
	}

	@Test
	public void testStopServer() throws Exception {
		serverClient.stopServer("server01");
	}

	@Test
	public void testServerStarted() throws Exception {
		System.out.println(serverClient.serverStarted("server01"));
	}

	@Test
	public void testGetServerHost() throws Exception {
		System.out.println(serverClient.getServerHost("server01"));
	}

	@Test
	public void testGetServerPort() throws Exception {
		System.out.println(serverClient.getServerPort("server01"));
	}

	@Test
	public void testGetMonitorPort() throws Exception {
		System.out.println(serverClient.getMonitorPort("server01"));
	}

	@Test
	public void testStartMonitor() throws Exception {
		serverClient.startMonitor("server01");
	}

	@Test
	public void testStopMonitor() throws Exception {
		serverClient.stopMonitor("server01");
	}

	@Test
	public void testRestartMonitor() throws Exception {
		serverClient.restartMonitor("server01");
	}



	@Test
	public void testSetServerConfig() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetServerName() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetMemoInfo() throws Exception {
		 HashMap memoInfoMap = serverClient.getMemoInfo("server01");
		 
		 Iterator it = memoInfoMap.keySet().iterator();
		 
		 while(it.hasNext()){
			  String key = (String )it.next();
			 System.out.println(key +":"+ memoInfoMap.get(key));
		 }
	}
	
	

}
