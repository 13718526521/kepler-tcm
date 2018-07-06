package com.kepler.tcm.web.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kepler.tcm.service.AgentService;
import com.kepler.tcm.service.RestApiService;

@RestController
@RequestMapping(value = "/agent")
public class AgentController {

	@Autowired
	private AgentService agentService;

	private static final Logger log = LoggerFactory.getLogger(AgentController.class);

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public Map<String, Object> add(HttpServletRequest request, HttpServletResponse response,String agent,String port,String memo) throws Exception {
		String agentName=agent+":"+port;
		return agentService.add(request, response,agentName,memo);
	}
	
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public Map<String, Object> query(HttpServletRequest request, HttpServletResponse response) throws Exception {
		 return agentService.query(request, response);
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public Map<String, Object> edit(HttpServletRequest request, HttpServletResponse response,String oldagent,String agent,String port,String memo) throws Exception {
		String agentName=agent+":"+port;
		return agentService.edit(request, response,oldagent,agentName,memo);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public Map<String, Object> delete(HttpServletRequest request, HttpServletResponse response,String agentName) throws Exception {
		return agentService.delete(request, response,agentName);
	}
	
	@RequestMapping(value = "/connect", method = RequestMethod.GET)
	public Map<String, Object> connect(HttpServletRequest request, HttpServletResponse response,String agent,String port) throws Exception {
		return agentService.connect(request, response,agent,port);
	}
	
	@RequestMapping(value = "/querystate", method = RequestMethod.GET)
	public Map<String, Object> querystate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return agentService.querystate(request, response);
	}

}
