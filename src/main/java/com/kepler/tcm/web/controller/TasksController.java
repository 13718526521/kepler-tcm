package com.kepler.tcm.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kepler.tcm.service.TasksService;
import com.kepler.tcm.util.SequenceUtil;

@RestController
@RequestMapping(value="/tasks")
public class TasksController {

	private final Logger log = LoggerFactory.getLogger(TasksController.class);
	
	@Autowired
	private TasksService tasksService;
	
	/**
	 * 添加
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public void add(String agentAndServer,HashMap map) throws Exception{
		String taskId = SequenceUtil.getUUID();
		map.put("taskId", taskId);
		tasksService.add(agentAndServer,map);
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public void edit(String agentAndServer,HashMap map) throws Exception{
		tasksService.editTask(agentAndServer,map);
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/remove",method=RequestMethod.POST)
	public void remove(String agentAndServer,String taskId) throws Exception{
		
		tasksService.remove(agentAndServer,taskId);
	}
	
	/**
	 * 获取任务列表
	 */
	@RequestMapping(value="/pages",method=RequestMethod.GET)
	public HashMap pages(String agentAndServer,String name,int pageNum,int pageSize){
		return tasksService.pages(agentAndServer,name,pageNum,pageSize);
	}
	
	/**
	 * 启动任务
	 */
	@RequestMapping(value="/startTask",method=RequestMethod.POST)
	public void startTask(String agentAndServer,String taskId) throws Exception{
		tasksService.startTask(agentAndServer,taskId);
	}
	
	/**
	 * 停止任务
	 */
	@RequestMapping(value="/stopTask",method=RequestMethod.POST)
	public void stopTask(String agentAndServer,String taskId) throws Exception{
		tasksService.stopTask(agentAndServer,taskId);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
