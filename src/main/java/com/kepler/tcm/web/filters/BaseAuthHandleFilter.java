package com.kepler.tcm.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
/**
 * 判断基本鉴权过滤器
 * @author lvyx
 * 2017年10月24日 09:38:05
 */
public class BaseAuthHandleFilter implements Filter{
	/**
	 * 判断token鉴权
	 */
	@Value("${security.basic.enabled:true}")
	private boolean baseEnabled;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		//存入基本鉴权标识用于页面的判断
		Cookie cookie = new Cookie("baseEnable",baseEnabled+"");
		httpResponse.addCookie(cookie);
		chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
