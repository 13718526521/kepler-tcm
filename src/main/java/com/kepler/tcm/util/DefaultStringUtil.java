package com.kepler.tcm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具
 * @author wangsp
 * @date 2017年3月21日
 * @version V1.0
 */
public class DefaultStringUtil {
	
	
    public static final char UNDERLINE='_';
    
    
    /**
     * 驼峰转下划线
     * @param param 源字符串
     * @return
     */
    public static String camelToUnderline(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=param.charAt(i);
            if (Character.isUpperCase(c)){
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
     * 下划线转驼峰
     * @param param 源字符串
     * @return
     */
    public static String underlineToCamel(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=param.charAt(i);
            if (c==UNDERLINE){
               if (++i<len){
                   sb.append(Character.toUpperCase(param.charAt(i)));
               }
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    public static String underlineToCamel2(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        StringBuilder sb=new StringBuilder(param);
        Matcher mc= Pattern.compile("_").matcher(param);
        int i=0;
        while (mc.find()){
            int position=mc.end()-(i++);
            sb.replace(position-1,position+1,sb.substring(position,position+1).toUpperCase());
        }
        return sb.toString();
    }
    
    /**
	 * map集合，key驼峰转换
	 * @return
	 */
	public static Map<String,Object> underlineToCamel(Map<String,Object> paramMap){
		Map<String,Object> resMap = new HashMap<String,Object>();
		Set<String> set = paramMap.keySet();
		for(String obj:set){
			resMap.put(underlineToCamel(nullToString(obj).trim().toLowerCase()), paramMap.get(obj));
		}
		return resMap;
	}
	
	// 空字符串转化为""
	public static String nullToString(Object str) {
		if (str == null) {
			return "";
		} else {
			try {
				return str.toString().trim();
			} catch (Exception e) {
				return "";
			}
		}
	}
	//去掉空格
	public static String tirmString(Object str) {
		if (str != null) {
			 return str.toString().trim();
		}else{
			return null;
		}
	}
	//替换参数
	public static String replaceParam(String sql,Map param){
		Set<String> set = param.keySet();
		for(String key:set){
			sql = sql.replace("#"+key+"#", nullToString(param.get(key)));
		}
		return sql;
	}
	/**
	 * 获得参数对象方法，将所有参数封装为大写Map，如果参数名同名，则只取第一个参数值
	 * @return
	 */
	public static Map<String,Object> getUpCaseParam(Map paramMap){
		Map<String,Object> resMap = new HashMap<String,Object>();
		Set set = paramMap.keySet();
		for(Object obj:set){
			Object[] paramObj = (Object[])paramMap.get(tirmString(nullToString(obj)));
			resMap.put(nullToString(obj).toUpperCase().trim(), paramObj[0]);
		}
		return resMap;
	}
	/**
	 * 判断字符串是为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }
	/**
	 * 判断字符串不为空
	 * @param str
	 * @return
	 */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * 替换特殊字符 
     *    eg： #param#  
     * @param source 需要替换的参数
     * @param param  参数列表
     * @return
     */
    public static String replaceSqlParam(String source , Map<String , Object> param ){
		Set<String> set = param.keySet();
		for(String key:set){
			source = source.replace("#"+key+"#", 
					(param.get(key).toString().trim() == null ?  "" :  param.get(key).toString().trim()));
		}
		return source;
		
    }
    
    /**
	 * 获得参数对象方法，将所有参数封装为Map，如果参数名同名，则只取第一个参数值
	 * @return
	 */
	public static Map<String,Object> getParam(Map paramMap){
		Map<String,Object> resMap = new HashMap<String,Object>();
		Set set = paramMap.keySet();
		for(Object obj:set){
			Object[] paramObj = (Object[])paramMap.get(tirmString(nullToString(obj)));
			resMap.put(nullToString(obj).trim(), paramObj[0]);
		}
		return resMap;
	}
    
	public static void main(String[] args) {
		
	}
}
