package com.kepler.tcm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
			Boolean flag=pluginClient.reloadPlugin(id);
			if(flag == true){
				map.put("CODE", 0);
				map.put("MESSAGE", "成功");
			}else{
				map.put("CODE", 1);
				map.put("MESSAGE", "重载失败");
			}
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
					Map file_map = new HashMap<>();
					file_map.put("Path",  fileList.getName(i));
					file_map.put("File",  fileList.getValue(i));
					list.add(i, file_map);
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
	public Map<String, Object> upload(String agentAndServer,Plugin plugin, MultipartFile[] file,String[] className,String auto_plugin_id) throws Exception {
		long startTime = System.currentTimeMillis();
		String server=agentAndServer.substring(agentAndServer.lastIndexOf("@")+1);
		PluginClient pluginClient=new PluginClient(agentAndServer);
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap param_map = new HashMap();
		String pluginId="";
		if(auto_plugin_id.equals("0")) {
			param_map.put("id", plugin.getEntryClass());
			pluginId=plugin.getEntryClass();
		}else{
			param_map.put("id", plugin.getPluginid());
			pluginId=plugin.getPluginid();
		}
		param_map.put("pluginName", plugin.getPluginName());
		param_map.put("pluginMemo", plugin.getPluginMemo());
		param_map.put("entryClass", plugin.getEntryClass());
		StringList fileList = new StringList();
		String class_path="";
		String e_class=plugin.getEntryClass();
		if (e_class.indexOf(".")!=-1){
			class_path=e_class.substring(0, e_class.lastIndexOf(".")+1).replace(".", "/");
		}else{
			class_path="";
		}
		for (int i = 0; i < file.length; i++) {
			String path="";
			String ext = FileTools.extractFileExt(file[i].getOriginalFilename());
			if(ext.equalsIgnoreCase(".class")){
				path=file[i].getOriginalFilename();
				pluginClient.addPluginFile(file[i].getInputStream(), server,pluginId, class_path+file[i].getOriginalFilename() , -1);
			}else if(ext.equalsIgnoreCase(".jar")){
				path=file[i].getOriginalFilename();
				pluginClient.addPluginFile(file[i].getInputStream(), server,pluginId, file[i].getOriginalFilename() , -1);
			}else{
				String className_str=className[i];
				String className_path;
				if (className_str.indexOf("/")!=-1){
					className_path=className_str.substring(0, className_str.lastIndexOf("/")+1);
				}else{
					className_path="";
				}
				path=className_str;
				pluginClient.addPluginFile(file[i].getInputStream(), server,pluginId, className_path+file[i].getOriginalFilename() , -1);
			}
			fileList.setValue(path, file[i].getOriginalFilename());
		}
		param_map.put("fileList", fileList.getAllText());
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
	public Map<String, Object> uploadedit(String agentAndServer,Plugin plugin, MultipartFile[] file,String[] className,String[] fileArray) throws Exception {
		long startTime = System.currentTimeMillis();
		String server=agentAndServer.substring(agentAndServer.lastIndexOf("@")+1);
		PluginClient pluginClient=new PluginClient(agentAndServer);
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap param_map = new HashMap();
		param_map.put("id", plugin.getPluginid());
		param_map.put("pluginName", plugin.getPluginName());
		param_map.put("pluginMemo", plugin.getPluginMemo());
		param_map.put("entryClass", plugin.getEntryClass());
		StringList fileList = new StringList();
		String class_path="";
		String e_class=plugin.getEntryClass();
		if (e_class.indexOf(".")!=-1){
			class_path=e_class.substring(0, e_class.lastIndexOf(".")+1).replace(".", "/");
		}else{
			class_path="";
		}
		HashMap hashMap=pluginClient.getPluginPropertyById(plugin.getPluginid());
		fileList.setAllText((String) hashMap.get("fileList"));
		List<String> del_list = new ArrayList<String>();
		String[] file_array=fileArray;
		String[] filelist_array = new String[fileList.getCount()];
		for (int i = 0; i < fileList.getCount(); i++) {
			filelist_array[i]=fileList.getName(i);
		}
		for (String file_list : filelist_array) {
			if (!ArrayUtils.contains(file_array, file_list)) {
	            	del_list.add(file_list);
	            }
	        }
		for (int i = 0; i < del_list.size(); i++) {
			fileList.removeByName(del_list.get(i));
		}
		for (int i = 0; i < file.length; i++) {
			if(!("").equals(file[i].getOriginalFilename())){
				String path="";
				String ext = FileTools.extractFileExt(file[i].getOriginalFilename());
				if(ext.equalsIgnoreCase(".class")){
					path=file[i].getOriginalFilename();
					pluginClient.addPluginFile(file[i].getInputStream(), server,plugin.getPluginid(), class_path+file[i].getOriginalFilename() , -1);
				}else if(ext.equalsIgnoreCase(".jar")){
					path=file[i].getOriginalFilename();
					pluginClient.addPluginFile(file[i].getInputStream(), server,plugin.getPluginid(), file[i].getOriginalFilename() , -1);
				}else{
					String className_str=className[i];
					String className_path;
					if (className_str.indexOf("/")!=-1){
						className_path=className_str.substring(0, className_str.lastIndexOf("/")+1);
					}else{
						className_path="";
					}
					path=className_str;
					pluginClient.addPluginFile(file[i].getInputStream(), server,plugin.getPluginid(), className_path+file[i].getOriginalFilename() , -1);
				}
				fileList.setValue(path, file[i].getOriginalFilename());
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

	public Map querypage(String agentAndServer, String pluginName,int pageNum, int pageSize) throws Exception {
		long startTime = System.currentTimeMillis();
		HashMap<String, Object> map = new HashMap<String, Object>();
		PluginClient pluginClient=new PluginClient(agentAndServer);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> query_list = new ArrayList<Map<String, Object>>();
		try {
		String[][] array=pluginClient.getPlugins();
		if(array == null || array.length == 0){
			map.put("CODE",0);
			map.put("MESSAGE","暂时没有数据");
			map.put("data", list);
			return map;
		}
		if(StringUtils.isNoneBlank(pluginName)) {
			for (int i = 0; i < array.length;i++) {
				String plugin_name=array[i][1];
				if(plugin_name.indexOf(pluginName)>-1){
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
			}
			map.put("CODE", 0);
			map.put("MESSAGE", "成功");
			map.put("data", list);
			int totalPages=pageSize==0 ? 1 :(int) Math.ceil((double) list.size() / (double) pageSize);
			map.put("totalCount", list.size());
			map.put("totalPages", totalPages);
			return map;
		}
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
		int length = 0;
		if((pageNum * pageSize + pageSize) <= list.size()) {
			length = pageNum * pageSize + pageSize;
		}else {
			length = list.size();
		}
		for(int i= pageNum * pageSize; i<length; i++) {
			query_list.add(list.get(i));
		}
		int totalPages=pageSize==0 ? 1 :(int) Math.ceil((double) list.size() / (double) pageSize);
		map.put("CODE", 0);
		map.put("MESSAGE", "成功");
		map.put("data", query_list);
		map.put("totalCount", list.size());
		map.put("totalPages", totalPages);
		return map;
		} catch (Exception e) {
			map.put("CODE", 1);
			map.put("MESSAGE", e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("plugin-querypage程序运行时间："+(endTime-startTime)+"ms");
		return map;
		}

	
}