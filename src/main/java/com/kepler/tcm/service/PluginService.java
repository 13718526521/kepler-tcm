package com.kepler.tcm.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.kepler.tcm.domain.Plugin;

public interface PluginService {		
	
	Map query(String agentAndServer) throws Exception;
	
	Map querydetail(String agentAndServer) throws Exception;
	
	Map<String, Object> add(String agentAndServer,String id,String pluginName,String pluginMemo,String entryClass) throws Exception;

	Map<String, Object> edit(String agentAndServer,String id,String pluginName,String pluginMemo,String entryClass) throws Exception;

	Map<String, Object> delete(String agentAndServer,String id) throws Exception;

	Map<String, Object> reload(String agentAndServer,String id) throws Exception;

	Map<String, Object> getpropertybyid(String agentAndServer,String id) throws Exception;
	
	Map<String, Object> upload(String agentAndServer,Plugin plugin, MultipartFile[] file) throws Exception;
	
	Map<String, Object> uploadedit(String agentAndServer,Plugin plugin, MultipartFile[] file) throws Exception;

}
