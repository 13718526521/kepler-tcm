package com.kepler.tcm.web.security;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.kepler.tcm.util.DefaultStringUtils;
/**
 * 登录时添加新的自定义参数      如有新的参数可在这里自行添加
 * @author wangsp
 * @date 2017年4月5日
 * @version V1.0
 */
public class SimpleWebAuthenticationDetails extends WebAuthenticationDetails {

	private static final long serialVersionUID = 1L;
	
	
	//登陆自定义参数集合
	private  Map<String,Object> params = new HashMap<String, Object>() ;

	public SimpleWebAuthenticationDetails(HttpServletRequest request) {
		//父类构造
		super(request);
		//子类处理参数
		Map paramMap = request.getParameterMap();
		//将参数map转换
		params = DefaultStringUtils.getParam(paramMap);
		
	}
	
	public Map<String,Object> getParams() {
		return params;
	}
	
	@Override
	public String toString() {
	        StringBuilder sb = new StringBuilder();	        
	        Iterator<Map.Entry<String, Object>> entries = params.entrySet().iterator();          
	        while (entries.hasNext()) {  	          
	            Map.Entry<String, Object> entry = entries.next();   
	            sb.append(super.toString()).append(";");
	            sb.append(super.toString()).append(entry.getKey()+" : ");
	            sb.append(super.toString()).append(entry.getValue());
	        } 
	        return sb.toString();
	 }
}
