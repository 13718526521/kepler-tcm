package com.kepler.tcm.server.tcm.agent;

import com.kepler.tcm.server.tcm.server.RemoteServer;
import java.rmi.Naming;

public class ServerTool {
	public static RemoteServer getServer(int serverPort) throws Exception {
		RemoteServer server = (RemoteServer) Naming.lookup("rmi://" + Agent.host + ":" + serverPort + "/tcm_server");
		return server;
	}
}