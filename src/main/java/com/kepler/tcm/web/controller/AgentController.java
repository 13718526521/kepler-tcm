package com.kepler.tcm.web.controller;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
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
		log.info("path:"+AgentServiceImpl.class.getClassLoader().getResource("agent.conf").getPath());
		log.info("-----");
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		log.info("path:"+path);
		log.info("-----");
		int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
		log.info("path.separator:"+System.getProperty("path.separator"));
		log.info("-----");
		int lastIndex = path.lastIndexOf(File.separator) + 1;
		log.info("File.separator:"+File.separator);
		log.info("-----");
		String new_path = path.substring(firstIndex, lastIndex);
		log.info("path:"+new_path);
	}

}
