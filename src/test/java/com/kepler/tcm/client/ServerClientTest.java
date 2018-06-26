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
		
		//String agentAndServer = "172.16.13.248:1098@server_01";
		
		String agentAndServer = "127.0.0.1:1098@server01";
		
		serverClient = serverClient ==  null ? new ServerClient(agentAndServer): serverClient ;
	}

	@Test
	public void testGetServerMap() throws Exception {
		Map serverMap = serverClient.getServerMap();
		if(serverMap == null ) return;
		Set set = serverMap.keySet();
		
		Iterator it = set.iterator();
		
		while(it.hasNext()){
			System.out.println(serverMap.get(it.next()));
		}
	}

	@Test
	public void testAddServer() throws Exception {
		
		Map map = new HashMap();
		map.put("autoRestart","1");
		map.put("monitorInterval",""+300);
		map.put("serverPort",""+1088);
		map.put("monitorPort",1077);
		map.put("memo", "测试服务09");
		serverClient.addServer("server09", map);
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
	public void testServerStarted() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetServerHost() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetServerPort() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMonitorPort() {
		fail("Not yet implemented");
	}

	@Test
	public void testStartMonitor() {
		fail("Not yet implemented");
	}

	@Test
	public void testStopMonitor() {
		fail("Not yet implemented");
	}

	@Test
	public void testRestartMonitor() {
		fail("Not yet implemented");
	}



	@Test
	public void testSetServerConfig() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetServerName() {
		fail("Not yet implemented");
	}

}
