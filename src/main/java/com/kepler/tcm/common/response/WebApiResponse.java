package com.kepler.tcm.common.response;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

/**
 * API响应解析器
 * @author 作者 E-mail: wangshuping@bonc.com.cn
 * @date 创建时间：2017年4月4日 上午9:37:17 
 * @version V-1.0.0
 */
public class WebApiResponse<T> {
    
    /**
     * 成功编码
     */
	public static final int SUCCESS_CODE = 0 ;
	
	/**
	 * 错误编码
	 */
	public static final int ERROE_CODE = 1 ;
	
	/**
	 * 成功消息
	 */
	public static final String SUCCESS_MESSAGE = "Request success!!" ;
	
	
    /**
     * 消息
     */
    private String message ;
    
    /**
     * 返回码
     */
    private int code ;
    
    /**
     * 返回数据集
     */
    private T data ;
    
    /**
     * 返回MAP数据集
     */
    private Map resultMap ;
    
    /**
     * 
     */
    private static Map<Object ,Object> map  = new HashMap<Object, Object>();
    
    
    /**
     * 加载配置
     */
    static {
    	Properties props = new Properties();
		try {
	         InputStream in = WebApiResponse.class.getClassLoader()
	        		 .getResourceAsStream("i18n/messages_api.properties");
	         props.load(in);
	         Enumeration en = props.propertyNames();
		         while (en.hasMoreElements()) {	 
		              String key = (String) en.nextElement();
		              String Property = props.getProperty (key);
		              map.put(key,Property);
		         }
	    } catch (Exception e) {
	         e.printStackTrace();
	    }
    }
    
    /**
     * 请求成功，返回结果
     * @param data 结果集
     * @param message 自定义消息
     * @param code 消息编码
     * @return WebApiResponse<T> 
     */
    public static <T>  WebApiResponse<T> success(T data,String message,int code){
        WebApiResponse<T> response = new WebApiResponse<T>();
        response.setData(data);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
    
    /**
     * 请求成功，返回结果(取默认编码：code = 0 )
     * @param data 结果集
     * @param message 自定义消息
     * @return WebApiResponse<T> 
     */
    public static <T>  WebApiResponse<T> success(T data,String message){
        
        return WebApiResponse.<T>success(data,message,SUCCESS_CODE);
    }
    
    /**
     * 请求成功，返回结果(取默认编码：code = 0 ,默认消息：Request success!!)
     * @param data 结果集
     * @param message 自定义消息
     * @return WebApiResponse<T> 
     */
    public static <T>  WebApiResponse<T> success(T data){
        return WebApiResponse.<T>success(data,SUCCESS_MESSAGE,SUCCESS_CODE);
    }
    
    /**
     * 请求错误，返回消息提示（根据code取默认消息提示信息）
     * @param code  消息编码
     * @return WebApiResponse<T> 
     */
    public static <T> WebApiResponse<T> error(int code){
        
        return WebApiResponse.<T>error((String)map.get(code+""),code);
    }
    
    /**
     * 请求错误，返回消息提示(取默认编码：code = 1 )
     * @param errorMessage 自定义错误消息
     * @return WebApiResponse<T> 
     */
    public static <T> WebApiResponse<T> error(String errorMessage){
        
        return WebApiResponse.<T>error(errorMessage,ERROE_CODE);
    }
    
    /**
     * 请求错误，返回消息提示
     * @param errorMessage 自定义错误消息
     * @param code 消息编码
     * @return WebApiResponse<T> 
     */
    public static <T> WebApiResponse<T> error(String errorMessage, int code){
        WebApiResponse<T> response = new WebApiResponse<T>();
        response.setCode(code);
        response.setMessage(errorMessage);
        return response;
    }
    
    
    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	//-------------------------------------------------------------------------
    /**
     * 请求成功，返回结果
     * @param data 结果集
     * @param message 自定义消息
     * @param code 消息编码
     * @return WebApiResponse<T> 
     */
    public static Map successMap(Map data,String message,int code){
        Map map = new HashMap();
        map.put("CODE",code);
        map.put("MESSAGE",message);
        map.putAll(data);
        return map;
    }
    
    /**
     * 请求成功，返回结果(取默认编码：code = 0 )
     * @param data 结果集
     * @param message 自定义消息
     * @return WebApiResponse<T> 
     */
    public static Map successMap(Map data,String message){
          return successMap(data,message,SUCCESS_CODE);
    }
    
    /**
     * 请求成功，返回结果(取默认编码：code = 0 ,默认消息：Request success!!)
     * @param data 结果集
     * @param message 自定义消息
     * @return WebApiResponse<T> 
     */
    public static Map successMap(Map data){
        return successMap(data,SUCCESS_MESSAGE,SUCCESS_CODE);
    }
    
    /**
     * 请求错误，返回消息提示（根据code取默认消息提示信息）
     * @param code  消息编码
     * @return WebApiResponse<T> 
     */
    public static Map  errorMap(int code){
        
        return errorMap((String)map.get(code+""),code);
    }
    
    /**
     * 请求错误，返回消息提示(取默认编码：code = 1 )
     * @param errorMessage 自定义错误消息
     * @return Map
     */
    public static Map errorMap(String errorMessage){
        return errorMap(errorMessage,ERROE_CODE);
    }
    
    /**
     * 请求错误，返回消息提示
     * @param errorMessage 自定义错误消息
     * @param code 消息编码
     * @return Map
     */
    public static Map errorMap(String errorMessage, int code){
        Map response = new HashMap();
        response.put("CODE",code);
        response.put("MESSAGE",errorMessage);
        return response;
    }
    
    /**
     * 请求错误，返回消息提示
     * @param errorMessage 自定义错误消息
     * @param code 消息编码
     * @throws IOException 
     */
    public static void responseWriter(HttpServletResponse response ,String errorMessage, int code) throws IOException{
    	Writer writer = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
		Map resultMap = new HashMap();
		resultMap.put("CODE", code);
		resultMap.put("MESSAGE", errorMessage);
		writer.write(JSONObject.toJSONString(resultMap));
		writer.close();
    }
    
    /**
     * 请求错误，返回消息提示
     * @param code 消息编码
     * @throws IOException 
     */
    public static void responseWriter(HttpServletResponse response , int code) throws IOException{
    	Writer writer = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
		Map resultMap = new HashMap();
		resultMap.put("CODE", code);
		resultMap.put("MESSAGE", (String)map.get(code+""));
		writer.write(JSONObject.toJSONString(resultMap));
		writer.close();
    }
    
    
    
    
}