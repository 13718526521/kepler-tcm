package com.kepler.tcm.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;

import com.kepler.tcm.cache.CacheTemplate;
import com.kepler.tcm.cache.MixCacheManager;
/**
 * 多缓存配置
 * @author wangsp
 * @date 2017年4月11日
 * @version V1.0
 */
@Configuration
//标注启动了缓存
@EnableCaching
@ConditionalOnBean({RedisConfiguration.class ,EhCacheConfiguration.class })
public class CacheConfiguration {
	
	/**
	 * 混合缓存管理类
	 * @param redisCacheManager
	 * @param ehCacheCacheManager
	 * @return
	 */
    @Bean
    @Primary
	public CacheManager cacheManager(RedisCacheManager redisCacheManager ,EhCacheCacheManager ehCacheCacheManager) {  
	    MixCacheManager cacheManager = new MixCacheManager();  
	    cacheManager.setRedisCacheManager(redisCacheManager);  
	    cacheManager.setEhCacheManager(ehCacheCacheManager);  
	    return cacheManager;  
	}
    
    /**
     * 同步缓存模板
     * @param ehCacheCacheManager
     * @return
     */
    @Bean
    //@Primary
    //@Profile({"dev","test"})
    public CacheTemplate cacheTemplate(CacheManager cacheManager ){
    	
    	return new CacheTemplate(cacheManager);
    }
    
    /*@Bean
    @Primary
    //@Profile("prd")
    public CacheTemplate cacheTemplate(RedisCacheManager redisCacheManager){
    	return new CacheTemplate(redisCacheManager);
    }*/
}
