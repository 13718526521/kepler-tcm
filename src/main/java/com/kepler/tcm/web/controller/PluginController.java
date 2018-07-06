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

import com.kepler.tcm.service.PluginService;

@RestController
@RequestMapping(value = "/plugin")
public class PluginController {

	@Autowired
	private PluginService pluginService;

	private static final Logger log = LoggerFactory
			.getLogger(PluginController.class);

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public Map<String, Object> query(HttpServletRequest request,
			HttpServletResponse response, String agentAndServer)
			throws Exception {
		return pluginService.query(request, response, agentAndServer);
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public Map<String, Object> add(HttpServletRequest request,
			HttpServletResponse response,String agentAndServer,String id,String pluginName,String pluginMemo,String entryClass)
			throws Exception {
		return pluginService.add(request, response, agentAndServer,id,pluginName,pluginMemo,entryClass);
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public Map<String, Object> edit(HttpServletRequest request,
			HttpServletResponse response,String agentAndServer,String id,String pluginName,String pluginMemo,String entryClass)
			throws Exception {
		return pluginService.edit(request, response, agentAndServer,id,pluginName,pluginMemo,entryClass);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public Map<String, Object> delete(HttpServletRequest request,
			HttpServletResponse response,String agentAndServer,String id)
			throws Exception {
		return pluginService.delete(request, response, agentAndServer,id);
	}
	
	@RequestMapping(value = "/reload", method = RequestMethod.GET)
	public Map<String, Object> reload(HttpServletRequest request,
			HttpServletResponse response,String agentAndServer,String id)
			throws Exception {
		return pluginService.reload(request, response, agentAndServer,id);
	}
	
	@RequestMapping(value = "/getpropertybyid", method = RequestMethod.GET)
	public Map<String, Object> getpropertybyid(HttpServletRequest request,
			HttpServletResponse response,String agentAndServer,String id)
			throws Exception {
		return pluginService.getpropertybyid(request, response, agentAndServer,id);
	}

}
