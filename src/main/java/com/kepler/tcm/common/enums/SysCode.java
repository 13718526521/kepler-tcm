package com.kepler.tcm.common.enums;
/**
 * 
 * @author wangsp
 * @date 2017年4月6日
 * @version V1.0
 */
public enum SysCode {
	
	/**
	 * 系统繁忙
	 */	
	busy("-1"),
	/**
	 * 数据正常返回
	 */
	right("0"),
	/**
	 * 获取数据操作发生错误
	 */
	error("1"),
	/**
	 * 用户ATOK已过期，强制退出，需重新登录系统
	 */
	invalid("2");
	
	/**
	 * 
	 */
	private String sysCode;
	/**
	 * 构造器
	 * <p>Title: </p>
	 * <p>Description: </p>
	 * @param setType
	 */
	SysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getValue() {
		return this.sysCode;
	}
}
