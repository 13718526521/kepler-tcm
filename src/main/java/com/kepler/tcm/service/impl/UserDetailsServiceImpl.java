package com.kepler.tcm.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.kepler.tcm.common.enums.SysLoginMode;
import com.kepler.tcm.dao.SysUserMapper;
import com.kepler.tcm.domain.DetailsUser;
import com.kepler.tcm.domain.SysUser;
import com.kepler.tcm.service.SimpleUserDetailsService;
/**
 * 自定义实现登陆认证
 * 登陆认证分三种情况：1、系统内用户表配置登陆；2、单点登陆；3、匿名用户(实质是没有登陆过程)
 * @author wangsp
 * @date 2017年3月21日
 * @version V1.0
 */
public class UserDetailsServiceImpl implements SimpleUserDetailsService , AuthenticationUserDetailsService<CasAssertionAuthenticationToken>{

	private static Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	@Autowired
	private SecurityProperties security;
	
	@Value("${security.login.mode:CONFIG}")
	private String sysLoginMode;
	
	@Value("${cas.security.enabled:false}")
	private boolean casEnabled;
	
	@Value("${cas.load.mode:false}")
	private boolean casLoadMode;
	
	@Value("${login.fail.lock.hour:0.5}")
	private String loginFialLockHourStr;
	
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	//用户登陆认证验证权限
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
		throws UsernameNotFoundException, DataAccessException{
		
		DetailsUser user  = null ;
		try{
			SysUser sysUser = getSysUser(username);
			boolean enabled=false;
			boolean accountNonLocked=false;
			if(sysUser!=null) {
				if(sysUser.getIsUsable()==0) {
					enabled=true;
				}
				if(sysUser.getPasswordErrorLock().equals("0")) {
					accountNonLocked=true;
				}else{
					/*
                	 * 账户已锁定，并且sysLoginMode配置为LOCAL,即从本地数据库查询的时候。
                	 * 根据锁定时长来判断是否解锁用户。如果用户锁定时间超过设置时长，解锁用户，否则继续锁定用户
                	 */
                	if(sysLoginMode.equals(SysLoginMode.LOCAL.name())){
                		//解锁用户
                		if(unlockUser(sysUser)){
                			sysUser.setPasswordErrorLock("0");
                			 accountNonLocked = true;
                		}
                	}
				}
				user =  new DetailsUser(sysUser, enabled, accountNonLocked,getAuthorities(sysUser.getIsAdmin()));
			}else {
				throw new UsernameNotFoundException("Error in retrieving user"); 
			}
	    } catch (Exception e) {  
	    	log.debug("Error in retrieving user");  
	        throw new UsernameNotFoundException("Error in retrieving user");  
	    }  
		return user ;
	}

    /**
     * 设置默认用户认证权限
     */
    public Collection<GrantedAuthority> getAuthorities(String access) {
    	List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
    	  
    	// 如果参数access为1.则拥有ROLE_ADMIN权限  
    	if (access != null && access.equals("1")) {
    		log.debug("Grant ROLE_ADMIN to this user");
    		authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));  
    	}
    	
    	//只有用户权限
    	log.debug("Grant ROLE_USER to this user");
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authList;
    }

    
	@Override
	public UserDetails loadUserByUsername(String username, Map<String,Object> params)
			throws UsernameNotFoundException {
		DetailsUser user  = null ;
		SysUser sysUser =null;
		boolean isSysUser=true;//系统用户标识，默认为true
		try{

			sysUser = getSysUser(username);
	
			boolean enabled=false;
			boolean accountNonLocked=false;
			if(sysUser!=null) {
				if(sysUser.getIsUsable()==0) {
					enabled=true;
				}
				if(sysUser.getPasswordErrorLock().equals("0")) {
					accountNonLocked=true;
				}else{
					/*
                	 * 账户已锁定，并且sysLoginMode配置为LOCAL,即从本地数据库查询的时候。
                	 * 根据锁定时长来判断是否解锁用户。如果用户锁定时间超过设置时长，解锁用户，否则继续锁定用户
                	 */
					if(isSysUser){
						if(sysLoginMode.equals(SysLoginMode.LOCAL.name())){
							//解锁用户
							if(unlockUser(sysUser)){
								sysUser.setPasswordErrorLock("0");
								accountNonLocked = true;
							}
						}
					}
				}
				user =  new DetailsUser(sysUser, enabled, accountNonLocked,getAuthorities(sysUser.getIsAdmin()));
			}else {
				throw new UsernameNotFoundException("Error in retrieving user"); 
			}

	    } catch (Exception e) {  
	    	log.debug("Error in retrieving user");  
	        throw new UsernameNotFoundException("Error in retrieving user");  
	    }  
		return user ;
	}
	
	/**
	 * 获取系统管理用户登陆信息
	 * @param username 登陆输入用户名
	 * @return  SysUser 
	 */
	private SysUser getSysUser(String username){
		SysUser sysUser = null ;
		if(sysLoginMode.equals(SysLoginMode.LOCAL.name())){
	        //2、本地系统数据库用户
	        sysUser = new SysUser() ;
	        sysUser = sysUserMapper.findOne(username);
        	log.info("local login mode : {}",SysLoginMode.LOCAL.name());
        }
		
		if(sysUser == null ){
			log.debug("get username failure!!");
		}
		return sysUser ;
	} 
	


	@Override
	public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
		DetailsUser user  = null ;
		SysUser sysUser = null;
		if(!casLoadMode){
			Assertion assertion=token.getAssertion();
			AttributePrincipal attributePrincipal=assertion.getPrincipal();
			Map map=attributePrincipal.getAttributes();
			sysUser = new SysUser();
			if(map.get("ID")!=null) {
				sysUser.setId(map.get("ID").toString());
			}
			if(map.get("LOGIN_NAME")!=null) {
				sysUser.setLoginName(map.get("LOGIN_NAME").toString());
			}
	        if(map.get("COUNTRY")!=null) {
	        	sysUser.setCountry(map.get("COUNTRY").toString());
			}
			if(map.get("IS_ADMIN")!=null) {
				sysUser.setIsAdmin(map.get("IS_ADMIN").toString());
			}else {
				sysUser.setIsAdmin("0");
			}
	        if(map.get("IS_USABLE")!=null) {
	        	sysUser.setIsUsable(Integer.parseInt(map.get("IS_USABLE").toString()));
			}
	        if(map.get("PASSWORD_ERROR_LOCK")!=null) {
				sysUser.setPasswordErrorLock(map.get("PASSWORD_ERROR_LOCK").toString());
			}else {
				sysUser.setPasswordErrorLock("0");
			}
	        if(map.get("PASSWORD_ERROR_COUNT")!=null) {
	        	sysUser.setPasswordErrorCount(Integer.parseInt(map.get("PASSWORD_ERROR_COUNT").toString()));
			}else {
				sysUser.setPasswordErrorCount(0);
			}
			if(map.get("MOBILE")!=null) {
				sysUser.setMobile(map.get("MOBILE").toString());
			}
	        if(map.get("PROVINCE")!=null) {
				sysUser.setProvince(map.get("PROVINCE").toString());
			}
			if(map.get("REAL_NAME")!=null) {
				sysUser.setRealName(map.get("REAL_NAME").toString());
			}     
			sysUser.setPassword("[PROTECTED]");
		}else {
			sysUser = getSysUser(token.getName());
		}
		boolean enabled=false;
		boolean accountNonLocked=false;
		if(sysUser!=null) {
			if(sysUser.getIsUsable()==0) {
				enabled=true;
			}
			if(sysUser.getPasswordErrorLock().equals("0")) {
				accountNonLocked=true;
			}
			user = new  DetailsUser(sysUser, enabled, accountNonLocked,getAuthorities(sysUser.getIsAdmin()));
		}
		return user;
	} 
	/**
     * 解锁用户
     * @param username
     * @throws
     */
    private boolean unlockUser(SysUser sysUser){
    	/*
    	 * 用户上次的登录失败到当前时间的时长大于系统配置的锁定时长，则解锁用户。
    	 */
    	boolean res=false;
    	if(sysUser!=null&&StringUtils.isNotBlank(sysUser.getLoginName())){
    		long loginFailLockTimeMillis=(long)(0.5*60*60*1000);//默认一个小时
    		if(StringUtils.isNotBlank(loginFialLockHourStr)){
    			try{
    				float parseFloat = Float.parseFloat(loginFialLockHourStr.trim());
    				if(parseFloat>0){
    					loginFailLockTimeMillis=(long)(parseFloat*60*60*1000);
    				}
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    		}
    		//用户上次登录失败时间
    		Date lastLoginFailTime = sysUser.getLastLoginFailTime();
    		//得到用户登录失败到当前时间的时长大于系统配置的时长，则解锁用户
    		if(lastLoginFailTime==null||
    				new Date().getTime()-lastLoginFailTime.getTime()>=loginFailLockTimeMillis){
    			//解锁用户
    			SysUser entity = new SysUser();
    			entity.setLoginName(sysUser.getLoginName());
    			entity.setPasswordErrorCount(0);
    			entity.setPasswordErrorLock("0");
    			sysUserMapper.updateByLoginNameSelective(entity);
    			res=true;
    		}
    	}
    	return res;
    }
}
