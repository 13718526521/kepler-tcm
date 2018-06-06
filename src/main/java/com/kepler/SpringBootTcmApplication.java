package com.kepler;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.google.code.kaptcha.servlet.KaptchaServlet;
@EnableDiscoveryClient
@SpringBootApplication
//@Configuration//配置控制  
//@EnableAutoConfiguration//启用自动配置  
@ComponentScan(basePackages={"com.kepler.*"})//组件扫描 
//@ServletComponentScan //扫描带有Servlet的注解，过滤器、监听器、Servlet
//开启缓存  
@EnableCaching
public class SpringBootTcmApplication extends SpringBootServletInitializer 
	implements CommandLineRunner,EmbeddedServletContainerCustomizer {
	
	private static Logger log = LoggerFactory.getLogger(SpringBootTcmApplication.class);
	/**
	 * 外部web容器运行war包启动入口设置
	 */
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(SpringBootTcmApplication.class);
    }

    /**
     * 启动主方法入口
     * @param args
     */
    public static void main(String[] args) {
    	
    	for (int i = 0; i < args.length; i++) {
			 log.debug("arguments[{}] : {}",i,args[i]);
		}
    	
        SpringApplication.run(SpringBootTcmApplication.class, args);
    }
    
    
	@Override
	public void run(String... args) throws Exception {
		
		for (int i = 0; i < args.length; i++) {
			 log.debug("arguments[{}] : {}",i,args[i]);
		}
	}

	/**
	 * 内嵌容器自定义配置
	 */
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		 
		
		 ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
         ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
         ErrorPage error405Page = new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/405.html");
         ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
         container.addErrorPages(error401Page, error404Page,error405Page, error500Page);
         
		 //container.setPort(7070); //设置内置web容器端口号
		 //container.setSessionTimeout(2, TimeUnit.SECONDS); //session超时时间
		 //container.setSessionTimeout(3600);//单位为S
		 //container.setContextPath("/sea");
	}
	
	
	/*@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

	    return new EmbeddedServletContainerCustomizer() {
	        @Override
	        public void customize(ConfigurableEmbeddedServletContainer container) {

	            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
	            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
	            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");

	            container.addErrorPages(error401Page, error404Page, error500Page);
	        }
	    };
	}*/
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		 super.onStartup(servletContext);
		 
		 //自定义设置
		 
		 //编码设置过滤器
		 FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("encoding-filter", CharacterEncodingFilter.class);  
		 encodingFilter.setInitParameter("encoding", "UTF-8");  
		 encodingFilter.setInitParameter("forceEncoding", "true");  
		 encodingFilter.setAsyncSupported(true);  
		 encodingFilter.addMappingForUrlPatterns(null, false, "/*");  
		 //添加验证码servlet
		 ServletRegistration.Dynamic kaptchaServlet = servletContext.addServlet("kaptcha-servlet", KaptchaServlet.class);  
		 kaptchaServlet.addMapping("/except/kaptcha");
	}
	
    /*
     * -----设置http跳转https 
     */
/*    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };

        tomcat.addAdditionalTomcatConnectors(httpConnector());
        return tomcat;
    }

    @Bean
    public Connector httpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);//
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }*/
}
