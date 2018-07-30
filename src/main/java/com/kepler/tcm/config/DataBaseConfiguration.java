package com.kepler.tcm.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 数据源自定义配置 //http://www.cnblogs.com/java-zhao/p/5413845.html
 * @author wangsp
 * @date 2017年3月20日
 * @version V1.0
 */
//@Configuration
//@EnableTransactionManagement
public class DataBaseConfiguration implements EnvironmentAware{

	 private RelaxedPropertyResolver propertyResolver;  
	  
	 private static Logger log = LoggerFactory.getLogger(DataBaseConfiguration.class);
	 
	
	 
	 @Override  
	 public void setEnvironment(Environment env) {  
		 this.propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");  
	 }  
	 
	 @Bean(name="dataSource", destroyMethod = "close", initMethod="init")  
	 public DataSource dataSource() {  
		 log.debug("Configruing Base DataSource");  
          
		 DruidDataSource datasource = new DruidDataSource();  
		 datasource.setUrl(propertyResolver.getProperty("url"));  
		 datasource.setDriverClassName(propertyResolver.getProperty("driverClassName"));  
		 datasource.setUsername(propertyResolver.getProperty("username"));  
		 datasource.setPassword(propertyResolver.getProperty("password"));
		 datasource.setInitialSize(Integer.valueOf(propertyResolver.getProperty("initialSize")));
		 datasource.setMaxActive(Integer.valueOf(propertyResolver.getProperty("maxActive")));
		 datasource.setMinIdle(Integer.valueOf(propertyResolver.getProperty("minIdle")));
		 datasource.setMaxWait(Integer.valueOf(propertyResolver.getProperty("maxWait")));
		 datasource.setValidationQuery(propertyResolver.getProperty("validationQuery"));
		 return datasource;  
	 }  
	 
	 //-------------------------------------------------------------------------
  
/*	 @Bean(name="writeDataSource", destroyMethod = "close", initMethod="init")  
	 @Primary  
	 public DataSource writeDataSource() {  
		 log.debug("Configruing Write DataSource");  
          
		 DruidDataSource datasource = new DruidDataSource();  
		 datasource.setUrl(propertyResolver.getProperty("url"));  
		 datasource.setDriverClassName(propertyResolver.getProperty("driverClassName"));  
		 datasource.setUsername(propertyResolver.getProperty("username"));  
		 datasource.setPassword(propertyResolver.getProperty("password"));  
          
		 return datasource;  
	 }  
      
	 @Bean(name="readOneDataSource", destroyMethod = "close", initMethod="init")  
	 public DataSource readOneDataSource() {  
		 log.debug("Configruing Read One DataSource");  
          
		 DruidDataSource datasource = new DruidDataSource();  
		 datasource.setUrl(propertyResolver.getProperty("url"));  
		 datasource.setDriverClassName(propertyResolver.getProperty("driverClassName"));  
		 datasource.setUsername(propertyResolver.getProperty("username"));  
		 datasource.setPassword(propertyResolver.getProperty("password"));  
          
		 return datasource;  
	 }  
      
	 @Bean(name="readTowDataSource", destroyMethod = "close", initMethod="init")  
	 public DataSource readTowDataSource() {  
		 log.debug("Configruing Read Two DataSource");  
          
		 DruidDataSource datasource = new DruidDataSource();  
		 datasource.setUrl(propertyResolver.getProperty("url"));  
		 datasource.setDriverClassName(propertyResolver.getProperty("driverClassName"));  
		 datasource.setUsername(propertyResolver.getProperty("username"));  
		 datasource.setPassword(propertyResolver.getProperty("password"));  
          
		 return datasource;  
	 }  
      
      
	 @Bean(name="readDataSources")  
	 public List<DataSource> readDataSources(){  
		 List<DataSource> dataSources = new ArrayList<DataSource>();  
		 dataSources.add(readOneDataSource());  
		 dataSources.add(readTowDataSource());  
		 return dataSources;  
	 }*/
}
