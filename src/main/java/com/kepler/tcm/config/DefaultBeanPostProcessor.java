package com.kepler.tcm.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class DefaultBeanPostProcessor implements BeanPostProcessor {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultBeanPostProcessor.class);

	public DefaultBeanPostProcessor() {  
		super();  
		log.debug("实现类构造器！！加载的bean......");       
    }  
  
    @Override  
    public Object postProcessAfterInitialization(Object bean, String arg1)  
            throws BeansException {  
    	log.debug("beanName : {}",arg1); 
        return bean;  
    }  
  
    @Override  
    public Object postProcessBeforeInitialization(Object bean, String arg1)  
            throws BeansException {  
        //System.out.println("bean处理器：bean创建之前..");  
    	log.debug("bean处理器：bean创建之前......");   
    	return bean;  
    }

}
