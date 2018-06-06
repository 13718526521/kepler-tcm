package com.kepler.tcm.cache;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
/**
 * 混合缓存配置
 * @author wangsp
 * @date 2017年4月7日
 * @version V1.0
 */
public class MixCacheManager implements CacheManager {

	/**
	 * redis缓存管理
	 */
	private RedisCacheManager redisCacheManager;
	
	/**
	 * ehcache缓存管理
	 */
    private CacheManager ehCacheManager;
    
    /**
     * 缓存前缀
     */
    private static String REDIS_PREFIX = "redis-" ;
    
    
    /**
     * 是否启用redis
     */
    @Value("${spring.redis.enabled:true}")
    private boolean redisEnabled ;
   
    public Cache getCache(String cache) {
    	
    	/*if(cache.startsWith(REDIS_PREFIX)){
    		return redisCacheManager.getCache(cache);  
    	}else{
    		return ehCacheManager.getCache(cache);  
    	}*/
    	
    	if(redisEnabled){
    		return redisCacheManager.getCache(cache);  
    	}else{
    		return ehCacheManager.getCache(cache);  
    	}
    	
    }  
  
    public Collection<String> getCacheNames() {  
        Collection<String> cacheNames = new ArrayList<String>();          
        if (redisCacheManager != null) {  
            cacheNames.addAll(redisCacheManager.getCacheNames());  
        }  
        if (ehCacheManager != null) {  
            cacheNames.addAll(ehCacheManager.getCacheNames());  
        }         
        return cacheNames;  
    }

	public RedisCacheManager getRedisCacheManager() {
		return redisCacheManager;
	}

	public void setRedisCacheManager(RedisCacheManager redisCacheManager) {
		this.redisCacheManager = redisCacheManager;
	}

	public CacheManager getEhCacheManager() {
		return ehCacheManager;
	}

	public void setEhCacheManager(CacheManager ehCacheManager) {
		this.ehCacheManager = ehCacheManager;
	}
    
}
