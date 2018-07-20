package com.kepler.tcm.web.controller;

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

	private static final Logger log = LoggerFactory
			.getLogger(ServerController.class);

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public Map<String, Object> query(String agentName) throws Exception {
		return serverService.query(agentName);
	}

	@RequestMapping(value = "/querystate", method = RequestMethod.POST)
	public Map<String, Object> querystate(String agentName) throws Exception {
		return serverService.querystate(agentName);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map<String, Object> add(String agentName, String serverName,
			String autoRestart, String monitorInterval, String serverPort,
			String monitorPort, String memo) throws Exception {
		return serverService.add(agentName, serverName, autoRestart,
				monitorInterval, serverPort, monitorPort, memo);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public Map<String, Object> edit(HttpServletRequest request,
			HttpServletResponse response, String agentName, String serverName,
			String autoRestart, String monitorInterval, String serverPort,
			String monitorPort, String memo) throws Exception {
		return serverService.edit(agentName, serverName, autoRestart,
				monitorInterval, serverPort, monitorPort, memo);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Map<String, Object> get(String agentName, String serverName)
			throws Exception {
		return serverService.get(agentName, serverName);
	}

	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public Map<String, Object> start(String agentName, String serverName)
			throws Exception {
		return serverService.start(agentName, serverName);
	}

	@RequestMapping(value = "/restart", method = RequestMethod.POST)
	public Map<String, Object> restart(String agentName, String serverName)
			throws Exception {
		return serverService.restart(agentName, serverName);
	}

	@RequestMapping(value = "/stop", method = RequestMethod.POST)
	public Map<String, Object> stop(String agentName, String serverName)
			throws Exception {
		return serverService.stop(agentName, serverName);
	}

	@RequestMapping(value = "/started", method = RequestMethod.POST)
	public boolean started(String agentName, String serverName)
			throws Exception {
		return serverService.started(agentName, serverName);
	}

	@RequestMapping(value = "/serverhost", method = RequestMethod.GET)
	public Map<String, Object> serverhost(String agentName, String serverName)
			throws Exception {
		return serverService.serverhost(agentName, serverName);
	}

	@RequestMapping(value = "/serverport", method = RequestMethod.GET)
	public Map<String, Object> serverport(String agentName, String serverName)
			throws Exception {
		return serverService.serverport(agentName, serverName);
	}

	@RequestMapping(value = "/monitorport", method = RequestMethod.GET)
	public Map<String, Object> monitorport(String agentName, String serverName)
			throws Exception {
		return serverService.monitorport(agentName, serverName);
	}
	
	@RequestMapping(value = "/memoInfo", method = RequestMethod.GET)
	public Map<String, Object> memoInfo(String agentName, String serverName)
			throws Exception {
		return serverService.memoInfo(agentName, serverName);
	}
	
}
