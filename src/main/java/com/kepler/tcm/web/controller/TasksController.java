package com.kepler.tcm.web.controller;

import java.util.Enumeration;
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

import com.kepler.tcm.core.task.RemoteTask;
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
	public void add(HttpServletRequest req) throws Exception{
		Map<String, String[]> parameterMap = req.getParameterMap();
		HashMap map = new HashMap<>();
		String agentAndServer = StringUtils.join(parameterMap.get("agentAndServer"));
		map.put("taskName", StringUtils.join(parameterMap.get("taskName")));
		map.put("disabled", StringUtils.join(parameterMap.get("disabled")));
		map.put("autoRun", StringUtils.join(parameterMap.get("autoRun")));
		map.put("pluginId", StringUtils.join(parameterMap.get("pluginId")));
		map.put("databaseId", StringUtils.join(parameterMap.get("databaseId")));
		map.put("standbyDatabaseId", StringUtils.join(parameterMap.get("standbyDatabaseId")));
		map.put("planType", StringUtils.join(parameterMap.get("planType")));
		map.put("year", StringUtils.join(parameterMap.get("year")));
		map.put("month", StringUtils.join(parameterMap.get("month")));
		map.put("day", StringUtils.join(parameterMap.get("day")));
		map.put("hour", StringUtils.join(parameterMap.get("hour")));
		map.put("minute", StringUtils.join(parameterMap.get("minute")));
		map.put("second", StringUtils.join(parameterMap.get("second")));
		map.put("mxf", StringUtils.join(parameterMap.get("mxf")));
		map.put("mxt", StringUtils.join(parameterMap.get("mxt")));
		map.put("hour4", StringUtils.join(parameterMap.get("hour4")));
		map.put("minute4", StringUtils.join(parameterMap.get("minute4")));
		map.put("second4", StringUtils.join(parameterMap.get("second4")));
		map.put("cron", StringUtils.join(parameterMap.get("cron")));
		map.put("logType", StringUtils.join(parameterMap.get("logType")));
		map.put("logLevel", StringUtils.join(parameterMap.get("logLevel")));
		map.put("logLevel2", StringUtils.join(parameterMap.get("logLevel2")));
		map.put("taskTimeout", StringUtils.join(parameterMap.get("taskTimeout")));
		map.put("logBackNums", StringUtils.join(parameterMap.get("logBackNums")));
		map.put("logMaxSize", StringUtils.join(parameterMap.get("logMaxSize")));
		map.put("taskAlert", StringUtils.join(parameterMap.get("taskAlert")));
		map.put("alertType", StringUtils.join(parameterMap.get("alertType")));
		map.put("keepAlertTime", StringUtils.join(parameterMap.get("keepAlertTime")));
		map.put("notSuccAlert", StringUtils.join(parameterMap.get("notSuccAlert")));
		map.put("notSuccTime", StringUtils.join(parameterMap.get("notSuccTime")));
		map.put("failAlert", StringUtils.join(parameterMap.get("failAlert")));
		tasksService.add(agentAndServer,map);
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public void edit(HttpServletRequest req) throws Exception{
		Map<String, String[]> parameterMap = req.getParameterMap();
		HashMap map = new HashMap<>();
		String agentAndServer = StringUtils.join(parameterMap.get("agentAndServer"));
		map.put("taskID", StringUtils.join(parameterMap.get("taskID")));
		map.put("taskName", StringUtils.join(parameterMap.get("taskName")));
		map.put("disabled", StringUtils.join(parameterMap.get("disabled")));
		map.put("autoRun", StringUtils.join(parameterMap.get("autoRun")));
		map.put("pluginId", StringUtils.join(parameterMap.get("pluginId")));
		map.put("databaseId", StringUtils.join(parameterMap.get("databaseId")));
		map.put("standbyDatabaseId", StringUtils.join(parameterMap.get("standbyDatabaseId")));
		map.put("planType", StringUtils.join(parameterMap.get("planType")));
		map.put("year", StringUtils.join(parameterMap.get("year")));
		map.put("month", StringUtils.join(parameterMap.get("month")));
		map.put("day", StringUtils.join(parameterMap.get("day")));
		map.put("hour", StringUtils.join(parameterMap.get("hour")));
		map.put("minute", StringUtils.join(parameterMap.get("minute")));
		map.put("second", StringUtils.join(parameterMap.get("second")));
		map.put("mxf", StringUtils.join(parameterMap.get("mxf")));
		map.put("mxt", StringUtils.join(parameterMap.get("mxt")));
		map.put("hour4", StringUtils.join(parameterMap.get("hour4")));
		map.put("minute4", StringUtils.join(parameterMap.get("minute4")));
		map.put("second4", StringUtils.join(parameterMap.get("second4")));
		map.put("cron", StringUtils.join(parameterMap.get("cron")));
		map.put("logType", StringUtils.join(parameterMap.get("logType")));
		map.put("logLevel", StringUtils.join(parameterMap.get("logLevel")));
		map.put("logLevel2", StringUtils.join(parameterMap.get("logLevel2")));
		map.put("taskTimeout", StringUtils.join(parameterMap.get("taskTimeout")));
		map.put("logBackNums", StringUtils.join(parameterMap.get("logBackNums")));
		map.put("logMaxSize", StringUtils.join(parameterMap.get("logMaxSize")));
		map.put("taskAlert", StringUtils.join(parameterMap.get("taskAlert")));
		map.put("alertType", StringUtils.join(parameterMap.get("alertType")));
		map.put("keepAlertTime", StringUtils.join(parameterMap.get("keepAlertTime")));
		map.put("notSuccAlert", StringUtils.join(parameterMap.get("notSuccAlert")));
		map.put("notSuccTime", StringUtils.join(parameterMap.get("notSuccTime")));
		map.put("failAlert", StringUtils.join(parameterMap.get("failAlert")));
		tasksService.editTask(agentAndServer,map);
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/remove",method=RequestMethod.POST)
	public int remove(String agentAndServer,String taskId){
		
		try {
			tasksService.remove(agentAndServer,taskId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
		return 0;
	}
	
	/**
	 * 获取任务列表 分页
	 */
	@RequestMapping(value="/pages",method=RequestMethod.GET)
	public Map pages(String agentAndServer,int pageNum,int pageSize){
		return tasksService.pages(agentAndServer,pageNum,pageSize);
	}
	
	/**
	 * 获取任务列表 所有 不分页
	 */
	@RequestMapping(value="/findAll",method=RequestMethod.GET)
	public List findAll(String agentAndServer) throws Exception{
		return tasksService.findAll(agentAndServer);
	}
	
	/**
	 * 启动任务
	 */
	@RequestMapping(value="/startTask",method=RequestMethod.POST)
	public int startTask(String agentAndServer,String taskId){
		try {
			tasksService.startTask(agentAndServer,taskId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
		return 0;
	}
	
	/**
	 * 停止任务
	 */
	@RequestMapping(value="/stopTask",method=RequestMethod.POST)
	public int stopTask(String agentAndServer,String taskId){
		try {
			tasksService.stopTask(agentAndServer,taskId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
		return 0;
	}
	
	/**
	 * 判断任务是否启动成功
	 */
	@RequestMapping(value="/isTaskStarted",method=RequestMethod.POST)
	public boolean isTaskStarted(String agentAndServer,String taskId){
		return tasksService.isTaskStarted(agentAndServer,taskId);
	}
	
	/**
	 * 获取任务
	 */
	@RequestMapping(value="/getTask",method=RequestMethod.GET)
	public RemoteTask getTask(String agentAndServer,String taskId) throws Exception{
		return tasksService.getTask(agentAndServer,taskId);
	}
	
	/**
	 * 分页
	 */
	@RequestMapping(value="/getTaskLog",method=RequestMethod.GET)
	public Map<String,Object> getTaskLog(String agentAndServer,String types,String taskId,int pageNo,int pageNum,int pageSize) throws Exception{
		return tasksService.getTaskLog(agentAndServer,types,taskId,pageNo,pageNum,pageSize);
	}
	
	
	/**
	 * 保存参数
	 */
	@RequestMapping(value="/saveConfigProperty",method=RequestMethod.POST)
	public int saveConfigProperty(HttpServletRequest req){
		Map<String, String[]> parameterMap = req.getParameterMap();
		HashMap map = new HashMap<>();
		String agentAndServer = StringUtils.join(parameterMap.get("agentAndServer"));
		map.put("taskId", StringUtils.join(parameterMap.get("taskId")));
		//获取所有请求参数
		Enumeration et = req.getParameterNames();
		while (et.hasMoreElements()){
			String name = (String) et.nextElement();
			if (name.startsWith("param_")){
				map.put(name.substring(6), req.getParameter(name));
			}
		}
		try {
			tasksService.saveConfigProperty(agentAndServer, map);
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		return 0;
	}
	
	
	/**
	 * 获取参数
	 */
	@RequestMapping(value="/getTaskConfig",method=RequestMethod.GET)
	public String[][] getTaskConfig(String agentAndServer,String taskId){
		String[][] taskConfig = null;
		try {
			taskConfig = tasksService.getTaskConfig(agentAndServer, taskId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taskConfig;
	}
	
	
}
