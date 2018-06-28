package com.kepler.tcm.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public boolean getConnection(String agentAndServer,HashMap map) throws Exception{
		return dataBaseConfigService.getConnection(agentAndServer,map);
	} 
	
	/**
	 * 添加
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public boolean add(String agentAndServer,HashMap map) throws Exception{
		String dbId = SequenceUtil.getUUID();
		map.put("dbId", dbId);
		return dataBaseConfigService.add(agentAndServer,map);
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value="/modify",method=RequestMethod.POST)
	public boolean modify(String agentAndServer,String dbId,HashMap map) throws Exception{
		return dataBaseConfigService.modify(agentAndServer,dbId,map);
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/remove",method=RequestMethod.POST)
	public boolean remove(String agentAndServer,String dbId) throws Exception{
		
		return dataBaseConfigService.remove(agentAndServer,dbId);
	}
	
	/**
	 * 分页-条件查询
	 */
	@RequestMapping(value="/pages",method=RequestMethod.GET)
	public Map pages(String agentAndServer,String name,int pageNum,int pageSize){
		return dataBaseConfigService.pages(agentAndServer,name,pageNum,pageSize);
	}
}
