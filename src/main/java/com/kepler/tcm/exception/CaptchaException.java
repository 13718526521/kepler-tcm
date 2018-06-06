package com.kepler.tcm.exception;
import org.springframework.security.core.AuthenticationException;
/**
 * 验证码异常
 * @author  liqiang
 * @date    2017年8月24日
 * @version V1.0
 */
public class CaptchaException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CaptchaException(String msg) {
		super(msg);
	}

}