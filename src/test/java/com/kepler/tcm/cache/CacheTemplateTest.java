package com.kepler.tcm.cache;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import com.alibaba.fastjson.JSON;
import com.kepler.tcm.common.response.WebApiResponse;
import com.kepler.tcm.dao.SysUserMapper;
import com.kepler.tcm.domain.SysUser;
import com.kepler.tcm.util.DefaultBeanUtils;

public class CacheTemplateTest extends BaseCacheTest {

	@Autowired
	SysUserMapper sysUserMapper ;
	
	@Autowired
	EhCacheCacheManager ehCacheCacheManager ;
	
	@Autowired
	private CacheTemplate cacheTemplate ;
	
	
	//创建线程池
	ExecutorService cachedThreadPool = Executors.newFixedThreadPool(10) ;
	
	//指令枪，倒计时，为了让所有线程同一时间点开始
	private CountDownLatch latch = new CountDownLatch(10);
	
	@Before
	public void setUp() throws Exception {
		ehCacheCacheManager.getCache("demo").clear();
	}
	@Test    
	public void clear(){
	}

	@Test
	public void testFindEhCache() throws Exception {
		for (int i = 0; i < 5; i++) {
			SysUser sysUser = cacheTemplate.findEhCache("demo", "user",SysUser.class, new CacheCallback<SysUser>() {

				@Override
				public SysUser load() {
					
					return sysUserMapper.findOne("admin");
				}
			});
			logger.info(JSON.toJSONString(sysUser));
		}
		
		
	}
	
	@Test
	public void testFindEhCacheMap() throws Exception {
		for (int i = 0; i < 5; i++) {
			Map map = cacheTemplate.findEhCache("demo", "user",Map.class, new CacheCallback<Map>() {

				@Override
				public Map load() {
					
					return DefaultBeanUtils.objectToMap(sysUserMapper.findOne("admin"));
				}
			});
			logger.info(JSON.toJSONString(map));
		}
		
		//多线程测试需要阻塞主线程
		Thread.sleep(1000*60);
		logger.info("main thread stop");
	}
	
	@Test
	public void testFindEhCacheThread() throws InterruptedException {
		//2、多线程并发测试
		for (int i = 0; i < 10; i++) {
			Thread executeThread = new Thread(new ExecuteEhcacheThreads()); 
			executeThread.start();
			latch.countDown();
		}
		
		//多线程测试需要阻塞主线程
		Thread.sleep(1000*5);
		logger.info("main thread stop");
	}
	
	@Test
	public void testFindEhCacheThreadPool() throws InterruptedException {
		//2、多线程并发测试
		for (int i = 0; i < 10; i++) {
			cachedThreadPool.execute(new ExecuteEhcacheThreads()); 
			latch.countDown();
		}
		
		//多线程测试需要阻塞主线程
		Thread.sleep(1000*5);
		logger.info("main thread stop");
	}
	
	/**
	 * 执行线程
	 * @author 作者 E-mail: wangshuping@bonc.com.cn
	 * @date 创建时间：2017年4月8日 下午6:56:29 
	 * @version V-1.0.0
	 */
	private class ExecuteEhcacheThreads implements Runnable{
		@Override
		public void run() {
			try {
				
				latch.await();
				Map map = cacheTemplate.findCache("demo", "user",100L,Map.class , new CacheCallback<Map>() {

					@Override
					public Map load() {
						
						return DefaultBeanUtils.objectToMap(sysUserMapper.findOne("admin"));
					}
				});
				logger.info(JSON.toJSONString(map));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	//------------------------redis----------------------------------
	@Test
	public void testFindRedisMap() throws Exception {
		for (int i = 0; i < 5; i++) {
			Map map = cacheTemplate.findCache("demo", "user",1000L,Map.class, new CacheCallback<Map>() {

				@Override
				public Map load() {
					
					return DefaultBeanUtils.objectToMap(sysUserMapper.findOne("admin"));
				}
			});
			WebApiResponse<Map> apiResponse = WebApiResponse.success(map,"请求成功",0);

			logger.info(JSON.toJSONString(apiResponse));
		}
	}

	@Test
	public void testFindRedisCache() throws Exception {
		for (int i = 0; i < 5; i++) {
			SysUser sysUser = cacheTemplate.findCache("demo", "user",1*2L,SysUser.class, new CacheCallback<SysUser>() {

				@Override
				public SysUser load() {
					
					return sysUserMapper.findOne("admin");
				}
			});
			logger.info(JSON.toJSONString(sysUser));
		}
	}
	
	@Test
	public void testFindRedisThread() throws InterruptedException {
		//2、多线程并发测试
		for (int i = 0; i < 10; i++) {
			Thread executeThread = new Thread(new ExecuteRedisThreads()); 
			executeThread.start();
			latch.countDown();
		}
		
		//多线程测试需要阻塞主线程
		Thread.sleep(1000*5);
		logger.info("main thread stop");
	}
	
	@Test
	public void testFindRedisThreadPool() throws InterruptedException {
		//2、多线程并发测试
		for (int i = 0; i < 10; i++) {
			Thread.sleep(1000*2);
			logger.info("main thread stop");
			cachedThreadPool.execute(new ExecuteRedisThreads()); 
			//latch.countDown();
		}
		
		//多线程测试需要阻塞主线程
		Thread.sleep(1000*20);
		logger.info("main thread stop");
	}
	
	/**
	 * 执行线程
	 * @author 作者 E-mail: wangshuping@bonc.com.cn
	 * @date 创建时间：2017年4月8日 下午6:56:29 
	 * @version V-1.0.0
	 */
	private class ExecuteRedisThreads implements Runnable{
		@Override
		public void run() {

			//latch.await();
			Map map;
			try {
				map = cacheTemplate.findCache("demo", "user",1*5L,Map.class , new CacheCallback<Map>() {

					@Override
					public Map load() {
						
						return DefaultBeanUtils.objectToMap(sysUserMapper.findOne("admin"));
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//logger.info(JSON.toJSONString(map));

		}
		
	}
	
	@Test
	public void evictEhCacheTest(){
		cacheTemplate.evictCache("interfacesql", "4F3E51189CB2B3E7E05010AC1D0E5B67");
	}


}
