package com.kepler.tcm.web.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kepler.tcm.service.AgentService;

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

}
