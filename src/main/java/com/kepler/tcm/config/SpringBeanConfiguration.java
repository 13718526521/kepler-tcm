package com.kepler.tcm.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Spirng bean 配置类
 * @author wangsp
 * @date 2017年3月21日
 * @version V1.0
 */
@Configuration
public class SpringBeanConfiguration {

	private static final Logger log = LoggerFactory.getLogger(SpringBeanConfiguration.class);
	
	/*@Bean
	public DefaultBeanPostProcessor defaultBeanPostProcessor(){
		return new DefaultBeanPostProcessor();
	}*/
	@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
