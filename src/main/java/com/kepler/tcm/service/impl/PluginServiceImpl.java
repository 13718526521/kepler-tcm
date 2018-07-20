package com.kepler.tcm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kepler.tcm.client.PluginClient;
import com.kepler.tcm.core.util.FileTools;
import com.kepler.tcm.core.util.StringList;
import com.kepler.tcm.domain.Plugin;
import com.kepler.tcm.service.PluginService;

@Service
public class PluginServiceImpl implements PluginService {

	private static final Logger log = LoggerFactory.getLogger(PluginServiceImpl.class);

	@Override
	public Map query(String agentAndServer) throws Exception {
		long startTime = System.currentTimeMillis();
		PluginClient pluginClient=new PluginClient(agentAndServer);
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
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("plugin-query程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}
	
	@Override
	public Map querydetail(String agentAndServer) throws Exception {
		long startTime = System.currentTimeMillis();
		PluginClient pluginClient=new PluginClient(agentAndServer);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			String[][] array=pluginClient.getPlugins();
			for (int i = 0; i < array.length; i++) {
				Map data_map = new HashMap<>();
				HashMap hashMap = pluginClient.getPluginPropertyById(array[i][0]);
				data_map.put("id", array[i][0]);
				data_map.put("PluginName", array[i][1]);
				data_map.put("EntryClass", array[i][2]);
				data_map.put("PluginPath", hashMap.get("pluginPath"));
				data_map.put("PluginMemo", hashMap.get("pluginMemo"));
				data_map.put("Error", hashMap.get("error"));
				data_map.put("Version", hashMap.get("version"));
				data_map.put("FileList", hashMap.get("fileList"));
				list.add(i, data_map);
			}
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
			map.put("data", list);
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("plugin-querydetail程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> add(String agentAndServer, String id,
			String pluginName, String pluginMemo, String entryClass)
			throws Exception {
		long startTime = System.currentTimeMillis();
		PluginClient pluginClient=new PluginClient(agentAndServer);
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
			map.put("MESSAGE",e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("plugin-add程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> edit(String agentAndServer, String id,
			String pluginName, String pluginMemo, String entryClass)
			throws Exception {
		long startTime = System.currentTimeMillis();
		PluginClient pluginClient=new PluginClient(agentAndServer);
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
			map.put("MESSAGE",e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("plugin-edit程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> delete(String agentAndServer, String id)
			throws Exception {
		long startTime = System.currentTimeMillis();
		PluginClient pluginClient=new PluginClient(agentAndServer);
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			pluginClient.removePlugin(id);
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("plugin-delete程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> reload(String agentAndServer, String id)
			throws Exception {
		long startTime = System.currentTimeMillis();
		PluginClient pluginClient=new PluginClient(agentAndServer);
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			pluginClient.reloadPlugin(id);
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("plugin-reload程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> getpropertybyid(String agentAndServer, String id)
			throws Exception {
		long startTime = System.currentTimeMillis();
		PluginClient pluginClient=new PluginClient(agentAndServer);
		HashMap<String, Object> map = new HashMap<String, Object>();
		List list = new ArrayList();
		HashMap hashMap = new HashMap();
		StringList fileList = new StringList();
		try {
			hashMap=pluginClient.getPluginPropertyById(id);
			if(hashMap != null ) {
				map.put("CODE", 0);
				map.put("data", hashMap);
				map.put("MESSAGE", "成功");
				fileList.setAllText((String) hashMap.get("fileList"));
				for (int i = 0; i < fileList.getCount(); i++) {
					list.add(i, fileList.getName(i));
					}
				map.put("fileList",list);
				}else{
				map.put("CODE", 0);
				map.put("data",hashMap);
				map.put("MESSAGE", "暂时没有数据");
			}
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("plugin-getpropertybyid程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> upload(String agentAndServer,Plugin plugin, MultipartFile[] file) throws Exception {
		String server=agentAndServer.substring(agentAndServer.lastIndexOf("@")+1);
		long startTime = System.currentTimeMillis();
		PluginClient pluginClient=new PluginClient(agentAndServer);
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap param_map = new HashMap();
		param_map.put("id", plugin.getPluginid());
		param_map.put("pluginName", plugin.getPluginName());
		param_map.put("pluginMemo", plugin.getPluginMemo());
		param_map.put("entryClass", plugin.getEntryClass());
		StringList fileList = new StringList();
		String path="";
		String str=plugin.getEntryClass();
		if (str.indexOf(".")!=-1){
			 path=str.substring(0, str.lastIndexOf(".")+1).replace(".", "/");
		}else{
			path="";
		}
		for (int i = 0; i < file.length; i++) {
			String ext = FileTools.extractFileExt(file[i].getOriginalFilename());
			if(ext.equalsIgnoreCase(".class")){
				pluginClient.addPluginFile(file[i].getInputStream(), server,path, file[i].getOriginalFilename() , 0, -1);
			}else if(ext.equalsIgnoreCase(".jar")){
				pluginClient.addPluginFile(file[i].getInputStream(), server,path, file[i].getOriginalFilename(), 1, -1);
			}else{
				pluginClient.addPluginFile(file[i].getInputStream(), server,path, file[i].getOriginalFilename(), 2, -1);
			}
			fileList.setValue(file[i].getOriginalFilename(), file[i].getOriginalFilename());
		}
		param_map.put("fileList", fileList.getAllText());
		log.info(param_map.toString());
		try {
			pluginClient.addPlugin(param_map);
			map.put("CODE",0);
			map.put("MESSAGE","成功");
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("plugin-upload程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	@Override
	public Map<String, Object> uploadedit(String agentAndServer,Plugin plugin, MultipartFile[] file) throws Exception {
		String server=agentAndServer.substring(agentAndServer.lastIndexOf("@")+1);
		long startTime = System.currentTimeMillis();
		PluginClient pluginClient=new PluginClient(agentAndServer);
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap param_map = new HashMap();
		param_map.put("id", plugin.getPluginid());
		param_map.put("pluginName", plugin.getPluginName());
		param_map.put("pluginMemo", plugin.getPluginMemo());
		param_map.put("entryClass", plugin.getEntryClass());
		StringList fileList = new StringList();
		String path="";
		String str=plugin.getEntryClass();
		if (str.indexOf(".")!=-1){
			 path=str.substring(0, str.lastIndexOf(".")+1).replace(".", "/");
		}else{
			path="";
		}
		for (int i = 0; i < file.length; i++) {
			if(file[i].getOriginalFilename().equals("")){}else{
				String ext = FileTools.extractFileExt(file[i].getOriginalFilename());
				if(ext.equalsIgnoreCase(".class")){
					pluginClient.addPluginFile(file[i].getInputStream(), server,path, file[i].getOriginalFilename(), 0, -1);
				}else if(ext.equalsIgnoreCase(".jar")){
					pluginClient.addPluginFile(file[i].getInputStream(), server,path, file[i].getOriginalFilename(), 1, -1);
				}else{
					pluginClient.addPluginFile(file[i].getInputStream(), server,path,file[i].getOriginalFilename(), 2, -1);
				}
				fileList.setValue(file[i].getOriginalFilename(), file[i].getOriginalFilename());
			}
		}
		param_map.put("fileList", fileList.getAllText());
		try {
			pluginClient.editPlugin(param_map);
			map.put("CODE",0);
			map.put("MESSAGE","成功");
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("plugin-uploadedit程序运行时间："+(endTime-startTime)+"ms");
		return map;
	}

	
}