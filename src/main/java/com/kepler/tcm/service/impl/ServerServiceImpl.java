package com.kepler.tcm.service.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kepler.tcm.client.ServerClient;
import com.kepler.tcm.client.TaskClient;
import com.kepler.tcm.core.agent.ServerInfo;
import com.kepler.tcm.core.server.RemoteServer;
import com.kepler.tcm.core.task.RemoteTask;
import com.kepler.tcm.service.ServerService;
@Service
public class ServerServiceImpl implements ServerService {
	
	private static final Logger log  = LoggerFactory.getLogger(ServerServiceImpl.class);
	
	@Override
	public Map<String, Object> query(String agentName) throws Exception {
			long startTime = System.currentTimeMillis();
			List<ServerInfo> list = new ArrayList<ServerInfo>();
			HashMap<String, Object> map=new HashMap<String, Object>();
			ServerClient serverClient=new ServerClient(agentName);
			try {
				Set keys=serverClient.getServerMap().keySet();
				String message="";
				for (Object object : keys) {
					ServerInfo serverInfo=(ServerInfo) serverClient.getServerMap().get(object);
					list.add(serverInfo);
				}
				map.put("data", list);
				map.put("CODE",0);
				map.put("MESSAGE","成功");
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw, true));  
				map.put("CODE", 1);
				map.put("MESSAGE", e.getMessage());
				log.info("异常信息为:"+sw.toString());
				e.printStackTrace();
			}
			long endTime = System.currentTimeMillis();
			log.info("server-query程序运行时间："+(endTime-startTime)+"ms");
			return map;
	}
	
	@Override
	public Map<String, Object> querystate(String agentName) throws Exception {
		long startTime = System.currentTimeMillis();
		ServerClient serverClient=new ServerClient(agentName);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			Set keys=serverClient.getServerMap().keySet();
			List key_list = new ArrayList(keys);
			for (int i = 0; i < key_list.size(); i++) {
				ServerInfo serverInfo=(ServerInfo) serverClient.getServerMap().get(key_list.get(i));
				Map data_map = new HashMap<>();
				data_map.put("serverName",  serverInfo.serverName);
				data_map.put("port",  serverInfo.port);
				data_map.put("monitorPort",  serverInfo.monitorPort);
				data_map.put("monitorInterval",  serverInfo.monitorInterval);
				data_map.put("memo",  serverInfo.memo);
				Boolean flag=started(agentName, serverInfo.serverName);
				data_map.put("started", flag);
				list.add(i, data_map);
			}
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
			map.put("data", list);
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("server-querystate程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}
	
	@Override
	public Map<String, Object> add( String agentName,String serverName,String autoRestart,String monitorInterval,String serverPort,String monitorPort,String memo) throws Exception {
			long startTime = System.currentTimeMillis();
			ServerClient serverClient=new ServerClient(agentName);
			HashMap<String, Object> param_map=new HashMap<String, Object>();
			HashMap<String, Object> map=new HashMap<String, Object>();
			param_map.put("autoRestart",autoRestart);
			param_map.put("monitorInterval",monitorInterval);
			param_map.put("serverPort",serverPort);
			param_map.put("monitorPort",monitorPort);
			param_map.put("memo", memo);
			try {
				serverClient.addServer(serverName, param_map);
				map.put("CODE",0);
				map.put("MESSAGE","成功");
			} catch (Exception e) {
				map.put("CODE",1);
				map.put("MESSAGE",e.getMessage());
			}
			long endTime = System.currentTimeMillis();
			log.info("server-add程序运行时间："+(endTime-startTime)+"ms");	
			return map;
	}
	
	@Override
	public Map<String, Object> edit( String agentName, String serverName,String autoRestart, String monitorInterval, String serverPort,String monitorPort, String memo) throws Exception {
		long startTime = System.currentTimeMillis();
		ServerClient serverClient=new ServerClient(agentName);
		HashMap<String, Object> param_map=new HashMap<String, Object>();
		HashMap<String, Object> map=new HashMap<String, Object>();
		param_map.put("autoRestart",autoRestart);
		param_map.put("monitorInterval",monitorInterval);
		param_map.put("serverPort",serverPort);
		param_map.put("monitorPort",monitorPort);
		param_map.put("memo", memo);
		try {
			serverClient.setServerConfig(serverName, param_map);
			map.put("CODE",0);
			map.put("MESSAGE","成功");
		} catch (Exception e) {
			map.put("CODE",1);
			map.put("MESSAGE",e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("server-edit程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}
	
	@Override
	public Map<String, Object> get(String agentName,String serverName) throws Exception {
		long startTime = System.currentTimeMillis();
		ServerClient serverClient=new ServerClient(agentName);
		HashMap<String, Object> map=new HashMap<String, Object>();
		RemoteServer remoteServer = null;
		try {
			remoteServer = serverClient.getServer(serverName);
			if(!remoteServer.getServerName().equals(serverName)){
				map.put("CODE", 1);
				map.put("MESSAGE", "服务器尚未启动");
			}else{
				map.put("CODE", 0);
				map.put("MESSAGE", "成功");
			}
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE",e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("server-get程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> start(String agentName, String serverName)
			throws Exception {
		long startTime = System.currentTimeMillis();
		HashMap<String, Object> map=new HashMap<String, Object>();
		ServerClient serverClient=new ServerClient(agentName);
		try {
			serverClient.startServer(serverName);
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("server-start程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> restart(String agentName, String serverName)
			throws Exception {
		long startTime = System.currentTimeMillis();
		ServerClient serverClient=new ServerClient(agentName);
		HashMap<String, Object> map=new HashMap<String, Object>();
		try {
			serverClient.restartServer(serverName, false);
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("server-restart程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> stop(String agentName, String serverName)
			throws Exception {
		long startTime = System.currentTimeMillis();
		HashMap<String, Object> map=new HashMap<String, Object>();
		ServerClient serverClient=new ServerClient(agentName);
		try {
			serverClient.stopServer(serverName);
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("server-stop程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public boolean started(String agentName, String serverName)
			throws Exception {
		ServerClient serverClient=new ServerClient(agentName);
		return serverClient.serverStarted(serverName);
	}

	@Override
	public Map<String, Object> serverhost(String agentName, String serverName)
			throws Exception {
		long startTime = System.currentTimeMillis();
		ServerClient serverClient=new ServerClient(agentName);
		HashMap<String, Object> map=new HashMap<String, Object>();
		try {
			String result=serverClient.getServerHost(serverName);
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
			map.put("data", result);
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("server-serverhost程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> serverport(String agentName, String serverName)
			throws Exception {
		long startTime = System.currentTimeMillis();
		HashMap<String, Object> map=new HashMap<String, Object>();
		ServerClient serverClient=new ServerClient(agentName);
		try {
			String result=serverClient.getServerPort(serverName);
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
			map.put("data", result);
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("server-serverport程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> monitorport(String agentName, String serverName)
			throws Exception {
		long startTime = System.currentTimeMillis();
		HashMap<String, Object> map=new HashMap<String, Object>();
		ServerClient serverClient=new ServerClient(agentName);
		try {
			String result=serverClient.getMonitorPort(serverName);
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
			map.put("data", result);
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("server-monitorport程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> memoInfo(String agentName, String serverName)
			throws Exception {
		long startTime = System.currentTimeMillis();
		HashMap<String, Object> map=new HashMap<String, Object>();
		ServerClient serverClient=new ServerClient(agentName);
		HashMap hashMap = new HashMap();
		try {
			hashMap =serverClient.getMemoInfo(serverName);
			map.put("CODE", 0);
			map.put("data", hashMap);
			map.put("MESSAGE", "成功");
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("server-memoInfo程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

}