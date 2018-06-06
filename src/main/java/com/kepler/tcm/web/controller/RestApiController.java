package com.kepler.tcm.web.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kepler.tcm.service.RestApiService;
/**
 * API接口控制器
 * @author wangsp
 * @date 2017年4月24日
 * @version V1.0
 */
@RestController
@RequestMapping(value = "/rest")
public class RestApiController {
	
	
	//rest接口service注入
	@Autowired
	private RestApiService restApiService;
	

	private static final Logger log  = LoggerFactory.getLogger(RestApiController.class);
	/**
	 * restapi接口
	 * @return
	 */
	@ApiOperation(value="RESTAPI接口",notes="对外接口信息")
	@ApiImplicitParams({
        @ApiImplicitParam(paramType="path",name = "code", value = "接口唯一标识code", required = true, dataType = "Sting")
	})
	@RequestMapping(value = "/api/{code}",method = RequestMethod.GET)
	public Map<String,Object> queryData(@PathVariable("code") String code,HttpServletRequest request,
			HttpServletResponse response){
		//获取1970年至今的秒值
		Long startTime = System.currentTimeMillis()/1000;
		log.debug("请求开始时间秒值：=====："+startTime);
		//获取参数注意 map中的value是string数组类型
		Map paramMap = request.getParameterMap();
		//转换成普通的map
		Map map = new HashMap<>();
		Set keys = paramMap.keySet();
		for(Object key: keys.toArray()) {
			map.put(key.toString(),((String[]) paramMap.get(key))[0]);
		}
		Map<String,Object> resultMap = restApiService.queryData(code,map);
		//查询完之后获取至今的秒值
		Long endTime = System.currentTimeMillis()/1000;
		log.debug("请求结束时间秒值：=====："+endTime);
		//接口访问的时长(秒)
		int total = (int)(endTime - startTime);
		log.debug("请求总时间秒值：=====："+total);
		log.debug("请求总时间分钟：=====："+total/60);
		return resultMap;//return restApiService.queryData(code,tempParamMap);
	}

}
