package com.kepler.tcm.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.alibaba.fastjson.JSON;

/**
 * 缓存调用模板
 * @author 作者 E-mail: wangshuping@bonc.com.cn
 * @date 创建时间：2017年4月8日 下午9:51:45 
 * @version V-1.0.0
 */
public class CacheTemplate {
	
	private static Logger log = LoggerFactory.getLogger(CacheTemplate.class);
	
	/**
	 * spring 缓存管理
	 */
	private CacheManager cacheManager ;
	
	
    /**
     * 是否启用redis
     */
    @Value("${spring.redis.enabled:true}")
    private boolean redisEnabled ;
	
	public CacheTemplate(CacheManager cacheManager){
		 this.cacheManager = cacheManager ;
	}
	
	/**
	 * @throws Exception 
	 * 获取数据,Redis缓存
	 * @param cacheName 缓存名称
	 * @param key 缓存Key
	 * @param expire 过期时间，单位秒
	 * @param clazz 缓存类型 
	 * @param callback 获取数据回调类型
	 * @return T 缓存结果
	 * @throws
	 */
	public <T>  T findCache(String cacheName ,String key ,Long expire,
			Class<T> clazz,CacheCallback<T> callback ) throws Exception{
		
		T value = null ;
		
		if(cacheManager == null ){
			return  value ;
		}
		
		if(!redisEnabled){
			value  = findEhCache(cacheName ,key ,clazz ,callback );
		}else{
			value  = findRedisCache(cacheName ,key ,expire ,clazz ,callback );
		}
		return value ;
	}
	
	/**
	 * @throws Exception 
	 * 获取数据，Ehcache缓存
	 * @param cacheName 缓存名称
	 * @param key 缓存Key
	 * @param expire 过期时间，单位秒
	 * @param clazz 缓存类型 
	 * @param callback 获取数据回调类型
	 * @return T 缓存结果
	 * @throws
	 */
	public <T>  T findEhCache(String cacheName ,String key , Class<T> clazz ,
			CacheCallback<T> callback ) throws Exception{
		
		if(cacheManager == null ){
			return  null ;
		}
		
		Cache cache = cacheManager.getCache(cacheName);
		//获取缓存中数据
		T value = cache.get(key,clazz);
		log.debug("first get data from cache !!!");
		if(value == null){
			synchronized (CacheTemplate.class) {
				value = cache.get(key,clazz);
				log.debug("second get data from cache !!!");
				if(value == null){
					log.debug("get data from database !!!");
					value = callback.load();
					if(value != null){
						log.debug("put to cache , data from database !!!");
						cache.put(key, value);
					}
				}
			}
		}
		return value ;
	}
	
	/**
	 * @throws Exception 
	 * 获取数据,Redis缓存
	 * @param cacheName 缓存名称
	 * @param key 缓存Key
	 * @param expire 过期时间，单位秒
	 * @param clazz 缓存类型 
	 * @param callback 获取数据回调类型
	 * @return T 缓存结果
	 * @throws
	 */
	public <T>  T findRedisCache(String cacheName ,String key , Long expire ,
			 Class<T> clazz , CacheCallback<T> callback ) throws Exception{
		
		if(cacheManager == null ){
			return  null ;
		}
		//设置过期时间
		setKeyExpires(cacheName,expire);
		//获取缓存
		RedisCache cache = (RedisCache) cacheManager.getCache(cacheName);
		//获取缓存中数据
		Object cacheValue  = cache.get(key,clazz);
		T value = null ;
		if(cacheValue != null){
			log.debug("first get data from cache !!!");
			value = JSON.parseObject((String) cacheValue,clazz);
		}
		//ValueWrapper valueWrapper  = cache.get(key);
		//Object a = valueWrapper.get();
		
		if(value == null){
			synchronized (this) {
				value = cache.get(key,clazz);
				if(value != null){
					log.debug("second get data from cache !!!");
				}
				if(value == null){
					log.info("get data from database !!!");
					value = callback.load();
					if(value != null){
						log.debug("put to cache , data from database !!!");
						cache.put(key, JSON.toJSONString(value));
					}
				}
			}
		}
		return value ;
	}
	
	/**
	 * 清除数据，cache缓存
	 * @param cacheName 缓存名称
	 * @param key 缓存Key
	 * @throws
	 */
	public void evictCache(String cacheName ,String key){
		
		if(cacheManager == null || StringUtils.isEmpty(cacheName) 
				||  StringUtils.isEmpty(key)  ){
			return  ;
		}
		
		if(!redisEnabled){
			evictEhCache(cacheName ,key );
		}else{
			evictRedisCache(cacheName ,key);
		}
	}
	
	/**
	 * 清除数据，Ehcache缓存
	 * @param cacheName 缓存名称
	 * @param key 缓存Key
	 * @throws
	 */
	public void evictEhCache(String cacheName ,String key){
		
		if(cacheManager == null || StringUtils.isEmpty(cacheName) 
				||  StringUtils.isEmpty(key)){
			return ;
		}
		
		Cache cache = cacheManager.getCache(cacheName);
		//清除缓存
		 cache.evict(key);
		log.debug("clear  key = {}  data from cache !!",key);
	}
	
	/**
	 * 清除数据,Redis缓存
	 * @param cacheName 缓存名称
	 * @param key 缓存Key
	 * @throws
	 */
	public void  evictRedisCache(String cacheName ,String key){
		
		if(cacheManager == null || StringUtils.isEmpty(cacheName) 
				||  StringUtils.isEmpty(key)){
			return ;
		}
		
		Cache cache = cacheManager.getCache(cacheName);
		//清除缓存
		 cache.evict(key);
		log.debug("clear  key = {}  data from cache !!",key);
	}
	
	/**
	 * 清除数据，cache缓存
	 * @param cacheName 缓存名称
	 * @throws
	 */
	public void clearCache(String cacheName){
		
		if(cacheManager == null || StringUtils.isEmpty(cacheName)){
			return  ;
		}
		
		if(!redisEnabled){
			clearEhCache(cacheName);
		}else{
			clearRedisCache(cacheName);
		}
	}
	
	/**
	 * 清除数据，Ehcache缓存
	 * @param cacheName 缓存名称
	 * @throws
	 */
	public void clearEhCache(String cacheName){
		
		if(cacheManager == null || StringUtils.isEmpty(cacheName)){
			return ;
		}
		
		Cache cache = cacheManager.getCache(cacheName);
		//清除缓存
		cache.clear();;
		log.debug("clear  cacheName = {}  data from cache !!",cacheName);
	}
	
	/**
	 * 清除数据,Redis缓存
	 * @param cacheName 缓存名称
	 * @throws
	 */
	public void  clearRedisCache(String cacheName){
		
		if(cacheManager == null || StringUtils.isEmpty(cacheName)){
			return ;
		}
		
		Cache cache = cacheManager.getCache(cacheName);
		//清除缓存
		 cache.clear();;
		log.debug("clear  cacheName = {}  data from cache !!",cacheName);
	}
	
	
	/**
	 * 通过cacheName
	 * @param cacheName
	 * @param expire
	 */
	private void setCacheNameExpires(String cacheName ,long expire ){
		Map<String, Long> expires = new HashMap<String, Long>();
		expires.put(cacheName, expire);
		MixCacheManager mixCacheManager = (MixCacheManager)cacheManager;
		RedisCacheManager redisCacheManager = mixCacheManager.getRedisCacheManager();
		redisCacheManager.setExpires(expires); //设置value过期时间,单位秒
	}
	
	/**
	 * 通过key
	 * @param key
	 * @param expire
	 */
	private void setKeyExpires(String key ,long expire ){
		Map<String, Long> expires = new HashMap<String, Long>();
		expires.put(key, expire);
		MixCacheManager mixCacheManager = (MixCacheManager)cacheManager;
		RedisCacheManager redisCacheManager = mixCacheManager.getRedisCacheManager();
		redisCacheManager.setExpires(expires); //设置value过期时间,单位秒
	}
		
}
