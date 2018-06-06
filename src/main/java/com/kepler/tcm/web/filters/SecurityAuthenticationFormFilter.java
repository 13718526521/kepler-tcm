package com.kepler.tcm.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class SecurityAuthenticationFormFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println(" ----------Form filter init--------------");
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
		System.out.println(" ----------Form filter doFilter--------------");
		
	}

	@Override
	public void destroy() {
		System.out.println(" ----------Form filter destroy--------------");
		
	}

}
