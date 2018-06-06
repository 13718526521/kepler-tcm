package com.kepler.tcm.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.kepler.tcm.web.filters.BaseAuthHandleFilter;
import com.kepler.tcm.web.interceptor.SessionHandlerInterceptor;


/**
 * @ClassName: DefaultWebMvcConfigurerAdapter
 * @Description: 自定义资源映射
 * @author wangshuping
 * @date 2016年10月13日 下午4:47:58
 * @version V1.0  
 */
@Configuration
public class DefaultWebMvcConfiguration extends WebMvcConfigurerAdapter implements EnvironmentAware{
	
	private static Logger log = LoggerFactory.getLogger(DefaultWebMvcConfiguration.class);
	
	

	
	private RelaxedPropertyResolver propertyResolver;  
	
	@Override
	public void setEnvironment(Environment env) {
		this.propertyResolver = new RelaxedPropertyResolver(env, "spring.default.");  
	}
	
	 /**
	  * 设置默认页
	  */
	@Override
	 public void addViewControllers(ViewControllerRegistry registry) {
		 if(propertyResolver.containsProperty("index")){
			registry.addViewController("/sea").setViewName(propertyResolver.getProperty("index")); 
		 }
	    //registry.addViewController("/sea").setViewName("forward:/index.html");
		registry.addViewController("/sea/").setViewName("forward:/view/html/home.html");
	    registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	    super.addViewControllers(registry);
	}
	
	 /**
	  * 静态资源处理
	  */
	 @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//默认静态资源
		//默认值为 classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/
		//registry.addResourceHandler("/page/**").addResourceLocations("classpath:/public/static-sea/");
		if(propertyResolver.containsProperty("static.locations")){
			List<String> listPath = Arrays.asList(propertyResolver.getProperty("static.locations").split(","));
			if(listPath != null && listPath.size() > 0 ){
				for( String path : listPath){
					String resourceLocations = path.split("=")[0];
					String resourceHandler = path.split("=")[1];
					registry.addResourceHandler(resourceHandler).addResourceLocations(resourceLocations);
				}
			}
		}

        super.addResourceHandlers(registry);
    }
    

    
    @Bean
    public SessionHandlerInterceptor sessionHandlerInterceptor(){
    	return   new SessionHandlerInterceptor(); 
    }



    /**
     *  Spting Boot 注册过滤器
     * @return
     */
    /*@Bean
    public FilterRegistrationBean firstFilterRegistrationBean() {
    	
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setName("first");
        FirstFilter firstFilter = new FirstFilter();
        registrationBean.setFilter(firstFilter);
        registrationBean.setOrder(1);
        List<String> urlList = new ArrayList<String>();
        urlList.add("/user/*");
        registrationBean.setUrlPatterns(urlList);
        return registrationBean;
    }*/

    /**
     * 自定义spring mvc servlet	
     * @return
     */
/*    @Bean
    public ServletRegistrationBean dispatcherServlet() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                new DispatcherServlet(), "/");
        registration.setAsyncSupported(true);
        return registration;
    }*/
    /**
     * 增加验证码
     * @return
     * @throws ServletException
     * @throws
     */
    @Bean   
    public ServletRegistrationBean servletRegistrationBean() throws ServletException{  
        ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(),"/base/images/kaptcha.jpg");  
        Map<String,String> initParameters=new HashMap<String,String>();
        initParameters.put("kaptcha.border", "no");//边框
        initParameters.put("kaptcha.image.width", "160");//图片宽度
        initParameters.put("kaptcha.image.height", "38");//图片高度
        initParameters.put("kaptcha.textproducer.font.names", "Arial");//字体
        initParameters.put("kaptcha.textproducer.font.color", "133,133,133");//字体颜色
        initParameters.put("kaptcha.textproducer.font.size", "30");//字体大小
        initParameters.put("kaptcha.textproducer.char.space", "7");//字体间距
        initParameters.put("kaptcha.noise.color", "119,136,153");//干扰线颜色
        initParameters.put("kaptcha.background.clear.from", "white");//背景颜色
        initParameters.put("kaptcha.background.clear.to", "white");//背景颜色
        servlet.setInitParameters(initParameters);
        return servlet;
    }



	/**
	 * 基本鉴权过滤器
	 * @return
	 */
	@Bean
	public BaseAuthHandleFilter baseAuthHandleFilter(){
		return new BaseAuthHandleFilter();
	}
	@Bean
	public FilterRegistrationBean baseAuthRegistration() {

		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setName("baseAuthFilter");
		registrationBean.setFilter(baseAuthHandleFilter());
		List<String> urlList = new ArrayList<String>();
		urlList.add("/*");
		registrationBean.setUrlPatterns(urlList);
		return registrationBean;
	}
	
}
