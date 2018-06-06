package com.kepler.tcm.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.google.code.kaptcha.Constants;
import com.kepler.tcm.web.security.SimpleLoginFailureHandler;
/**
 * 验证码校验
 * @author wangsp
 * @date 2017年6月1日
 * @version V1.0
 */
public class SecurityAuthenticationKaptchaFilter extends UsernamePasswordAuthenticationFilter {

	private String  servletPath ;

	public SecurityAuthenticationKaptchaFilter(AuthenticationManager authenticationManager,
			String servletPath,String failureUrl) {
		
		super();
		
		this.servletPath = servletPath;
		
		//设置认证管理
		setAuthenticationManager(authenticationManager);
		//setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(failureUrl)); 
		//设置错误处理
        setAuthenticationFailureHandler(new SimpleLoginFailureHandler(failureUrl));  
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;  
		HttpServletResponse res=(HttpServletResponse)response;  
		if ("POST".equalsIgnoreCase(req.getMethod())&& servletPath.equals(req.getServletPath())){
			String expect = (String) req.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);  
			if(expect != null && !expect.equalsIgnoreCase(req.getParameter("kaptcha"))){  
				unsuccessfulAuthentication(req, res, new InsufficientAuthenticationException("输入的验证码不正确"));  
	                return;  
			}  
		}  
		chain.doFilter(request,response);  
		System.out.println(" ----------kaptchaFilter filter doFilter--------------");
		
	}

	@Override
	public void destroy() {
		System.out.println(" ----------kaptchaFilter filter destroy--------------");
		
	}
}
