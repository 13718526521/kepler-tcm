package com.kepler.tcm.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

public class DefaultBeanUtils {
	
	/**
	 * Map 2 Object使用org.apache.commons.beanutils进行转换 
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {    
        if (map == null)  
            return null;  
  
        Object obj = beanClass.newInstance();  
  
        BeanUtils.populate(obj, map);  
  
        return obj;  
    }    
    
	/**
	 * Object 2 Map使用org.apache.commons.beanutils进行转换 
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
    public static Map<?, ?> objectToMap(Object obj) {  
        if(obj == null)  
            return null;   
  
        return new BeanMap(obj);  
    }
    
    /**
     * 使用Introspector进行转换 
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject1(Map<String, Object> map, Class<?> beanClass) throws Exception {    
        if (map == null)   
            return null;    
  
        Object obj = beanClass.newInstance();  
  
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());    
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();    
        for (PropertyDescriptor property : propertyDescriptors) {  
            Method setter = property.getWriteMethod();    
            if (setter != null) {  
                setter.invoke(obj, map.get(property.getName()));   
            }  
        }  
  
        return obj;  
    }
    
    /**
     * 使用Introspector进行转换 
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> objectToMap1(Object obj) throws Exception {    
        if(obj == null)  
            return null;      
  
        Map<String, Object> map = new HashMap<String, Object>();   
  
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());    
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();    
        for (PropertyDescriptor property : propertyDescriptors) {    
            String key = property.getName();    
            if (key.compareToIgnoreCase("class") == 0) {   
                continue;  
            }  
            Method getter = property.getReadMethod();  
            Object value = getter!=null ? getter.invoke(obj) : null;  
            map.put(key, value);  
        }    
  
        return map;  
    }
    
    /**
     * 使用reflect进行转换 
     * @param map 
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject2(Map<String, Object> map, Class<?> beanClass) throws Exception {    
        if (map == null)  
            return null;    
  
        Object obj = beanClass.newInstance();  
  
        Field[] fields = obj.getClass().getDeclaredFields();   
        for (Field field : fields) {    
            int mod = field.getModifiers();    
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){    
                continue;    
            }    
  
            field.setAccessible(true);    
            field.set(obj, map.get(field.getName()));   
        }   
  
        return obj;    
    }
    
    /**
     * 使用reflect进行转换 
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> objectToMap2(Object obj) throws Exception {    
        if(obj == null){    
            return null;    
        }   
  
        Map<String, Object> map = new HashMap<String, Object>();    
  
        Field[] declaredFields = obj.getClass().getDeclaredFields();    
        for (Field field : declaredFields) {    
            field.setAccessible(true);  
            map.put(field.getName(), field.get(obj));  
        }    
  
        return map;  
    }    
}
