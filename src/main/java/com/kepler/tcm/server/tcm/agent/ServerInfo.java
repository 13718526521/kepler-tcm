package com.kepler.tcm.server.tcm.agent;

import java.io.Serializable;

public class ServerInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	public String server = "";
	public int port;
	public int monitorPort;
	public int monitorInterval;
	public String memo = "";
	public String path = "";
}