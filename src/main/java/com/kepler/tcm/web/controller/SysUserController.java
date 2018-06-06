package com.kepler.tcm.web.controller;

import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kepler.tcm.domain.SysUser;
import com.kepler.tcm.service.SysUserService;

@RestController
@RequestMapping("/user")
public class SysUserController {
	
	private static Logger log = LoggerFactory.getLogger(SysUserController.class);
	
	@Autowired
	private SysUserService sysUserService ;

	
    @ApiOperation(value="查询用户", notes="当前登陆用户信息")
    @RequestMapping(value="/sessionUser", method=RequestMethod.GET)
	public  SysUser getSessionUser(){
		return sysUserService.getSessionUser();
	}
    
    
    

}
