package com.kepler.tcm.web.filters;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import com.alibaba.druid.support.http.WebStatFilter;

/**
 * 配置druid监控统计功能
 * @author wangsp
 * @date 2017年4月6日
 * @version V1.0
 */
/*@WebFilter(filterName = "druidWebStatFilter", urlPatterns = "/*",
	initParams = {
        @WebInitParam(name="exclusions",value="*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")// 忽略资源
	}
)*/
public class DruidStatFilter extends WebStatFilter {

}
