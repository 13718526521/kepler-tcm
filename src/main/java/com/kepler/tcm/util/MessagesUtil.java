package com.kepler.tcm.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MessagesUtil
 * @Description 接口返回信息包装类
 * @author lvyx
 * @date 2017年3月29日
 * @version 1.0
 */
public class MessagesUtil {
	
	//接口返回map封装
	public static Map interfaceResult(String msg,String code){
		Map<Object,Object> resultMap = new HashMap<Object, Object>();
		resultMap.put("MESSAGE", msg);
		resultMap.put("CODE", code);
		return resultMap;
	}
	//接口返回的节点树
	public static Map<String, Object> sqlTree(String id,String attrName,String attrCode,String attrValue, 
			String attrDesc,String sqlType,String pid){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("ID", id);
		resultMap.put("SQL_NAME", attrName);
		resultMap.put("ATTR_VALUE", attrCode);
		resultMap.put("ATTR_NAME", attrValue);
		resultMap.put("QUERY_INFO", attrDesc);
		resultMap.put("SQL_TYPE", sqlType);
		resultMap.put("PID", pid);
		return resultMap;
	}
	
}
