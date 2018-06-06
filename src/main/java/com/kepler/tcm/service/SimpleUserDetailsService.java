package com.kepler.tcm.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * 接口适配器模式扩展接口，实现多参数传递
 * @author wangsp
 * @date 2017年5月22日
 * @version V1.0
 */
public interface SimpleUserDetailsService  extends UserDetailsService {
	
	
	/**
	 * 扩展用户信息获取方法
	 * @param username  用户吗名
	 * @param params  参数
	 * @return
	 * @throws UsernameNotFoundException
	 */
	UserDetails loadUserByUsername(String username , Map<String,Object> params) 
			throws UsernameNotFoundException;

}
