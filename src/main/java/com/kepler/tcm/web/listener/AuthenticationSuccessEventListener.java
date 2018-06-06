package com.kepler.tcm.web.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.kepler.tcm.dao.SysUserMapper;
import com.kepler.tcm.domain.DetailsUser;


/**
 * 登陆成功监听
 * @author Administrator
 *
 */
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent>{

	protected static Logger logger = LoggerFactory.getLogger(AuthenticationSuccessEventListener.class);
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		DetailsUser userDetails = (DetailsUser) event.getAuthentication().getPrincipal();
		String account=userDetails.getUsername();
		//登陆成功  将用户状态和登陆失败次数重置为0;
		sysUserMapper.updatePasswordErrorLockAndCountByLoginName("0", 0, account);
		logger.info(account+"登陆成功");
	}
	

}
