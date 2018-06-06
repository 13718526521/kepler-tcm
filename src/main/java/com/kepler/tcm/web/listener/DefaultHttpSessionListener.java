package com.kepler.tcm.web.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 
 * @author wangsp
 * @date 2017年3月21日
 * @version V1.0
 */
@WebListener
public class DefaultHttpSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		
		 System.out.println("Session 被创建");
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		
		System.out.println("ServletContex初始化");
		
	}

}
