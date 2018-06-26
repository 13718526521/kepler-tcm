package com.kepler.tcm.client;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kepler.tcm.core.agent.RemoteAgent;
import com.kepler.tcm.core.server.RemoteServer;

/**
 * 应用服务器客户端
 * 
 * @author wangsp
 * @date 2018年6月19日
 * @version V1.0
 */
public class ServerClient {
	
	private final static Logger log = LoggerFactory.getLogger(ServerClient.class);

	private String strServerName = "";

	private String strServerUrl = "";

	private int nServerPort = 1099;

	private String strRMIUrl = "";

	private RemoteServer remoteServer = null;

	private boolean bIsConnected = false;

	private HashMap taskMap = new HashMap();

	private String[] pluginNames = new String[0];

	private String[] databaseNames = new String[0];

	public String strAgent = "";

	/**
	 * 服务器名
	 */
	public String serverName = "";

	/**
	 * 远程代理
	 */
	private RemoteAgent agent;

	/**
	 * 构造函数
	 * 
	 * @param agentAndServer
	 *            eg: 172.16.13.248:1098@server_01
	 */
	public ServerClient(String agentAndServer) {
		int n = agentAndServer.indexOf('@');
		if (n == -1) {
			this.strAgent = agentAndServer;
		} else {
			this.strAgent = agentAndServer.substring(0, n);
			this.serverName = agentAndServer.substring(n + 1);
		}
	}
	
	
	private RemoteAgent getAgent() throws Exception{
		String rmi = "rmi://" + this.strAgent + "/tcm_agent";
		agent = new AgentClient().getAgent(rmi);
		return agent ;
	}
	
	/**
	 * 获取服务列表
	 * @return
	 * @throws Exception 
	 */
	public  Map getServerMap() throws Exception{
		if (this.agent == null)getAgent();
		return this.agent.getServerMap();
	}
	
	/**
	 * 添加服务
	 * @param serverName 服务名
	 * @param paramMap   服务字段属性集
	 * @throws Exception 
	 */
	public  void addServer(String serverName, Map paramMap) throws Exception{
		if (this.agent == null)getAgent();
		this.agent.addServer(serverName == null ? this.serverName
				: serverName,paramMap);
	}

	/**
	 * 获取指定服务
	 * 
	 * @return
	 * @throws Exception
	 */
	public RemoteServer getServer(String serverName) throws Exception {
		if (this.agent == null) getAgent();
		return this.agent.getServer(serverName == null ? this.serverName
				: serverName);
	}

	/**
	 * 启动指定服务
	 * 
	 * @return
	 * @throws Exception
	 */
	public void startServer(String serverName) throws Exception {
		if (this.agent == null)getAgent();
		this.agent.startServer(serverName == null ? this.serverName
				: serverName);
	}
	
	/**
	 * 重启指定服务
	 * 
	 * @return
	 * @throws Exception
	 */
	public void restartServer(String serverName,boolean keepState)throws Exception {
		if (this.agent == null)getAgent();
		this.agent.restartServer(serverName == null ? this.serverName
				: serverName, keepState);
	}
	
	/**
	 * 停止服务
	 * @param serverName
	 * @throws Exception
	 */
	public void stopServer(String serverName)throws Exception {
		if (this.agent == null)getAgent();
		this.agent.stopServer(serverName == null ? this.serverName
				: serverName);
	}
	
	/**
	 * 服务是否已启动
	 * @param serverName
	 * @return
	 * @throws Exception
	 */
	public boolean serverStarted(String serverName) throws Exception{
		if (this.agent == null)getAgent();
		return this.agent.serverStarted(serverName == null ? this.serverName
				: serverName);
	}
	
	/**
	 * 获取服务主机
	 * @param serverName
	 * @return
	 * @throws Exception 
	 */
	public  String getServerHost(String serverName) throws Exception{
		if (this.agent == null)getAgent();
	return this.agent.getServerHost(serverName == null ? this.serverName
			: serverName);
	}
	
	/**
	 * 获取服务端口
	 * @param serverName
	 * @return
	 * @throws Exception 
	 */
	public  String getServerPort(String serverName) throws Exception{
		if (this.agent == null)getAgent();
		return this.agent.getServerPort(serverName == null ? this.serverName
				: serverName);
	}

	/**
	 * 获取监控端口
	 * @param serverName
	 * @return
	 * @throws Exception
	 */
	public  String getMonitorPort(String serverName) throws Exception {
		if (this.agent == null)getAgent();
		return this.agent.getMonitorPort(serverName == null ? this.serverName
				: serverName);
	}

	/**
	 * 启动监控服务
	 * @param serverName
	 * @throws Exception
	 */
	public  void startMonitor(String serverName) throws Exception{
		if (this.agent == null)getAgent();
		this.agent.stopMonitor(serverName == null ? this.serverName
				: serverName);
	}
	
	/**
	 * 停止监控服务
	 * @param serverName
	 * @throws Exception
	 */
	public  void stopMonitor(String serverName) throws Exception{
		if (this.agent == null)getAgent();
		this.agent.stopMonitor(serverName == null ? this.serverName
				: serverName);
	}

	/**
	 * 重启监控服务
	 * @param serverName
	 * @throws Exception
	 */
	public  void restartMonitor(String serverName) throws Exception{
		if (this.agent == null)getAgent();
		this.agent.restartMonitor(serverName == null ? this.serverName
				: serverName);
	}

	/**
	 * 获取服务配置
	 * @param serverName
	 * @param isOriginal
	 * @return
	 * @throws Exception
	 */
	public  Map getServerConfig(String serverName, boolean isOriginal) throws Exception{
		if (this.agent == null)getAgent();
		return this.agent.getServerConfig(serverName == null ? this.serverName
				: serverName,isOriginal);
	}

	/**
	 *设置服务配置
	 * @param serverName 服务名
	 * @param paramMap   服务字段属性集
	 * @throws Exception 
	 */
	public  void setServerConfig(String serverName, Map paramMap) throws Exception{
		if (this.agent == null)getAgent();
		this.agent.setServerConfig(serverName == null ? this.serverName
				: serverName,paramMap);
	}
	
	
	public String getServerName() {
		return this.serverName;
	}

	public static void main(String[] args) throws Exception {
		String rmi = "rmi://127.0.0.1:1098/tcm_agent";

		RemoteAgent remoteAgent = (RemoteAgent) Naming.lookup(rmi);
		String msg = remoteAgent.getConnectRMI();
		System.out.println("client:" + msg);
		// upload(remoteAgent, "c:\\1.jpg", "d:\\aagex.jpg");
		
		//输出localhost映射的所有IP地址
		InetAddress[] ips = InetAddress.getAllByName("localhost");
		if (ips != null) {
		    for (InetAddress ip : ips) {
		        System.out.println(ip.getHostAddress());
		    }
		}
	}

}
