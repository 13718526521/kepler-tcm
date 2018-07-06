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

import com.kepler.tcm.client.PluginClient;
import com.kepler.tcm.domain.Agent;
import com.kepler.tcm.service.PluginService;

@Service
public class PluginServiceImpl implements PluginService {

	private PluginClient pluginClient;

	private static final Logger log = LoggerFactory
			.getLogger(PluginServiceImpl.class);

	private void getIntence(String strAgent) {
		pluginClient = new PluginClient(strAgent);
	}

	@Override
	public Map query(HttpServletRequest request, HttpServletResponse response,
			String agentAndServer) throws Exception {
		if (pluginClient == null)
			getIntence(agentAndServer);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			String[][] array=pluginClient.getPlugins();
			for (int i = 0; i < array.length; i++) {
				Map data_map = new HashMap<>();
				data_map.put("id", array[i][0]);
				data_map.put("PluginName", array[i][1]);
				data_map.put("EntryClass", array[i][2]);
				list.add(i, data_map);
			}
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
			map.put("data", list);
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", "失败");
		}
		return map;
	}

	@Override
	public Map<String, Object> add(HttpServletRequest request,
			HttpServletResponse response, String agentAndServer, String id,
			String pluginName, String pluginMemo, String entryClass)
			throws Exception {
		if (pluginClient == null)
			getIntence(agentAndServer);
		HashMap<String, Object> map=new HashMap<String, Object>();
		HashMap param_map = new HashMap();
		param_map.put("id", id);
		param_map.put("pluginName", pluginName);
		param_map.put("pluginMemo", pluginMemo);
		param_map.put("entryClass", entryClass);
		param_map.put("fileList", "fileList");
		try {
			pluginClient.addPlugin(param_map);
			map.put("CODE",0);
			map.put("MESSAGE","成功");
		} catch (Exception e) {
			map.put("CODE",1);
			map.put("MESSAGE","失败");
		}
		return map;
	}

	@Override
	public Map<String, Object> edit(HttpServletRequest request,
			HttpServletResponse response, String agentAndServer, String id,
			String pluginName, String pluginMemo, String entryClass)
			throws Exception {
		if (pluginClient == null)
			getIntence(agentAndServer);
		HashMap<String, Object> map=new HashMap<String, Object>();
		HashMap param_map = new HashMap();
		param_map.put("id", id);
		param_map.put("pluginName", pluginName);
		param_map.put("pluginMemo", pluginMemo);
		param_map.put("entryClass", entryClass);
		param_map.put("fileList", "");
		try {
			pluginClient.editPlugin(param_map);
			map.put("CODE",0);
			map.put("MESSAGE","成功");
		} catch (Exception e) {
			map.put("CODE",1);
			map.put("MESSAGE","失败");
		}
		return map;
	}

	@Override
	public Map<String, Object> delete(HttpServletRequest request,
			HttpServletResponse response, String agentAndServer, String id)
			throws Exception {
		if (pluginClient == null)
			getIntence(agentAndServer);
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			pluginClient.removePlugin(id);
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", "失败");
		}
		return map;
	}

	@Override
	public Map<String, Object> reload(HttpServletRequest request,
			HttpServletResponse response, String agentAndServer, String id)
			throws Exception {
		if (pluginClient == null)
			getIntence(agentAndServer);
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			pluginClient.reloadPlugin(id);
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", "失败");
		}
		return map;
	}

	@Override
	public Map<String, Object> getpropertybyid(HttpServletRequest request,
			HttpServletResponse response, String agentAndServer, String id)
			throws Exception {
		if (pluginClient == null)
			getIntence(agentAndServer);
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap hashMap = new HashMap();
		try {
			hashMap=pluginClient.getPluginPropertyById(id);
			if(hashMap != null ) {
				map.put("CODE", 0);
				map.put("data", hashMap);
				map.put("MESSAGE", "成功");
			}else{
				map.put("CODE", 0);
				map.put("data",hashMap);
				map.put("MESSAGE", "暂时没有数据");
			}
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", "失败");
		}
		return map;
	}

}