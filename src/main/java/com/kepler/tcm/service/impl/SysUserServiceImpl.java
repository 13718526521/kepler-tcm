package com.kepler.tcm.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.jayway.jsonpath.Criteria;
import com.kepler.tcm.dao.BaseGenericMapper;
import com.kepler.tcm.dao.SysUserMapper;
import com.kepler.tcm.domain.SysUser;
import com.kepler.tcm.service.SysUserService;
import com.kepler.tcm.util.DefaultStringUtils;
import com.kepler.tcm.util.KeplerUtils;
@Service
public class SysUserServiceImpl extends BaseGenericServiceImpl<SysUser , String>
	implements SysUserService {
	 
	private static Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
	
	
	/**
     * 缓存的key
     */
    public static final String USER_ALL_KEY   = "\"user_all\"";
    
    /**
     * value属性表示使用哪个缓存策略，缓存策略在ehcache.xml
     */
    public static final String USER_CACHE_NAME = "sysUser";
    		
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired(required=false)
	private RestTemplate restTemplate;
	
	/**  
	 * 设值注入，注入子类实现
	 */
	@Autowired
	public void setBaseGenericMapper(BaseGenericMapper<SysUser, String> sysUserMapper) {
		this.baseGenericMapper = sysUserMapper;
	}

	@CacheEvict(value = USER_CACHE_NAME) //清除缓存
	public  int delete(String id){
		return sysUserMapper.deleteByPrimaryKey(id);
	}

	@CacheEvict(value = USER_CACHE_NAME)
	public  int insert(SysUser sysUser){
		return sysUserMapper.insertSelective(sysUser);
	}

   
	@Cacheable(value = USER_CACHE_NAME,key = "#id+'user'")
	public  SysUser select(String id){
		return sysUserMapper.selectByPrimaryKey(id);
	}

	@CachePut(value = USER_CACHE_NAME,key = "#id+'user'")
    @CacheEvict(value = USER_CACHE_NAME,key = USER_ALL_KEY)
	public  int update(SysUser sysUser ,String id){
		sysUser.setId(id);
		return sysUserMapper.updateByPrimaryKeySelective(sysUser);
	}

	@Override
	public SysUser getSessionUser() {
		
		return KeplerUtils.getSysUser();
	}
	
	@Override
	public void login(HttpServletRequest request,HttpServletResponse response, String url ,String params) {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		//headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		JSONObject jsonObj = JSONObject.parseObject(params);
		HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj.toString(), headers);
		String result = restTemplate.postForObject(url, formEntity, String.class);
		System.out.println(result);
	}
	
	@Override
	public SysUser info(String username) {
		return  sysUserMapper.findOne(username);
		
	}
	
}
