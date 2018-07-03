package com.kepler.tcm.client;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AgentServerConfigTest {
	
	
	private AgentConfig agentServerConfig = null ;

	@Before
	public void setUp() throws Exception {
		
		agentServerConfig = new AgentConfig("F:\\workspace\\bonc-git\\kepler-tcm-server","agent.conf");
		
	}

	@Test
	public void testAgentServerConfigString() {
		//agentServerConfig.AgentServerConfig()
	}

	@Test
	public void testAgentServerConfigStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetConfigString() throws Exception {
		agentServerConfig.getConfig("agent.conf");
	}

	@Test
	public void testGetConfigStringBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testAdd() throws Exception {
		agentServerConfig.add("127.10.0.1:1098", "cershi ");
	}

	@Test
	public void testEditStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemove() {
		fail("Not yet implemented");
	}

	@Test
	public void testSize() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexOfName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetValueInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetValueString() {
		fail("Not yet implemented");
	}

	@Test
	public void testReload() {
		fail("Not yet implemented");
	}

}
