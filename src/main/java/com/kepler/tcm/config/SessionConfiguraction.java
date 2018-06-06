package com.kepler.tcm.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


/**
 * redis 实现分布式session共享 
 * @author wangsp
 * @date 2017年4月10日
 * @version V1.0
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60*60) //default 1800
@ConditionalOnProperty(prefix = "spring.redis", name = "enabled", havingValue = "true")
public class SessionConfiguraction {
	
	private static Logger log = LoggerFactory.getLogger(SessionConfiguraction.class);
	
	public SessionConfiguraction(){
		log.info("redis 实现分布式session共享初始化... ");
	}

}