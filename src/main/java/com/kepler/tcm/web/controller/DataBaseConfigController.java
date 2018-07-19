package com.kepler.tcm.web.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kepler.tcm.service.DataBaseConfigService;
import com.kepler.tcm.util.SequenceUtil;

@RestController
@RequestMapping(value="/dataBaseConfig")
public class DataBaseConfigController {

	private final Logger log = LoggerFactory.getLogger(DataBaseConfigController.class);
	
	@Autowired
	private DataBaseConfigService dataBaseConfigService;

	/**
	 * 测试连接
	 */
	@RequestMapping(value="/getConnection",method=RequestMethod.POST)
	public String getConnection(HttpServletRequest req) throws Exception{
		Map<String, String[]> parameterMap = req.getParameterMap();
		HashMap map = new HashMap<>();
		String agentAndServer = StringUtils.join(parameterMap.get("agentAndServer"));
		map.put("name", StringUtils.join(parameterMap.get("name")));
		map.put("driver", StringUtils.join(parameterMap.get("driver")));
		map.put("url", StringUtils.join(parameterMap.get("url")));
		map.put("user", StringUtils.join(parameterMap.get("user")));
		map.put("pass", StringUtils.join(parameterMap.get("pass")));
		return dataBaseConfigService.getConnection(agentAndServer,map);
	} 
	
	/**
	 * 添加
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public boolean add(HttpServletRequest req) throws Exception{
		Map<String, String[]> parameterMap = req.getParameterMap();
		HashMap map = new HashMap<>();
		String dbId = SequenceUtil.getUUID();
		String agentAndServer = StringUtils.join(parameterMap.get("agentAndServer"));
		map.put("id", dbId);
		map.put("name", StringUtils.join(parameterMap.get("name")));
		map.put("driver", StringUtils.join(parameterMap.get("driver")));
		map.put("url", StringUtils.join(parameterMap.get("url")));
		map.put("user", StringUtils.join(parameterMap.get("user")));
		map.put("pass", StringUtils.join(parameterMap.get("pass")));
		return dataBaseConfigService.add(agentAndServer,map);
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value="/modify",method=RequestMethod.POST)
	public boolean modify(HttpServletRequest req) throws Exception{
		Map<String, String[]> parameterMap = req.getParameterMap();
		HashMap map = new HashMap<>();
		String agentAndServer = StringUtils.join(parameterMap.get("agentAndServer"));
		String id = StringUtils.join(parameterMap.get("id"));
		map.put("name", StringUtils.join(parameterMap.get("name")));
		map.put("driver", StringUtils.join(parameterMap.get("driver")));
		map.put("url", StringUtils.join(parameterMap.get("url")));
		map.put("user", StringUtils.join(parameterMap.get("user")));
		map.put("pass", StringUtils.join(parameterMap.get("pass")));
		return dataBaseConfigService.modify(agentAndServer,id,map);
		
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/remove",method=RequestMethod.PUT)
	public boolean remove(String agentAndServer,String id) throws Exception{
		
		return dataBaseConfigService.remove(agentAndServer,id);
	}
	
	/**
	 * 分页-条件查询
	 */
	@RequestMapping(value="/pages",method=RequestMethod.GET)
	public Map pages(String agentAndServer,String name,int pageNum,int pageSize){
		return dataBaseConfigService.pages(agentAndServer,name,pageNum,pageSize);
	}
	
	/**
	 * 查询所有数据库信息
	 */
	@RequestMapping(value="/findAll",method=RequestMethod.GET)
	public List findAll(String agentAndServer){
		return dataBaseConfigService.findAll(agentAndServer);
	}
	
	
}
