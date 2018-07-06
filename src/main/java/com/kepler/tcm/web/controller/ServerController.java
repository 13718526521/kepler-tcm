package com.kepler.tcm.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kepler.tcm.service.ServerService;

@RestController
@RequestMapping(value = "/server")
public class ServerController {

	@Autowired
	private ServerService serverService;

	private static final Logger log = LoggerFactory.getLogger(ServerController.class);

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public Map<String, Object> query(HttpServletRequest request, HttpServletResponse response,String agentName) throws Exception {
		 return serverService.query(request, response,agentName);
	}
	@RequestMapping(value = "/querystate", method = RequestMethod.GET)
	public Map<String, Object> querystate(HttpServletRequest request, HttpServletResponse response,String agentName) throws Exception {
		 return serverService.querystate(request, response,agentName);
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public Map<String, Object> add(HttpServletRequest request, HttpServletResponse response,String agentName,String serverName,String autoRestart,String monitorInterval,String serverPort,String monitorPort,String memo) throws Exception {
		 return serverService.add(request, response,agentName,serverName,autoRestart,monitorInterval,serverPort,monitorPort,memo);
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public Map<String, Object> edit(HttpServletRequest request, HttpServletResponse response,String agentName,String serverName,String autoRestart,String monitorInterval,String serverPort,String monitorPort,String memo) throws Exception {
		 return serverService.edit(request, response,agentName,serverName,autoRestart,monitorInterval,serverPort,monitorPort,memo);
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Map<String, Object> get(HttpServletRequest request, HttpServletResponse response,String agentName,String serverName) throws Exception {
		 return serverService.get(request, response,agentName,serverName);
	}
	
	@RequestMapping(value = "/start", method = RequestMethod.GET)
	public Map<String, Object> start(HttpServletRequest request, HttpServletResponse response,String agentName,String serverName) throws Exception {
		 return serverService.start(request, response,agentName,serverName);
	}
	
	@RequestMapping(value = "/restart", method = RequestMethod.GET)
	public Map<String, Object> restart(HttpServletRequest request, HttpServletResponse response,String agentName,String serverName) throws Exception {
		 return serverService.restart(request, response,agentName,serverName);
	}
	
	@RequestMapping(value = "/stop", method = RequestMethod.GET)
	public Map<String, Object> stop(HttpServletRequest request, HttpServletResponse response,String agentName,String serverName) throws Exception {
		 return serverService.stop(request, response,agentName,serverName);
	}
	
	@RequestMapping(value = "/started", method = RequestMethod.GET)
	public boolean started(HttpServletRequest request, HttpServletResponse response,String agentName,String serverName) throws Exception {
		 return serverService.started(request, response,agentName,serverName);
	}

	@RequestMapping(value = "/serverhost", method = RequestMethod.GET)
	public Map<String, Object> serverhost(HttpServletRequest request, HttpServletResponse response,String agentName,String serverName) throws Exception {
		 return serverService.serverhost(request, response,agentName,serverName);
	}
	
	@RequestMapping(value = "/serverport", method = RequestMethod.GET)
	public Map<String, Object> serverport(HttpServletRequest request, HttpServletResponse response,String agentName,String serverName) throws Exception {
		 return serverService.serverport(request, response,agentName,serverName);
	}
	
	@RequestMapping(value = "/monitorport", method = RequestMethod.GET)
	public Map<String, Object> monitorport(HttpServletRequest request, HttpServletResponse response,String agentName,String serverName) throws Exception {
		 return serverService.monitorport(request, response,agentName,serverName);
	}

}
