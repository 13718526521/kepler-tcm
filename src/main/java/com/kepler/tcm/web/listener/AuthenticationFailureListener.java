package com.kepler.tcm.web.listener;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import com.kepler.tcm.dao.SysUserMapper;
import com.kepler.tcm.domain.SysUser;
import com.kepler.tcm.service.SysUserService;


@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

	protected static Logger logger = LoggerFactory.getLogger(AuthenticationFailureListener.class);
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Value("${login.fail.count:5}")
	private int loginFailCount;
	
	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
		String account=event.getAuthentication().getPrincipal().toString();
		SysUser user=null;
		try {
			user=sysUserMapper.findOne(account);
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(user!=null) {
			int passwordErrorCount=user.getPasswordErrorCount();
			passwordErrorCount++;
			//失败限制次数  默认5  从配置文件配置
			logger.info("登陆失败次数限制："+loginFailCount);
			SysUser entity=new SysUser();
			entity.setLoginName(user.getLoginName());
			entity.setLastLoginFailTime(new Date());
			entity.setPasswordErrorCount(passwordErrorCount);
			entity.setPasswordErrorLock("0");
			if(passwordErrorCount>=loginFailCount) {
				entity.setPasswordErrorLock("1");
				logger.info("登录失败次数达到"+passwordErrorCount+"次，用户锁定");
			}
			sysUserMapper.updateByLoginNameSelective(entity);
		}
	}

}
