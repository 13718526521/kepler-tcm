package com.kepler.tcm.common.response;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class WebApiResponseTest {
    
	List<Object> list ;
	
	Map<Object,Object> map ;
	
	Map result ;
	
	@Before
	public void setUp() throws Exception {
		result = new HashMap();
		list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add("List"+i);

		}
		map = new HashMap<Object,Object>();
		for (int i = 0; i < 10; i++) {
			map.put("key"+i, "value"+i);
		}
		map.put("list", list);
		
		result.put("DATA", map);
	}

	@Test
	public void testSuccessTStringInt() {
		WebApiResponse<Map<Object, Object>> apiResponse = WebApiResponse.success(map,"请求成功",0);
		System.out.println(JSON.toJSONString(apiResponse));
	}

	@Test
	public void testSuccessTString() {
		WebApiResponse<List<Object>> apiResponse = WebApiResponse.success(list,"请求成功");
		System.out.println(JSON.toJSONString(apiResponse));
	}

	@Test
	public void testSuccessT() {
		WebApiResponse<Map<Object, Object>> apiResponse = WebApiResponse.success(map);
		System.out.println(JSON.toJSONString(apiResponse));
	}

	@Test
	public void testErrorInt() {
		WebApiResponse<Map<Object, Object>> apiResponse = WebApiResponse.error(-1);
		System.out.println(JSON.toJSONString(apiResponse));
	}

	@Test
	public void testErrorString() {
		WebApiResponse<Map<Object, Object>> apiResponse = WebApiResponse.error("sssss");
		System.out.println(JSON.toJSONString(apiResponse));
	}

	@Test
	public void testErrorStringInt() {
		WebApiResponse<Map<Object, Object>> apiResponse = WebApiResponse.error("sssss",6);
		System.out.println(JSON.toJSONString(apiResponse));
	}
	
	
	//---------------------------------------------------------------------------
	@Test
	public void testSuccessMapTStringInt() {
		Map apiResponse = WebApiResponse.successMap(result,"请求成功",0);
		System.out.println(JSON.toJSONString(apiResponse));
	}

	@Test
	public void testSuccessMapTString() {
		Map apiResponse = WebApiResponse.successMap(result,"请求成功");
		System.out.println(JSON.toJSONString(apiResponse));
	}

	@Test
	public void testSuccessMapT() {
		Map apiResponse = WebApiResponse.successMap(result);
		System.out.println(JSON.toJSONString(apiResponse));
	}

	@Test
	public void testErrorMapInt() {
		Map apiResponse = WebApiResponse.errorMap(-1);
		System.out.println(JSON.toJSONString(apiResponse));
	}

	@Test
	public void testErrorMapString() {
		Map apiResponse = WebApiResponse.errorMap("sssss");
		System.out.println(JSON.toJSONString(apiResponse));
	}

	@Test
	public void testErrorMapStringInt() {
		Map apiResponse = WebApiResponse.errorMap("sssss",6);
		System.out.println(JSON.toJSONString(apiResponse));
	}

}
