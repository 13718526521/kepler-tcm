package com.kepler.tcm.web.filters;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.google.code.kaptcha.Constants;
import com.kepler.tcm.exception.CaptchaException;
/**
 * 验证码校验
 * @author  liqiang
 * @date    2017年8月24日
 * @version V1.0
 */
public class CaptchaUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	protected static Logger logger=LoggerFactory.getLogger(CaptchaUsernamePasswordAuthenticationFilter.class);
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		logger.debug("验证码操作：进行验证");
		String captchaParam = request.getParameter("j_captcha");
		if(StringUtils.isBlank(captchaParam)){
			logger.debug("验证码操作：验证失败，验证码为空");
			throw new CaptchaException("验证码为空");
		}
		String captchaSession=(String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
		if(!captchaSession.equals(captchaParam)){
			logger.debug("验证码操作：验证失败，验证码错误");
			throw new CaptchaException("验证码错误");
		}
		logger.debug("验证码操作：验证成功");
		
		if (true && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod());
		}
		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				username, password);

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
		
	
	}


}