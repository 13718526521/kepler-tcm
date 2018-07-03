package com.kepler.tcm.client;

import org.junit.Before;
import org.junit.Test;

import com.kepler.tcm.core.agent.RemoteAgent;

public class AgentClientTest {
	
	private AgentClient agentClient = null ;
	
	@Before
	public void setUp() throws Exception {
		agentClient = agentClient ==  null ? new AgentClient(): agentClient ;
	}

	@Test
	public void testGetAgentString() throws Exception {
		String rmi = "rmi://172.16.63.32:1098/tcm_agent";
		RemoteAgent remoteAgent = agentClient.getAgent(rmi);
		System.out.println(remoteAgent.getConnectRMI());
	}

	@Test
	public void testGetAgentStringStringString() throws Exception {
		System.out.println(agentClient.getAgent("127.0.0.1", "1098").getConnectRMI());
	}

}
