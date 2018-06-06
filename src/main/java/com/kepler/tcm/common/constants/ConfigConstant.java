package com.kepler.tcm.common.constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import com.kepler.tcm.util.EnvironmentUtils;


/**
 * 
 * @author wangsp
 * @date 2017年4月6日
 * @version V1.0
 */
@Component
@Order(1)
public class ConfigConstant implements ApplicationListener<ApplicationReadyEvent> {

	/**
	 * 日志记录
	 */
	public static Logger logger = LoggerFactory.getLogger(ConfigConstant.class);
	
	/**
	 * 存放配置信息的哈希表
	 */
	public static HashMap<String, Object> map = new HashMap<String, Object>();
	
	/**
	 * 根据配置名称取得配置信息 
	 * @param cfgName
	 *  配置名称
	 */
	public static String getValue(String cfgName) {
		if (null == map.get(cfgName)) {
			return "";
		}
		return (String) map.get(cfgName);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		
	}
	
	
	
}
