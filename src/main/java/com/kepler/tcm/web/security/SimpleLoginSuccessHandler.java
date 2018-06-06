package com.kepler.tcm.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.kepler.tcm.domain.DetailsUser;
import com.kepler.tcm.domain.SysUser;
/**
 * 登陆成功后要处理的任务,可设定重定向到各种场景
 * @author wangsp
 * @date 2017年3月29日
 * @version V1.0
 */
public class SimpleLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	
	protected static Logger logger = LoggerFactory.getLogger(SimpleLoginSuccessHandler.class);
	
	
	/**
	 * 通过请求参数，动态指定跳转地址key
	 */
	@Value("${security.paramter.url.key:j_targetUrl}")
	private String targetUrlParameter;
	
	/**
	 * 登陆成功默认跳转地址
	 */
	@Value("${security.default.target.url:/home.html}")
	private String defaultTargetUrl;
	
	
	/**
	 * 是否总是使用用户默认地址跳转标记
	 */
	@Value("${security.always.default.targeturl:false}")
	private boolean alwaysUseDefaultTargetUrl = false;
	
	/**
	 * 
	 */
	private boolean useReferer = false;
	
	/**
     * 设置自定义session保存用户信息key
     */
	@Value("${security.user.session.key:user}")
	private String userSessionKey;
	
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	private RequestCache requestCache = new HttpSessionRequestCache();
    
	 
    
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		//获取session
		HttpSession session=request.getSession();
        
		//保存自定义用户信息
		DetailsUser detailsUser = (DetailsUser)authentication.getPrincipal();
        SysUser user = detailsUser.getSysUser();
        user.setPassword("[protected]");
        session.setAttribute(userSessionKey, user);
        
        //如果不是首次登陆，只是session过期，这里会保存跳转到登陆页面之前的访问地址
		SavedRequest savedRequest = requestCache.getRequest(request, response);

		
		if (savedRequest == null) { //首次登陆
			
			clearAuthenticationAttributes(request);
			
			handle(request, response, authentication);
			
			return;
		}
		
		//获取参数中重定向地址
		String targetUrlParameter = getTargetUrlParameter();
		
		//判断
		if (isAlwaysUseDefaultTargetUrl()
				|| (targetUrlParameter != null && StringUtils.hasText(request
						.getParameter(targetUrlParameter)))) {
			
			//清除session中保留的重定向地址
			requestCache.removeRequest(request, response);
			
			clearAuthenticationAttributes(request);
			
			handle(request, response, authentication);
			
			return;
		}

		//清空session 异常属性
		clearAuthenticationAttributes(request);

		// Use the DefaultSavedRequest URL
		String targetUrl = savedRequest.getRedirectUrl();
		logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	    
	}
	
	/**
	 * 
	 */
	protected void handle(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		//获取跳转目标地址
		String targetUrl = determineTargetUrl(request, response);

		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to "
					+ targetUrl);
			return;
		}
		
		//重定向到指定地址
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	
	/**
	 * 获取地址
	 */
	protected String determineTargetUrl(HttpServletRequest request,
			HttpServletResponse response) {
		
		//判断是否总是使用默认重定向地址
		if (isAlwaysUseDefaultTargetUrl()) {
			return defaultTargetUrl;
		}

		String targetUrl = null;
		
		//优先获取参数中的重定向地址
		if (targetUrlParameter != null) {
			targetUrl = request.getParameter(targetUrlParameter);

			if (StringUtils.hasText(targetUrl)) {
				logger.debug("Found targetUrlParameter in request: " + targetUrl);

				return targetUrl;
			}
		}

		//如果启用引用页面，则使用引用地址做重定向地址
		if (useReferer && !StringUtils.hasLength(targetUrl)) {
			targetUrl = request.getHeader("Referer");
			logger.debug("Using Referer header: " + targetUrl);
		}

		//如果以上条件都不满足，设置默认重定向地址
		if (!StringUtils.hasText(targetUrl)) {
			
			targetUrl = defaultTargetUrl;
			logger.debug("Using default Url: " + targetUrl);
		}

		return targetUrl;
	}

	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}
	
	
	public void setTargetUrlParameter(String targetUrlParameter) {
		if (targetUrlParameter != null) {
			Assert.hasText(targetUrlParameter, "targetUrlParameter cannot be empty");
		}
		this.targetUrlParameter = targetUrlParameter;
	}

	protected String getTargetUrlParameter() {
		return targetUrlParameter;
	}
	
	
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}
	
	
	public void setUseReferer(boolean useReferer) {
		this.useReferer = useReferer;
	}
	
	
	public void setAlwaysUseDefaultTargetUrl(boolean alwaysUseDefaultTargetUrl) {
		this.alwaysUseDefaultTargetUrl = alwaysUseDefaultTargetUrl;
	}

	protected boolean isAlwaysUseDefaultTargetUrl() {
		return alwaysUseDefaultTargetUrl;
	}
	
    public String getUserSessionKey() {
		return userSessionKey;
	}

	public void setUserSessionKey(String userSessionKey) {
		this.userSessionKey = userSessionKey;
	}
	
}
