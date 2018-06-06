package com.kepler.tcm.web.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.kepler.tcm.domain.DetailsUser;
/**
 * 系统注销成功后，具体操作
 * @author wangsp
 * @date 2017年4月5日
 * @version V1.0
 */
public class SimpleLogoutSuccessHandler implements LogoutSuccessHandler {

	protected static Logger logger = LoggerFactory.getLogger(SimpleLogoutSuccessHandler.class); 
	
	
	/**
	 * 客户成功退出地址
	 */
	@Value("${security.custom.logout.success.url:/view/portals/login.html}")
	private String customLogoutSuccessUrl ;
	
	/**
	 * 系统管理成功退出地址
	 */
	@Value("${security.logout.success.url:/login.html}")
	private String logoutSuccessUrl;
	
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	
	/**
	 * session 注册信息
	 */
	@Autowired
	private SessionRegistry sessionRegistry;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) throws IOException,
			ServletException {
		
		
		//判断是否为客户端登陆失败
		String url = request.getRequestURI();
		if(url.contains("view/portals")){
			logoutSuccessUrl = customLogoutSuccessUrl;
		}
		
		
		if(authentication != null){  
			
			logger.debug(" user  [ {} ] already Logout",authentication.getName() ); 
			DetailsUser userDetails = (DetailsUser)authentication.getPrincipal(); 
			
			List<SessionInformation> sis =sessionRegistry.getAllSessions(userDetails, false); 
			
			//判断同一个用户，当前登陆的会话个数，若果超过设置项，踢出用户
	        if (sis != null) {  
	             for (SessionInformation si : sis) {  
	                 si.expireNow();  
	                 logger.debug(si.isExpired() ? "yes,  session be expired": "no yet,session still active");  
	                 sessionRegistry.removeSessionInformation(si.getSessionId()); 
	                 logger.debug("[{}] have be kick out!", userDetails.getUsername());  
	            }  
	        }
		}
	    logger.debug("logout Redirect Url :{}",logoutSuccessUrl);
		redirectStrategy.sendRedirect(request, response, logoutSuccessUrl);       
	} 
}
