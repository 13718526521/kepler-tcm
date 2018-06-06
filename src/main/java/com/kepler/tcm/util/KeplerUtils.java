package com.kepler.tcm.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.kepler.tcm.domain.DetailsUser;
import com.kepler.tcm.domain.SysUser;

public class KeplerUtils {


	
	/**
	 * 获取系统登陆用户信息
	 * @return
	 */
	public static final SysUser getSysUser(){
		
		SysUser sysUser = null; 
		try {
			DetailsUser userDetails = (DetailsUser) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			if(userDetails != null ){
				return userDetails.getSysUser() ;
			}
		} catch (Exception e) {
			return sysUser;  
		}
				
		return sysUser;  
	}
}
