package com.kepler.tcm.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.SecurityProperties.Headers;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.header.writers.HstsHeaderWriter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.kepler.tcm.service.impl.UserDetailsServiceImpl;
import com.kepler.tcm.web.security.SimpleAuthenticationDetailsSource;
import com.kepler.tcm.web.security.SimpleLoginFailureHandler;
import com.kepler.tcm.web.security.SimpleLoginSuccessHandler;
import com.kepler.tcm.web.security.SimpleLogoutSuccessHandler;
/**
 * Security CAS单点登陆
 * @author wangsp
 * @date 2017年4月24日
 * @version V1.0
 */
@Configuration
@EnableWebSecurity //启用web权限
@EnableGlobalMethodSecurity(prePostEnabled = true) //启用方法验证
@ConditionalOnProperty(prefix = "cas.security", name = "enabled", havingValue = "true")
public class CasSecurityConfiguration extends WebSecurityConfigurerAdapter implements EnvironmentAware {
	
	private static Logger log = LoggerFactory.getLogger(CasSecurityConfiguration.class);
	
	
	@Value("${cas.server.host.url}")
	private String casServerUrl;

	@Value("${cas.server.host.login_url}")
	private String casServerLoginUrl;

	@Value("${cas.server.host.logout_url}")
	private String casServerLogoutUrl;

	@Value("${app.server.host.url}")
	private String appServerUrl;

	@Value("${app.login.url}")
	private String appLoginUrl;

	@Value("${app.logout.url}")
	private String appLogoutUrl;
	
	@Autowired
	private SecurityProperties security;
	
	@Autowired(required = false)
	private ErrorController errorController;
	
	@Autowired
	private ServerProperties server;

	/**
	 * 默认静态忽略静态资源
	 */
	private static List<String> DEFAULT_IGNORED = Arrays.asList("/css/**", "/js/**",
			"/images/**", "/webjars/**", "/**/favicon.ico");
	

	private RelaxedPropertyResolver propertyResolver;  

	@Override  
	public void setEnvironment(Environment env) {  
		this.propertyResolver = new RelaxedPropertyResolver(env, "security.");  
	}  
	
	
	/**
	 * 获取忽略资源路径
	 * @param security
	 * @return
	 */
	public static List<String> getIgnored(SecurityProperties security) {
		List<String> ignored = new ArrayList<String>(security.getIgnored());
		if (ignored.isEmpty()) {
			ignored.addAll(DEFAULT_IGNORED);
		}
		else if (ignored.contains("none")) {
			ignored.remove("none");
		}
		return ignored;
	}
	
	private String normalizePath(String errorPath) {
		String result = StringUtils.cleanPath(errorPath);
		if (!result.startsWith("/")) {
			result = "/" + result;
		}
		return result;
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		
		List<String> ignored = getIgnored(this.security);
		if (this.errorController != null) {
			ignored.add(normalizePath(this.errorController.getErrorPath()));
		}
		String[] paths = this.server.getPathsArray(ignored);
		if (!ObjectUtils.isEmpty(paths)) {
			//此处放权的地址，不可以获取登陆用户信息
			web.ignoring().antMatchers(paths);
			
		}
	}
	
	/**定义认证用户信息获取来源，密码校验规则等*/
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.authenticationProvider(casAuthenticationProvider());
		//inMemoryAuthentication 从内存中获取
		//auth.inMemoryAuthentication().withUser("chengli").password("123456").roles("USER")
		//.and().withUser("admin").password("123456").roles("ADMIN");
		
		//jdbcAuthentication从数据库中获取，但是默认是以security提供的表结构
		//usersByUsernameQuery 指定查询用户SQL
		//authoritiesByUsernameQuery 指定查询权限SQL
		//auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(query).authoritiesByUsernameQuery(query);
		
		//注入userDetailsService，需要实现userDetailsService接口
		//auth.userDetailsService(userDetailsService);
	}
	
	
	public static void configureHeaders(HeadersConfigurer<?> configurer,
			SecurityProperties.Headers headers) throws Exception {
		if (headers.getHsts() != Headers.HSTS.NONE) {
			boolean includeSubdomains = headers.getHsts() == Headers.HSTS.ALL;
			HstsHeaderWriter writer = new HstsHeaderWriter(includeSubdomains);
			writer.setRequestMatcher(AnyRequestMatcher.INSTANCE);
			configurer.addHeaderWriter(writer);
		}
		if (!headers.isContentType()) {
			configurer.contentTypeOptions().disable();
		}
		if (!headers.isXss()) {
			configurer.xssProtection().disable();
		}
		if (!headers.isCache()) {
			configurer.cacheControl().disable();
		}
		if (!headers.isFrame()) {
			configurer.frameOptions().disable();
		}
	}
	
	private String[] getSecureApplicationPaths() {
		List<String> list = new ArrayList<String>();
		for (String path : this.security.getBasic().getPath()) {
			path = (path == null ? "" : path.trim());
			if (path.equals("/**")) {
				return new String[] { path };
			}
			if (!path.equals("")) {
				list.add(path);
			}
		}
		return list.toArray(new String[list.size()]);
	}

	
	/**定义安全策略*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//设置是否开启Basic Security
		if(!security.getBasic().isEnabled()){
			http.requestMatcher(new RequestMatcher() {
				@Override
				public boolean matches(HttpServletRequest request) {
					return false;
				}
			});
		}
		
		//ssl
		if (this.security.isRequireSsl()) {
			http.requiresChannel().anyRequest().requiresSecure();
		}
		//csrf
		if (!this.security.isEnableCsrf()) {
			http.csrf().disable();
		}
		
		//http.rememberMe();
		
		
		//设置请求Header
		configureHeaders(http.headers(),this.security.getHeaders());
		
		
		String[] paths = getSecureApplicationPaths();
		
		if (paths.length > 0) {
			String[] roles = this.security.getUser().getRole().toArray(new String[0]);
			//权限控制
			http.authorizeRequests()
					.antMatchers("/oauth/**").permitAll()
					//.antMatchers("/login.html").permitAll()
					//.antMatchers("/login.html?error=true").permitAll()
					//.antMatchers("/**").access("hasRole('ROLE_READER')")
					.antMatchers(paths).access("hasRole('ROLE_USER')")
					//.antMatchers("/**").access("hasRole('ROLE_ADMIN')")
					
					.and()
					
					.formLogin()
						//.loginPage("/login.html")
						//.failureUrl("/login.html?error=true")
						//.usernameParameter("username") //不指定，默认j_username
						//.passwordParameter("password") //不指定，默认j_password
						//.loginProcessingUrl("/login")//不指定，默认j_spring_security_check
						//.authenticationDetailsSource(simpleAuthenticationDetailsSource())
						//.defaultSuccessUrl("/")
						//.defaultSuccessUrl("", true)
						.successHandler(successHandler())
						//.successForwardUrl("")
						//.failureForwardUrl("")
						.failureHandler(failureHandler());
		}
		
		
		
		//session 管理设置
		/*http.sessionManagement()
				.sessionAuthenticationErrorUrl("/login.html")
				.maximumSessions(1)
				.maxSessionsPreventsLogin(false)
				.expiredUrl("/login.html")
				.sessionRegistry(sessionRegistry());*/
		
		//注销设置
		/*http.logout()
				.logoutUrl("/logout").permitAll()
				.logoutSuccessHandler(sampleLogoutHandler())
				.invalidateHttpSession(true)
				//.deleteCookies(cookieNamesToClear)
				.logoutSuccessUrl("/login.html");*/
		
		//匿名用户配置
		/*http.authorizeRequests().antMatchers("/html/abouts/*").anonymous();*/
		
		http.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint())
			.and()
			.addFilter(casAuthenticationFilter())
			.addFilterBefore(casLogoutFilter(), LogoutFilter.class)
			.addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class);
	}
	
	@Bean
	public SimpleAuthenticationDetailsSource simpleAuthenticationDetailsSource(){
		return new SimpleAuthenticationDetailsSource();
	}
	
	@Bean
	public SimpleLoginSuccessHandler successHandler(){
		
		SimpleLoginSuccessHandler successHandler = new SimpleLoginSuccessHandler();
		
		if(propertyResolver.containsProperty("default.target.url")){
			successHandler.setDefaultTargetUrl(propertyResolver.getProperty("default.target.url"));
		}
		
		if(propertyResolver.containsProperty("user.session.key")){
			successHandler.setUserSessionKey(propertyResolver.getProperty("user.session.key"));
		}
		
		if(propertyResolver.containsProperty("paramter.url.key")){
			successHandler.setTargetUrlParameter(propertyResolver.getProperty("paramter.url.key"));
		}
		
		return successHandler;
	}
	
	@Bean
	public SimpleLoginFailureHandler failureHandler(){
		
		SimpleLoginFailureHandler failureHandler = new SimpleLoginFailureHandler();
		
		if(propertyResolver.containsProperty("default.failure.url")){
			failureHandler.setDefaultFailureUrl(propertyResolver.getProperty("default.failure.url"));
		}
		
		return failureHandler;
	}
	
	@Bean
	public SimpleLogoutSuccessHandler sampleLogoutHandler(){
		
		SimpleLogoutSuccessHandler sampleLogoutHandler = new SimpleLogoutSuccessHandler();
		
		return sampleLogoutHandler;
	}
	
	@Bean
	public SessionRegistry sessionRegistry(){
		
		SessionRegistry sessionRegistry = new SessionRegistryImpl();
		
		return sessionRegistry ;
	}
	
	/**认证的入口*/
	@Bean
	public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
		CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
		casAuthenticationEntryPoint.setLoginUrl(casServerLoginUrl);//单点登陆地址：http://localhost:8081/cas/login
		casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
		return casAuthenticationEntryPoint;
	}
	
	/**指定service相关信息*/
	@Bean
	public ServiceProperties serviceProperties() {
		ServiceProperties serviceProperties = new ServiceProperties();
		//cas中心认证服务配置,拦截时跳往cas服务器的地址， 后面login/cas是固定的   
		//http://localhost:8081/cas/login?service=http%3A%2F%2Flocalhost%3A8080%2Fkepler-web%2Flogin%2Fcas
		serviceProperties.setService(appServerUrl + appLoginUrl); //http://localhost:7070/sea/login/cas
		serviceProperties.setAuthenticateAllArtifacts(true);
		//serviceProperties.setServiceParameter(serviceParameter);
		// 敏感设置，如果设置成true表示敏感，意思就是说，就算客服端已经存在票据，也要到cas server重新登录。
		serviceProperties.setSendRenew(false);
		return serviceProperties;
	}
	
	/**CAS认证过滤器*/
	@Bean
	public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
		CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
		casAuthenticationFilter.setAuthenticationManager(authenticationManager());
		casAuthenticationFilter.setFilterProcessesUrl(appLoginUrl);
		//CAS单点登陆成功之后处理
		casAuthenticationFilter.setAuthenticationSuccessHandler(successHandler());
		//CAS登陆失败之后处理
		casAuthenticationFilter.setAuthenticationFailureHandler(failureHandler());
		//自定义
		//casAuthenticationFilter.setAuthenticationDetailsSource(simpleAuthenticationDetailsSource());
		return casAuthenticationFilter;
	}
	
	/**cas 认证 Provider*/
	@Bean
	public CasAuthenticationProvider casAuthenticationProvider() {
		CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
		casAuthenticationProvider.setAuthenticationUserDetailsService(authenticationUserDetailsService());
		//CAS客户端登陆处理
		//casAuthenticationProvider.setUserDetailsService(customUserDetailsService()); //这里只是接口类型，实现的接口不一样，都可以的。
		casAuthenticationProvider.setServiceProperties(serviceProperties());
		//CAS票据认证 
		//casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
		casAuthenticationProvider.setTicketValidator(cas30ServiceTicketValidator());
		//CAS票据KEY
		casAuthenticationProvider.setKey("casAuthenticationProviderKey");
		return casAuthenticationProvider;
	}
	
	@Bean
	public UserDetailsService customUserDetailsService(){
		return new UserDetailsServiceImpl();
	}
	
/*	*//**用户自定义的AuthenticationUserDetailsService*/
	@Bean
	public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> authenticationUserDetailsService(){
		return new UserDetailsServiceImpl();
	}
	
	/**
	 * 30支持多属性
	 * @return
	 */
	@Bean
	public Cas30ServiceTicketValidator cas30ServiceTicketValidator() {
		return new Cas30ServiceTicketValidator(casServerUrl);
	}
	
	@Bean  
    public FilterRegistrationBean cas30ProxyReceivingTicketValidationFilter() {  
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();  
        Cas30ProxyReceivingTicketValidationFilter cas30ProxyReceivingTicketValidationFilter=new Cas30ProxyReceivingTicketValidationFilter();
        filterRegistration.setName("CAS Validation Filter");
        filterRegistration.setFilter(cas30ProxyReceivingTicketValidationFilter);  
        filterRegistration.addUrlPatterns("/*");  
        filterRegistration.addInitParameter("casServerUrlPrefix", casServerUrl);  
        filterRegistration.addInitParameter("serverName", appServerUrl);  
        filterRegistration.setOrder(2);  
        return filterRegistration;  
    } 
	
	
	/**
	 * 20不支持多属性
	 */
	/*@Bean
	public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
		return new Cas20ServiceTicketValidator(casServerUrl);
	}*/
	
	/**单点登出过滤器*/
	@Bean
	public SingleSignOutFilter singleSignOutFilter() {
		SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
		singleSignOutFilter.setCasServerUrlPrefix(casServerUrl);
		singleSignOutFilter.setIgnoreInitConfiguration(true);
		return singleSignOutFilter;
	}
	
	/**
	 * 请求单点退出过滤器
	 */
	@Bean
	public LogoutFilter casLogoutFilter() {
		LogoutFilter logoutFilter = new LogoutFilter(casServerLogoutUrl, new SecurityContextLogoutHandler());
		logoutFilter.setFilterProcessesUrl(appLogoutUrl);
		return logoutFilter;
	}
	
}
