package com.kepler.tcm.service;

import java.util.Map;


public interface RestApiService{
	
	/**
	 * 核心查询接口
	 * @param code   接口编码
	 * @param paramMap  接口参数
	 * @return Map <String, Object> 查询结果集
	 */
	public Map<String, Object> queryData(String code, Map paramMap);
}
