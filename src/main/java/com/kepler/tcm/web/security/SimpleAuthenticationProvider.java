package com.kepler.tcm.web.security;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import com.kepler.tcm.service.SimpleUserDetailsService;
import com.kepler.tcm.util.EncryptUtil;

public class SimpleAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider{
	
    protected static Logger logger = LoggerFactory.getLogger(SimpleAuthenticationProvider.class);
    
    @Autowired
    private RedisTemplate redisTemplate;

 		/**
 		 * The plaintext password used to perform
 		 * {@link PasswordEncoder#isPasswordValid(String, String, Object)} on when the user is
 		 * not found to avoid SEC-2056.
 		 */
 		private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

 		@SuppressWarnings("deprecation")
		private PasswordEncoder passwordEncoder;

 		/**
 		 * The password used to perform
 		 * {@link PasswordEncoder#isPasswordValid(String, String, Object)} on when the user is
 		 * not found to avoid SEC-2056. This is necessary, because some
 		 * {@link PasswordEncoder} implementations will short circuit if the password is not
 		 * in a valid format.
 		 */
 		private String userNotFoundEncodedPassword;

 		private SaltSource saltSource;
 		
 		private boolean pwdEncryptEnabled;

 		//private UserDetailsService userDetailsService;
 		
 		//自定义参数接口
 		private SimpleUserDetailsService simpleUserDetailsService;
 		
 		//请求参数
 		private Map<String,Object> params ;

 		public SimpleAuthenticationProvider() {
 			setPasswordEncoder(new PlaintextPasswordEncoder());
 		}

 		@SuppressWarnings("deprecation")
		protected void additionalAuthenticationChecks(UserDetails userDetails,
 				UsernamePasswordAuthenticationToken authentication) 
 			throws AuthenticationException {
 			
 			//SimpleWebAuthenticationDetails details = (SimpleWebAuthenticationDetails) authentication.getDetails(); 
 			
 			Object salt = null;

 			if (this.saltSource != null) {
 				salt = this.saltSource.getSalt(userDetails);
 			}

 			if (authentication.getCredentials() == null) {
 				logger.debug("Authentication failed: no credentials provided");

 				throw new BadCredentialsException(messages.getMessage(
 						"AbstractUserDetailsAuthenticationProvider.badCredentials",
 						"Bad credentials"));
 			}

 			String presentedPassword = authentication.getCredentials().toString();
 			if(pwdEncryptEnabled) {
 				String keyid="";
 				if(this.getParams().get("pwdKeyId")!=null) {
 					keyid=this.getParams().get("pwdKeyId").toString();
 				}
 				logger.info("加密秘钥id："+keyid);
 				if(keyid==null||"".equals(keyid)) {
					throw new BadCredentialsException(messages.getMessage(
							"AbstractUserDetailsAuthenticationProvider.badCredentials",
							"Bad credentials"));
				}
 				String key="";
 				try {
 					if(redisTemplate.opsForValue().get(keyid)!=null) {
 						key=redisTemplate.opsForValue().get(keyid).toString();
 					}
 				}catch(Exception e) {
 					e.printStackTrace();
 					throw new BadCredentialsException(messages.getMessage(
							"AbstractUserDetailsAuthenticationProvider.badCredentials",
							"Bad credentials"));
 				}
 				if("".equals(key)) {
		        	throw new BadCredentialsException(messages.getMessage(
							"AbstractUserDetailsAuthenticationProvider.badCredentials",
							"Bad credentials"));
		        }
 				//对前台传来的密码解密
				try {
					presentedPassword=EncryptUtil.aesDecrypt(presentedPassword, key);
				} catch (Exception e) {
					e.printStackTrace();
					throw new BadCredentialsException(messages.getMessage(
							"AbstractUserDetailsAuthenticationProvider.badCredentials",
							"Bad credentials"));
				}
 			}else {
 				logger.info("解密前台传来的密码功能为false  不解密");
 			}
 			
 			
 			
 			logger.debug("presentedPassword：[Protected]");
 			logger.debug("passwordEncoder:{}",passwordEncoder);
 			//暂存原有加密方式
 			PasswordEncoder oldPasswordEncoder = passwordEncoder;
 			

 			
 			//判断加密方式---IsEncryption = false 使用明文，不加密
 			String isEncryption = this.getParams().containsKey("isEncryption") 
 					? (String)this.getParams().get("isEncryption") : "true"; 
 					
 			logger.debug("IsEncryption：{}",isEncryption);
 			if(this.getParams() != null && isEncryption.trim().equals("false") ){
 				passwordEncoder = new PlaintextPasswordEncoder(); //明文方式
 			}

 			//密码校验
 			if (!passwordEncoder.isPasswordValid(userDetails.getPassword(),
 					presentedPassword, salt)) {
 				logger.debug("Authentication failed: password does not match stored value");
 				
 				passwordEncoder = oldPasswordEncoder; //还原原有加密方式
 				logger.debug("密码认证失败，系统设置加密方式 :{}",passwordEncoder);

 	 			throw new BadCredentialsException(messages.getMessage(
 						"AbstractUserDetailsAuthenticationProvider.badCredentials",
 						"Bad credentials"));
 			}
 			//将加密方式还原为原来的加密
 			passwordEncoder = oldPasswordEncoder; //还原原有加密方式
 			logger.debug("密码认证成功，系统设置加密方式 :{}",passwordEncoder);
 			
 		}

 		protected void doAfterPropertiesSet() throws Exception {
 			Assert.notNull(this.simpleUserDetailsService, "A UserDetailsService must be set");
 		}

 		/**
 		 * 首先获取用户认证信息
 		 */
 		@SuppressWarnings("deprecation")
		protected final UserDetails retrieveUser(String username,
 				UsernamePasswordAuthenticationToken authentication)
 				throws AuthenticationException {
 			
 			//获取请求信息
 			SimpleWebAuthenticationDetails details = (SimpleWebAuthenticationDetails) authentication.getDetails(); 
 			
 			//装载请求参数
 			this.setParams(details.getParams());
 			
 			UserDetails loadedUser;

 			try {
 				//验证并且获取自定义用户信息
 				//loadedUser = this.getSimpleUserDetailsService().loadUserByUsername(username);
 				
 				loadedUser = this.getSimpleUserDetailsService().loadUserByUsername(username,params);
 				//清除原有参数，方式参数不一致问题
 				//params.clear();
 			}
 			catch (UsernameNotFoundException notFound) {
 				if (authentication.getCredentials() != null) {
 					String presentedPassword = authentication.getCredentials().toString();
 					passwordEncoder.isPasswordValid(userNotFoundEncodedPassword,
 							presentedPassword, null);
 				}
 				throw notFound;
 			}
 			catch (Exception repositoryProblem) {
 				throw new InternalAuthenticationServiceException(
 						repositoryProblem.getMessage(), repositoryProblem);
 			}

 			if (loadedUser == null) {
 				throw new InternalAuthenticationServiceException(
 						"UserDetailsService returned null, which is an interface contract violation");
 			}
 			return loadedUser;
 		}

 		/**
 		 * Sets the PasswordEncoder instance to be used to encode and validate passwords. If
 		 * not set, the password will be compared as plain text.
 		 * <p>
 		 * For systems which are already using salted password which are encoded with a
 		 * previous release, the encoder should be of type
 		 * {@code org.springframework.security.authentication.encoding.PasswordEncoder}.
 		 * Otherwise, the recommended approach is to use
 		 * {@code org.springframework.security.crypto.password.PasswordEncoder}.
 		 *
 		 * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder}
 		 * types.
 		 */
 		@SuppressWarnings("deprecation")
		public void setPasswordEncoder(Object passwordEncoder) {
 			Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

 			if (passwordEncoder instanceof PasswordEncoder) {
 				setPasswordEncoder((PasswordEncoder) passwordEncoder);
 				return;
 			}

 			if (passwordEncoder instanceof org.springframework.security.crypto.password.PasswordEncoder) {
 				final org.springframework.security.crypto.password.PasswordEncoder delegate = (org.springframework.security.crypto.password.PasswordEncoder) passwordEncoder;
 				setPasswordEncoder(new PasswordEncoder() {
 					public String encodePassword(String rawPass, Object salt) {
 						checkSalt(salt);
 						return delegate.encode(rawPass);
 					}

 					public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
 						checkSalt(salt);
 						return delegate.matches(rawPass, encPass);
 					}

 					private void checkSalt(Object salt) {
 						Assert.isNull(salt,
 								"Salt value must be null when used with crypto module PasswordEncoder");
 					}
 				});

 				return;
 			}

 			throw new IllegalArgumentException(
 					"passwordEncoder must be a PasswordEncoder instance");
 		}

 		@SuppressWarnings("deprecation")
		private void setPasswordEncoder(PasswordEncoder passwordEncoder) {
 			Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
 			this.userNotFoundEncodedPassword = passwordEncoder.encodePassword(
 					USER_NOT_FOUND_PASSWORD, null);
 			this.passwordEncoder = passwordEncoder;
 		}

 		@SuppressWarnings("deprecation")
		protected PasswordEncoder getPasswordEncoder() {
 			return passwordEncoder;
 		}


 		public void setSaltSource(SaltSource saltSource) {
 			this.saltSource = saltSource;
 		}

 		protected SaltSource getSaltSource() {
 			return saltSource;
 		}

 		/*public void setUserDetailsService(UserDetailsService userDetailsService) {
 			this.userDetailsService = userDetailsService;
 		}

 		protected UserDetailsService getUserDetailsService() {
 			return userDetailsService;
 		}*/
 		
 		public void setSimpleUserDetailsService(SimpleUserDetailsService simpleUserDetailsService) {
 			this.simpleUserDetailsService = simpleUserDetailsService;
 		}

 		protected SimpleUserDetailsService getSimpleUserDetailsService() {
 			return simpleUserDetailsService;
 		}
 		
 		public void setParams(Map<String,Object> params) {
 			this.params = params;
 		}

 		protected Map<String,Object> getParams() {
 			return params;
 		}

		public boolean isPwdEncryptEnabled() {
			return pwdEncryptEnabled;
		}

		public void setPwdEncryptEnabled(boolean pwdEncryptEnabled) {
			this.pwdEncryptEnabled = pwdEncryptEnabled;
		}
}
