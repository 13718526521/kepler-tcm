package com.kepler.tcm.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import com.kepler.tcm.exception.CaptchaException;

public class SimpleLoginFailureHandler implements AuthenticationFailureHandler {
	
	protected static Logger logger = LoggerFactory.getLogger(SimpleLoginFailureHandler.class);

	
	/**
	 * 客户失败重定向地址
	 */
	@Value("${security.custom.failure.url:/view/portals/login.html?error=user}")
	private String customFailureUrl ;
	
	/**
	 * 默认失败重定向地址
	 */
	@Value("${security.default.failure.url:/login.html?error=user}")
	private String defaultFailureUrl;
	
	
	/**
	 * 设置是否内部转向
	 */
	private boolean forwardToDestination = false;
	
	/**
	 * 是否允许创建session
	 */
	private boolean allowSessionCreation = true;
	
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	/**
	 * 默认构造器
	 */
	public SimpleLoginFailureHandler() {
		
	}

	public SimpleLoginFailureHandler(String defaultFailureUrl) {
		setDefaultFailureUrl(defaultFailureUrl);
	}

	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		
		//判断是否为客户端登陆失败
		// 获取请求是从哪里来的  
        String referer = request.getHeader("referer");
		if(referer.contains("view/portals")){
			defaultFailureUrl = customFailureUrl;
		}
		
		if (defaultFailureUrl == null) {
			logger.debug("No failure URL set, sending 401 Unauthorized error");

			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Authentication Failed: " + exception.getMessage());
		}else {
			
			saveException(request, exception);

			String errorCode="code";
			if(exception instanceof CaptchaException){
				//验证码错误
				errorCode+="=0";
			}else if(exception instanceof BadCredentialsException){
				//密码错误
				errorCode+="=1";
			}else if(exception instanceof LockedException){
				//账户锁定
				errorCode+="=2";
			}else if(exception instanceof DisabledException){
				//账户禁用
				errorCode+="=3";
			}else {
				//其它
				errorCode+="=4";
			}
			logger.debug("错误码： " + errorCode);
			if(defaultFailureUrl.indexOf("?")>0){
				//url包含“？”
				if(!defaultFailureUrl.matches(".*(\\?|&)$")){
					//url不以“？”或者“&”结尾
					errorCode="&"+errorCode;
				}
			}else{
				//url不包含“？”
				errorCode="?"+errorCode;
			}
			if (forwardToDestination) {
				
				logger.debug("Forwarding to " + defaultFailureUrl+errorCode);

				request.getRequestDispatcher(defaultFailureUrl+errorCode)
						.forward(request, response);
			}else {
				logger.debug("Redirecting to " + defaultFailureUrl+errorCode);
				redirectStrategy.sendRedirect(request, response, defaultFailureUrl+errorCode);
			}
		}
	}

	/**
	 * 
	 * @param request
	 * @param exception
	 */
	protected final void saveException(HttpServletRequest request,
			AuthenticationException exception) {
		if (forwardToDestination) {
			
			request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
		}else {
			
			HttpSession session = request.getSession(false);
			if (session != null || allowSessionCreation) {
				request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,
						exception);
			}
		}
	}


	public void setDefaultFailureUrl(String defaultFailureUrl) {
		Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultFailureUrl), "'"
				+ defaultFailureUrl + "' is not a valid redirect URL");
		this.defaultFailureUrl = defaultFailureUrl;
	}

	protected boolean isUseForward() {
		return forwardToDestination;
	}

	public void setUseForward(boolean forwardToDestination) {
		this.forwardToDestination = forwardToDestination;
	}


	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	protected boolean isAllowSessionCreation() {
		return allowSessionCreation;
	}

	public void setAllowSessionCreation(boolean allowSessionCreation) {
		this.allowSessionCreation = allowSessionCreation;
	}
}
