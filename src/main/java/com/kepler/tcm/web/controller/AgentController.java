package com.kepler.tcm.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kepler.tcm.service.AgentService;
import com.kepler.tcm.service.impl.AgentServiceImpl;

@RestController
@RequestMapping(value = "/agent")
public class AgentController {

	@Autowired
	private AgentService agentService;

	private static final Logger log = LoggerFactory
			.getLogger(AgentController.class);

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map<String, Object> add(String agent, String port, String memo)
			throws Exception {
		String agentName = agent + ":" + port;
		return agentService.add(agentName, memo);
	}

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public Map<String, Object> query() throws Exception {
		return agentService.query();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public Map<String, Object> edit(String oldagent, String agent, String port,
			String memo) throws Exception {
		String agentName = agent + ":" + port;
		return agentService.edit(oldagent, agentName, memo);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> delete(String agentName) throws Exception {
		return agentService.delete(agentName);
	}

	@RequestMapping(value = "/connect", method = RequestMethod.POST)
	public Map<String, Object> connect(String agent, String port)
			throws Exception {
		return agentService.connect(agent, port);
	}

	@RequestMapping(value = "/querystate", method = RequestMethod.GET)
	public Map<String, Object> querystate() throws Exception {
		return agentService.querystate();
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public void test() throws Exception {
		log.info("path:"+this.getClass().getClassLoader().getResource("").getPath());
		log.info("path1:"+this.getClass().getResource("").getPath());
		log.info("path2:"+AgentController.class.getResource("").getPath());
		log.info("path3:"+AgentController.class.getResource("agent.conf"));
		log.info("path4:"+AgentController.class.getResource("/agent.conf"));
		log.info("path5:"+AgentController.class.getResource("/"));
		log.info("path6:"+AgentController.class.getResource("agent.conf").getPath());
		log.info("path7:"+AgentController.class.getResource("/agent.conf").getPath());
		log.info("path8:"+AgentController.class.getResource("/").getPath());
		log.info("path9:"+AgentController.class.getResource("").getPath());
		log.info("path10:"+AgentController.class.getClassLoader().getResource("/"));
		log.info("path11:"+AgentController.class.getClassLoader().getResource("/agent.conf"));
		log.info("path12:"+AgentController.class.getClassLoader().getResource("agent.conf").getPath());
		log.info("path13:"+AgentController.class.getClassLoader().getResource("/").getPath());
		log.info("path14:"+AgentController.class.getClassLoader().getResource("/agent.conf").getPath());
		log.info("path15:"+AgentController.class.getClassLoader().getResource("agent.conf"));
		log.info("path16:"+Thread.currentThread().getContextClassLoader().getResource("agent.conf"));
		log.info("path17:"+Thread.currentThread().getContextClassLoader().getResource("agent.conf").getPath());
	}

}
