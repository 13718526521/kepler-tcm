package com.kepler.tcm.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 
 * @author wangsp
 * @date 2017年3月21日
 * @version V1.0
 */
@WebListener
public class DefaultServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
        System.out.println("ServletContex初始化");
        System.out.println(sce.getServletContext().getServerInfo());
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
		System.out.println("ServletContex销毁");
		
	}

}
