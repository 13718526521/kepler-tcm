package com.kepler.tcm.web.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
/**
 * 自定义AuthenticationDetailsSource,在认证过滤器中使用  在登录时验证
 * @author wangsp
 * @date 2017年4月5日
 * @version V1.0
 */
public class SimpleAuthenticationDetailsSource implements
		AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

	@Override
	public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
		return new SimpleWebAuthenticationDetails(context);
	}

}
