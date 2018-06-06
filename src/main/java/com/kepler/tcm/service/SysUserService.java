package com.kepler.tcm.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kepler.tcm.domain.SysUser;

public interface SysUserService extends BaseGenericService<SysUser, String> {

	/**
	 * 获取sesion用户信息
	 * @return
	 */
	SysUser getSessionUser();
	
	/**
	 * 用户模拟登陆
	 */
	public void login(HttpServletRequest request,HttpServletResponse response,
			String url ,String params);

	public SysUser info(String username);
}
