package com.kepler.tcm.core.agent;

import java.rmi.Naming;

import com.kepler.tcm.core.server.RemoteServer;

public class ServerTool {
	public static RemoteServer getServer(int serverPort) throws Exception {
		RemoteServer server = (RemoteServer) Naming.lookup("rmi://" + Agent.host + ":" + serverPort + "/tcm_server");
		return server;
	}
}