package com.kepler.tcm.aspectj.system;

import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author wangsp
 * @date 2017年5月8日
 * @version V1.0
 */
public class CreateUserAspectJ {
	
	private Logger logger =  LoggerFactory.getLogger(this.getClass());
	
	 /**
     * 定义一个切入点.
     * 解释下：
     *
     * ~ 第一个 * 代表任意修饰符及任意返回值.
     * ~ 第二个 * 任意包名
     * ~ 第三个 * 代表任意方法.
     * ~ 第四个 * 定义在web包或者子包
     * ~ 第五个 * 任意方法
     * ~ .. 匹配任意数量的参数.
     */

     @Pointcut("execution(public * com.sea.web..*.*(..))")
     public void pointCut(){}

     @Before("pointCut()")
     public void doBefore(JoinPoint joinPoint){
    	 
     }
     
     @AfterReturning("pointCut()")
     public void doAfterReturning(JoinPoint joinPoint){
    	
     }
}
