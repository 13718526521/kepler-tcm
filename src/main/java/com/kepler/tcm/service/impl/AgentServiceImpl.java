package com.kepler.tcm.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kepler.tcm.client.AgentClient;
import com.kepler.tcm.client.AgentConfig;
import com.kepler.tcm.core.agent.RemoteAgent;
import com.kepler.tcm.domain.Agent;
import com.kepler.tcm.domain.ProxyServer;
import com.kepler.tcm.service.AgentService;

@Service
public class AgentServiceImpl implements AgentService {

	private AgentConfig agentConfig;
	
	@Autowired
	private Environment env;

	private static final Logger log = LoggerFactory.getLogger(AgentServiceImpl.class);

	@Override
	public Map<String, Object> add(String agentName, String memo) throws Exception {
		long startTime = System.currentTimeMillis();
		if (agentConfig == null)
			getIntence();
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (agentConfig.indexOfName(agentName) >= 0) {
			map.put("CODE", 1);
			map.put("MESSAGE", agentName + "已经存在,取消新增。");
		} else {
			try {
				agentConfig.add(agentName, memo);
				map.put("CODE", 0);
				map.put("MESSAGE", "成功");
			} catch (Exception e) {
				map.put("CODE", 1);
				map.put("MESSAGE", e.getMessage());
			}
		}
		long endTime = System.currentTimeMillis();
		log.info("agent-add程序运行时间："+(endTime-startTime)+"ms");
		return map;
		
	}

	private void getIntence() {
		String window_path=env.getProperty("spring.application.windows.path");
		String linux_path=env.getProperty("spring.application.linux.path");
		String os=System.getProperty("os.name");
		if (os != null && os.toLowerCase().indexOf("linux") > -1) {
			File file = new File(linux_path,"agent.conf");
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			try {
				if(!file.exists()){
					file.createNewFile();
				}
				agentConfig = new AgentConfig(linux_path+"/", "agent.conf");
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			File fileDir = new File(window_path); 
			fileDir.mkdirs(); 
			File file = new File(window_path+"\\"+"agent.conf");
			try {
				if(!file.exists())
					file.createNewFile(); 
				agentConfig = new AgentConfig(window_path+"\\", "agent.conf");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Map<String, Object> query() throws Exception {
		long startTime = System.currentTimeMillis();
		if (agentConfig == null)
			getIntence();
		List<ProxyServer> list = new ArrayList<ProxyServer>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (agentConfig.size() == 0) {
			map.put("data", list);
			map.put("CODE", 1);
			map.put("size", agentConfig.size());
			map.put("MESSAGE", "暂时没有数据");
		} else {
			for (int i = 0; i < agentConfig.size(); i++) {
				ProxyServer proxyServer = new ProxyServer();
				proxyServer.setId((i+1) + "");
				proxyServer.setAgentName(agentConfig.getName(i));
				proxyServer.setMemo(agentConfig.getValue(i));
				list.add(proxyServer);
			}
			map.put("data", list);
			map.put("CODE", 0);
			map.put("size", agentConfig.size());
			map.put("MESSAGE", "成功");
		}
		long endTime = System.currentTimeMillis();
		log.info("agent-query程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> edit(String oldagent, String agentName,String memo) throws Exception {
		long startTime = System.currentTimeMillis();
		if (agentConfig == null)
			getIntence();
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (agentConfig.indexOfName(agentName) >= 0) {
			map.put("CODE", 1);
			map.put("MESSAGE", agentName + "已经存在,取消修改。");
		} else {
			try {
				agentConfig.edit(oldagent, agentName, memo);
				map.put("CODE", 0);
				map.put("MESSAGE", "成功");
			} catch (Exception e) {
				map.put("CODE", 1);
				map.put("MESSAGE", e.getMessage());
			}
		}
		long endTime = System.currentTimeMillis();
		log.info("agent-edit程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> delete(String agentName) throws Exception {
		long startTime = System.currentTimeMillis();
		if (agentConfig == null)
			getIntence();
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			agentConfig.remove(agentName);
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("agent-delete程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> connect(String agent, String port) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		long startTime = System.currentTimeMillis();
		RemoteAgent remoteAgent = null;
		try {
			remoteAgent = new AgentClient().getAgent(agent, port);
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
			map.put("data", remoteAgent);
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("agent-connect程序运行时间："+(endTime-startTime)+"ms");
		return map;
		
	}
	
	@Override
	public Map querystate() throws Exception {
		long startTime = System.currentTimeMillis();
		if (agentConfig == null)
			getIntence();
		List<Agent> list = new ArrayList<Agent>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> state_map = new HashMap<String, Object>();
		if (agentConfig.size() == 0) {
			map.put("data", list);
			map.put("CODE", 1);
			map.put("size", agentConfig.size());
			map.put("MESSAGE", "暂时没有数据");
		} else {
			for (int i = 0; i < agentConfig.size(); i++) {
				Agent agent= new Agent();
				agent.setId((i+1) + "");
				agent.setAgentName(agentConfig.getName(i));
				state_map=connect(agentConfig.getName(i).split(":")[0],agentConfig.getName(i).split(":")[1]);
				agent.setState_code(state_map.get("CODE").toString());
				agent.setState_message(state_map.get("MESSAGE").toString());
				agent.setMemo(agentConfig.getValue(i));
				list.add(agent);
			}
			map.put("data", list);
			map.put("CODE", 0);
			map.put("size", agentConfig.size());
			map.put("MESSAGE", "成功");
		}
		long endTime = System.currentTimeMillis();
		log.info("agent-querystate程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}
}
