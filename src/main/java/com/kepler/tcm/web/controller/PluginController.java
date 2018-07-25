package com.kepler.tcm.web.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kepler.tcm.domain.Plugin;
import com.kepler.tcm.service.PluginService;

@RestController
@RequestMapping(value = "/plugin")
public class PluginController {

	@Autowired
	private PluginService pluginService;

	private static final Logger log = LoggerFactory.getLogger(PluginController.class);

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public Map<String, Object> query(String agentAndServer) throws Exception {
		return pluginService.query(agentAndServer);
	}

	@RequestMapping(value = "/querydetail", method = RequestMethod.POST)
	public Map<String, Object> querydetail(String agentAndServer) throws Exception {
		return pluginService.querydetail(agentAndServer);
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public Map<String, Object> add(String agentAndServer, String id,String pluginName, String pluginMemo, String entryClass) throws Exception {
		return pluginService.add(agentAndServer, id, pluginName, pluginMemo,entryClass);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public Map<String, Object> edit(String agentAndServer, String id,String pluginName, String pluginMemo, String entryClass) throws Exception {
		return pluginService.edit(agentAndServer, id, pluginName, pluginMemo,entryClass);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> delete(String agentAndServer, String id) throws Exception {
		return pluginService.delete(agentAndServer, id);
	}

	@RequestMapping(value = "/reload", method = RequestMethod.POST)
	public Map<String, Object> reload(String agentAndServer, String id) throws Exception {
		return pluginService.reload(agentAndServer, id);
	}

	@RequestMapping(value = "/getpropertybyid", method = RequestMethod.GET)
	public Map<String, Object> getpropertybyid(String agentAndServer, String id) throws Exception {
		return pluginService.getpropertybyid(agentAndServer, id);
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public Map<String, Object> upload(String agentAndServer,Plugin plugin, MultipartFile[] file,String[] className,String auto_plugin_id) throws Exception {
		return pluginService.upload(agentAndServer,plugin, file,className,auto_plugin_id);
	}
	
	@RequestMapping(value = "/uploadedit", method = RequestMethod.POST)
	public Map<String, Object> uploadedit(String agentAndServer,Plugin plugin, MultipartFile[] file,String[] className) throws Exception {
		return pluginService.uploadedit(agentAndServer,plugin, file,className);
	}

}
