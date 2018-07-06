package com.kepler.tcm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger log = LoggerFactory
			.getLogger(AgentServiceImpl.class);

	/* log.info("getRequestURI对应的路径为"+request.getRequestURI()); */
	/* log.info("getContextPath对应的路径为"+request.getContextPath()); */
	/* log.info("getRealPath对应的路径为"+request.getRealPath("/")); */
	/* log.info("getRequestURL对应的路径为"+request.getRequestURL()); */
	/* log.info(this.getClass().getClassLoader().getResource("").getPath()); */
	@Override
	public Map<String, Object> add(HttpServletRequest request, HttpServletResponse response,
			String agentName, String memo) throws Exception {
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
				map.put("MESSAGE", "失败");
			}
		}
		return map;
		
	}

	private void getIntence() {
		try {
			agentConfig = new AgentConfig(this.getClass().getClassLoader()
					.getResource("").getPath(), "agent.conf");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> query(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
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
		return map;
	}

	@Override
	public Map<String, Object> edit(HttpServletRequest request,
			HttpServletResponse response, String oldagent, String agentName,
			String memo) throws Exception {
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
				map.put("MESSAGE", "失败");
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> delete(HttpServletRequest request,
			HttpServletResponse response, String agentName) throws Exception {
		if (agentConfig == null)
			getIntence();
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			agentConfig.remove(agentName);
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", "失败");
		}
		return map;
	}

	@Override
	public Map<String, Object> connect(HttpServletRequest request,
			HttpServletResponse response, String agent, String port)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		RemoteAgent remoteAgent = null;
		try {
			remoteAgent = new AgentClient().getAgent(agent, port);
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
			map.put("data", remoteAgent);
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", "失败");
		}
		return map;
	}
	
	@Override
	public Map querystate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
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
				state_map=connect(request,response,agentConfig.getName(i).split(":")[0],agentConfig.getName(i).split(":")[1]);
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
		return map;
	}
}
