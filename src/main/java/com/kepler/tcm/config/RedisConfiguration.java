package com.kepler.tcm.config;

import java.lang.reflect.Method;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.Jedis;
/**
 * Redis缓存
 * @author wangsp
 * @date 2017年4月6日
 * @version V1.0
 */
@Configuration
//标注启动了缓存
@EnableCaching
@ConditionalOnClass({ JedisConnection.class, RedisOperations.class, Jedis.class })
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration extends CachingConfigurerSupport {
	
	private static Logger log = LoggerFactory.getLogger(RedisConfiguration.class);
    
    @Bean
    //@Profile({"prd"})
    public RedisCacheManager redisCacheManager(RedisTemplate<Object, Object> redisTemplate) {  
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);  
  
        // Number of seconds before expiration. Defaults to unlimited (0)  
        //cacheManager.setDefaultExpiration(10); // Sets the default expire time (in seconds)
        cacheManager.setUsePrefix(true);
        //cacheManager.setDefaultExpiration(86400L);
        //添加缓存
        //List<String> cacheNames = new ArrayList<String>();
        //cacheManager.setCacheNames(cacheNames); 
        return cacheManager;  
    }
    
    /**
     * 覆盖默认模板
     * @param redisConnectionFactory
     * @return
     * @throws UnknownHostException
     */
    @Bean
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisTemplate<Object, Object> redisTemplate(
			RedisConnectionFactory redisConnectionFactory)
					throws UnknownHostException {
		RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
		template.setConnectionFactory(redisConnectionFactory);
		/*template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());*/
		return template;
	}
    
    
    
    
    
    @Bean
	@ConditionalOnMissingBean(StringRedisTemplate.class)
	public StringRedisTemplate stringRedisTemplate(
			RedisConnectionFactory redisConnectionFactory)
					throws UnknownHostException {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}
  
    
    
    /**
     * 生成key的策略
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {

			@Override
			public Object generate(Object target, Method method,
					Object... params) {
				StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
			}
        };
    }

}
