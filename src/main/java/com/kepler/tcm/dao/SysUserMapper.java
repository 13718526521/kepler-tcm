package com.kepler.tcm.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kepler.tcm.domain.SysUser;
@Mapper
public interface SysUserMapper extends BaseGenericMapper<SysUser , String>{

	/**
	 * 登陆验证，通过用户名
	 * @param userName
	 * @return
	 */
	public SysUser findOne(String userName);
	
	void updatePasswordErrorLockAndCountByLoginName(@Param("passwordErrorLock")String passwordErrorLock,@Param("passwordErrorCount")int passwordErrorCount,@Param("loginName")String loginName);
	
	void updatePasswordErrorLockAndCountAll(@Param("passwordErrorLock")String passwordErrorLock,@Param("passwordErrorCount")int passwordErrorCount);
	
	void updatePasswordErrorCountByLoginName(@Param("passwordErrorCount")int passwordErrorCount,@Param("loginName")String loginName);
	
	void updateByLoginNameSelective(SysUser sysUser);
	
}