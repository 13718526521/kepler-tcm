package com.kepler.tcm.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kepler.tcm.cache.CacheCallback;
import com.kepler.tcm.cache.CacheTemplate;
import com.kepler.tcm.common.constants.ConfigConstant;
import com.kepler.tcm.common.response.WebApiResponse;
import com.kepler.tcm.db.help.DbProcedureHelper;
import com.kepler.tcm.db.help.DbUtilHelper;
import com.kepler.tcm.domain.LocalSource;
import com.kepler.tcm.exception.ApiException;
import com.kepler.tcm.exception.ApiSQLException;
import com.kepler.tcm.service.RestApiService;
import com.kepler.tcm.util.DefaultStringUtils;
@Service
public class RestApiServiceImpl implements RestApiService {

	private static final Logger log  = LoggerFactory.getLogger(RestApiServiceImpl.class);

	@Autowired
	private CacheTemplate cacheTemplate ;
	/**
	 * 数据库查询值的之大小写转换控制
	 * 默认是1 大写，2转换成小写、3 转化成驼峰
	 */
	@Value("${spring.rest.key.enabled:1}")
	private String keyEnabled ;
	/**
	 *参数为空的情况下是需要去掉还是用空查询 true 按空查询
	 */
	@Value("${spring.rest.param.enabled:true}")
	private boolean paramEnabled ;
	@Override
	public Map<String, Object> queryData(String code, Map paramMap) {
		return null;
	}

	
}
