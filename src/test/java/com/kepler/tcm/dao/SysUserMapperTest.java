package com.kepler.tcm.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.kepler.tcm.domain.SysUser;

public class SysUserMapperTest extends BaseMapperTest {

	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	
	@Test
	public void testFindOne() {
		SysUser sysUser = sysUserMapper.findOne("admin");
		System.out.println(JSONObject.toJSON(sysUser));
	}

}
