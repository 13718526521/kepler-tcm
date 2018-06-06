package com.kepler.tcm.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName: SessionHandlerInterceptor
 * @Description: 自定义拦截器1
 * @author wangshuping
 * @date 2016年9月6日 上午11:36:31
 * @version V1.0  
 */
public class SessionHandlerInterceptor implements HandlerInterceptor {
	
	/**
	 * 日志管理
	 */
	private Logger log  = LoggerFactory.getLogger(SessionHandlerInterceptor.class);
	
	@Value("${security.basic.enabled:true}")
	private boolean securityBasicEnabled;
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
		//log.debug("request sessionhandlerinterceptor before .......");
		//给ajax请求增加超时标记 ,有效：effective； 超时：timeout
    	if( request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equals("XMLHttpRequest") ){
    		//设定头文件中ajax请求超时标志
    		if(securityBasicEnabled == true && !checkSessionEffective(request)){
    			response.setHeader("sessionstatus", "timeout"); 
    		}else{
    			response.setHeader("sessionstatus", "effective"); 
    		}
         }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    	//log.debug("request sessionhandlerinterceptor post .......");
    	
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    	//log.debug("request sessionhandlerinterceptor after .......");
    }
    
    /**
     * 校验session是否超时
     * @param request  请求实例
     * @return  超时状态
     */
    private boolean  checkSessionEffective(HttpServletRequest request){
    	if(request.getSession(false) == null ){
    		return false ;
    	}else{
    		return true ;
    	}
    }
}
