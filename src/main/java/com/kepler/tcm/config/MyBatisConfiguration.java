package com.kepler.tcm.config;

import java.io.IOException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 * Mybatis自定义配置
 * @author wangsp
 * @date 2017年3月20日
 * @version V1.0
 */
@Configuration  
@ConditionalOnClass({ EnableTransactionManagement.class})  
@AutoConfigureAfter({ DataBaseConfiguration.class })  
public class MyBatisConfiguration implements EnvironmentAware {
	
	private static Logger log = LoggerFactory.getLogger(MyBatisConfiguration.class);  
	  
    private RelaxedPropertyResolver propertyResolver;
    
    @Resource(name="dataSource")  
    private DataSource dataSource;  
    
    
    /*@Resource(name="writeDataSource")  
    private DataSource writeDataSource;  
      
    @Resource(name="readDataSources")  
    private List<Object> readDataSources;*/
       
    /**
     * 获取环境变量
     */
    @Override  
    public void setEnvironment(Environment environment) {  
        this.propertyResolver = new RelaxedPropertyResolver(environment,"spring.mybatis.");  
    }  
    
    
    @Bean  
    @ConditionalOnMissingBean  
    public SqlSessionFactory sqlSessionFactory() {  
        try {
        	
            SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();  
            sessionFactory.setDataSource(dataSource);  
            
            //分页插件  
           /* PageHelper pageHelper = new PageHelper();  
            Properties props = new Properties();  
            props.setProperty("reasonable", "true");  
            props.setProperty("supportMethodsArguments", "true");  
            props.setProperty("returnPageInfo", "check");  
            props.setProperty("params", "count=countSql");  
            pageHelper.setProperties(props);*/
            
            //添加插件  
            /*sessionFactory.setPlugins(new Interceptor[]{pageHelper});*/
            
            //sessionFactory.setTypeAliasesPackage(propertyResolver.getProperty("typeAliasesPackage")); 
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(); 
            sessionFactory.setConfigLocation(resolver.getResource(propertyResolver.getProperty("configLocation"))); 
            sessionFactory.setMapperLocations(resolver.getResources(propertyResolver.getProperty("mapperLocations")));  
  
            return sessionFactory.getObject();
            
        } catch (IOException e) {
        	e.printStackTrace();
            log.error("异常信息：{}",e.getMessage());  
        } catch (Exception e) {
        	e.printStackTrace();
            log.error("异常信息：{}",e.getMessage());  
        }
        
        return null;
    }  
    
    /**
     * Mybatis  sqlSessionFactory
     * @param sqlSessionFactory
     * @return
     */
    @Bean  
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {  
        return new SqlSessionTemplate(sqlSessionFactory());  
    }  
    
    /**
     * 开启数据库事务
     * @return
     */
    @Bean  
    @ConditionalOnMissingBean  
    public DataSourceTransactionManager transactionManager() {  
        return new DataSourceTransactionManager(dataSource);  
    }
    
    //-----------------------多数据源配置------------------------------------------
      
/*  @Bean  
    public RoundRobinRWRoutingDataSourceProxy roundRobinDataSouceProxy(){  
        RoundRobinRWRoutingDataSourceProxy proxy = new RoundRobinRWRoutingDataSourceProxy();  
        proxy.setWriteDataSource(writeDataSource);  
        proxy.setReadDataSoures(readDataSources);  
        proxy.setReadKey("READ");  
        proxy.setWriteKey("WRITE");  
          
        return proxy;  
    } */ 
    
    
    /*@Bean  
    @ConditionalOnMissingBean  
    public DataSourceTransactionManager transactionManager() {  
        return new DataSourceTransactionManager(writeDataSource);  
    }*/
 
}
