package com.kepler.tcm.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.HstsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.kepler.tcm.service.SimpleUserDetailsService;
import com.kepler.tcm.service.impl.UserDetailsServiceImpl;
import com.kepler.tcm.web.filters.CaptchaUsernamePasswordAuthenticationFilter;
import com.kepler.tcm.web.security.SimpleAuthenticationDetailsSource;
import com.kepler.tcm.web.security.SimpleAuthenticationProvider;
import com.kepler.tcm.web.security.SimpleLoginFailureHandler;
import com.kepler.tcm.web.security.SimpleLoginSuccessHandler;
import com.kepler.tcm.web.security.SimpleLogoutSuccessHandler;



/**
 * Security 安全管理 http://www.tuicool.com/articles/nEjQjyq
 * http://www.cnblogs.com/davidwang456/p/4549344.html?utm_source=tuicool
 * @author wangsp
 * @date 2017年3月21日
 * @version V1.0
 */
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(prefix = "cas.security", name = "enabled", havingValue = "false")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements EnvironmentAware {
	
	@Autowired(required = false)
	private ErrorController errorController;

	@Autowired
	private SecurityProperties security;

	@Autowired
	private ServerProperties server;
	
	/**
	 * 密码加密方式
	 */
	@Value("${security.password.encode:MD5}")
	private String securityPasswordEncode;
	
	/**
	 * 系统登陆页面地址
	 */
	@Value("${spring.security.login.url:/login.html}")
	private String loginPage;
	
	/**
	 * 客户登陆页面地址
	 */
	@Value("${security.custom.login.url:/view/portals/login.html}")
	private String customLoginPage;

	/**
	 * 数据接口能力平台首页
	 */
	@Value("${security.platform.index.url:/view/homepage/index.html}")
	private String platformHomePage;
	
	/**
	 * 默认失败重定向地址
	 */
	@Value("${security.default.failure.url:/login.html?error=user}")
	private String defaultFailureUrl ;
	/**
	 * 后台是否解密前台传来的密码
	 */
	@Value("${login.pwd.encrypt.enabled:true}")
	private boolean pwdEncryptEnabled;
	
	private static Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

	private RelaxedPropertyResolver propertyResolver;  

	@Override  
	public void setEnvironment(Environment env) {  
		this.propertyResolver = new RelaxedPropertyResolver(env, "security.");  
	}  
	
	/**
	 * 默认静态忽略静态资源
	 */
	private static List<String> DEFAULT_IGNORED = Arrays.asList("/css/**", "/js/**",
			"/images/**", "/webjars/**", "/**/favicon.ico");
	
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
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		super.configure(auth);
		auth
			//内存内部实现登陆认证
			//.inMemoryAuthentication().withUser("user").password("password").roles("ROLE_USER");
			//自定义实现登陆认证
			//.userDetailsService(new UserDetailsServiceImpl());
			//.userDetailsService(userDetailsServiceImpl())
			.authenticationProvider(simpleAuthenticationProvider());//自定义
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
					.antMatchers("/oauth2/authorize").permitAll()
					.antMatchers(loginPage).permitAll() //登陆页面放权
					.antMatchers("/view/portals/create-account.html").permitAll() //客户注册页面放权
					.antMatchers(customLoginPage).permitAll() //客户登陆页面放权
					.antMatchers(defaultFailureUrl).permitAll() //登陆失败放权
					.antMatchers("/sea/custom/check").permitAll() //客户注册用户放权
					.antMatchers(platformHomePage).permitAll() //平台首页放权--- by lvyuxue
					//.antMatchers("/**").access("hasRole('ROLE_READER')")
					.antMatchers(paths).access("hasRole('ROLE_USER')")
					//.antMatchers("/**").access("hasRole('ROLE_ADMIN')")
					
					.and()
					
					.formLogin()
						.loginPage(loginPage)
						//.failureUrl("/login.html?error=true")
						.usernameParameter("username") //不指定，默认j_username
						.passwordParameter("password") //不指定，默认j_password
						.loginProcessingUrl("/login")//不指定，默认j_spring_security_check
						.authenticationDetailsSource(simpleAuthenticationDetailsSource())
						//.defaultSuccessUrl("/")
						//.defaultSuccessUrl("", true)
						.successHandler(successHandler())
						//.successForwardUrl("")
						//.failureForwardUrl("")
						.failureHandler(failureHandler());
		}
		
		
		//session 管理设置
		http.sessionManagement()
				.sessionAuthenticationErrorUrl(loginPage)
				.maximumSessions(1)
				.maxSessionsPreventsLogin(false)
				.expiredUrl(loginPage)
				.sessionRegistry(sessionRegistry());
		
		//注销设置
		http.logout()
				.logoutUrl("/logout").permitAll()
				.logoutSuccessHandler(sampleLogoutHandler())
				.invalidateHttpSession(true)
				//.deleteCookies(cookieNamesToClear)
				.logoutSuccessUrl(loginPage);
		
		//添加过滤器
		//登陆验证码校验过滤器
		//http.addFilterBefore(new SecurityAuthenticationKaptchaFilter(authenticationManager(),"/login", "/login?error=kaptcha"), UsernamePasswordAuthenticationFilter.class);
		
		//http.addFilter(formFilter());
		//http.addFilterAt(atFilter(), UsernamePasswordAuthenticationFilter.class);
		//http.addFilterAfter(afterFilter(), UsernamePasswordAuthenticationFilter.class);
		
		http.addFilterAt(captchaUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
		//自定义设置登陆页面，可以根据请求地址不同，跳转到指定的登陆页面
		
		http.exceptionHandling()
			.defaultAuthenticationEntryPointFor(authenticationEntryPoint(),new AntPathRequestMatcher("/view/manage/**"))
			.defaultAuthenticationEntryPointFor(authenticationCostomEntryPoint(), new AntPathRequestMatcher("/view/portals/**"));
		
		
		//匿名用户配置
		/*http.authorizeRequests().antMatchers("/html/abouts/*").anonymous();*/
	}
	
	
	
	/**
	 * 自定义登陆验证,客户端登陆页，跳转到指定的登陆页面
	 * @return
	 */
	@Bean
	public LoginUrlAuthenticationEntryPoint authenticationCostomEntryPoint(){
		String loginFormUrl = customLoginPage;
		LoginUrlAuthenticationEntryPoint authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(loginFormUrl);
		return authenticationEntryPoint ;
	}
	
	/**
	 * 自定义登陆验证,后台管理登陆页，跳转到指定的登陆页面
	 * @return
	 */
	@Bean
	public LoginUrlAuthenticationEntryPoint authenticationEntryPoint(){
		String loginFormUrl = loginPage;
		LoginUrlAuthenticationEntryPoint authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(loginFormUrl);
		return authenticationEntryPoint ;
	}
	
	
	/**
	 * 自定义认证源
	 * @return
	 */
	@Bean
	public SimpleAuthenticationDetailsSource simpleAuthenticationDetailsSource(){
		return new SimpleAuthenticationDetailsSource();
	}
	
	/**
	 * sha1密码摘要算法实例
	 * @return
	 */
	@Bean
	public ShaPasswordEncoder shaPasswordEncoder(){
		
		ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder();
		
		return shaPasswordEncoder;
	}
	
	/**
	 * md5密码摘要算法实例
	 * @return
	 */
	@Bean
	public Md5PasswordEncoder md5PasswordEncoder(){
		
		Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
		
		return md5PasswordEncoder;
	}
	
	/**
	 * 自定义登陆认证解析工厂
	 * @return
	 */
	@Bean
	public  SimpleAuthenticationProvider simpleAuthenticationProvider(){
		
		SimpleAuthenticationProvider simpleAuthenticationProvider = new SimpleAuthenticationProvider();
		
		//自定义实现登陆认证
		simpleAuthenticationProvider.setSimpleUserDetailsService(userDetailsServiceImpl());
		
		//设置加密方式
		if(securityPasswordEncode.contains("SHA1")){
			simpleAuthenticationProvider.setPasswordEncoder(shaPasswordEncoder());
		}else{
			simpleAuthenticationProvider.setPasswordEncoder(md5PasswordEncoder());
		}
		
		//设置前台传来的密码是否解密  默认解密
		simpleAuthenticationProvider.setPwdEncryptEnabled(pwdEncryptEnabled);
		
		return simpleAuthenticationProvider ;
	}
	
	/**
	 * 用户登陆信息查询服务实例
	 * @return
	 */
	@Bean
	public SimpleUserDetailsService userDetailsServiceImpl(){
		
		return new UserDetailsServiceImpl();
	}
	
	/**
	 * 自定义登陆成功跳转处理器
	 * @return
	 */
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
	
	/**
	 * 自定义登陆失败跳转处理器
	 * @return
	 */
	@Bean
	public SimpleLoginFailureHandler failureHandler(){
		
		SimpleLoginFailureHandler failureHandler = new SimpleLoginFailureHandler();
		
		failureHandler.setDefaultFailureUrl(defaultFailureUrl);
		
		return failureHandler;
	}
	
	/**
	 * 自定义退出跳转处理器
	 * @return
	 */
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

	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	@Bean
	public CaptchaUsernamePasswordAuthenticationFilter captchaUsernamePasswordAuthenticationFilter() throws Exception{
		CaptchaUsernamePasswordAuthenticationFilter filter = new CaptchaUsernamePasswordAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(successHandler());
		filter.setAuthenticationFailureHandler(failureHandler());
		filter.setAuthenticationDetailsSource(simpleAuthenticationDetailsSource());
		return filter;
	}
}
