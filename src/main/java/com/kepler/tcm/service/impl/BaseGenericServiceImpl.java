package com.kepler.tcm.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kepler.tcm.dao.BaseGenericMapper;
import com.kepler.tcm.service.BaseGenericService;
import com.kepler.tcm.util.DefaultStringUtil;

public abstract class BaseGenericServiceImpl< T, PK extends Serializable> 
	implements BaseGenericService< T, PK> {
	
	
	private static Logger log = LoggerFactory.getLogger(BaseGenericServiceImpl.class);
	
	/**
	 * 持有持久层引用
	 */
	protected BaseGenericMapper<T, PK> baseGenericMapper ;
	

	/**
	 * 保留默认构造器
	 */
	public BaseGenericServiceImpl(){
		
	}
	
	/**
	 * 提供构造注入，注入子类实现
	 * @param baseGenericMapper
	 */
	public BaseGenericServiceImpl(BaseGenericMapper<T, PK> baseGenericMapper){
		this.baseGenericMapper = baseGenericMapper ;
	}
	
	/**
	 * 提供设值注入，注入子类实现
	 */
	public void setBaseGenericMapper(BaseGenericMapper<T, PK> baseGenericMapper) {
		this.baseGenericMapper = baseGenericMapper;
	}

	


	
	/**
	 * 删除一条，通过主键
	 * @param pk 主键
	 * @return int 返回记录数
	 */
	public int deleteByPrimaryKey(PK pk){
		return baseGenericMapper.deleteByPrimaryKey(pk);
	}

	/**
	 * 插入一条数据,全量插入
	 * @param t 业务实体类型
	 * @return int 返回记录数
	 */
	@Deprecated
	public int insert(T t){
		return baseGenericMapper.insert(t);
	}

	/**
	 * 插入一条数据
	 * @param t 业务实体类型
	 * @return int 返回记录数
	 */
	public int insertSelective(T t){
		return baseGenericMapper.insertSelective(t);
	}



	/**
	 * 查询一条，通过主键
	 * @param pk 主键
	 * @return T 返回业务实体类型
	 */
	public T selectByPrimaryKey(PK pk){
		return baseGenericMapper.selectByPrimaryKey(pk);
	}



	/**
	 * 更新一条数据,通过主键
	 * @param t 业务实体引用
	 * @return int 返回记录数
	 */
	public int updateByPrimaryKeySelective(T t){
		return baseGenericMapper.updateByPrimaryKeySelective(t);
	}

	/**
	 * 更新一条数据,通过主键（全量）
	 * @param t 业务实体引用
	 * @return int 返回记录数
	 */
	@Deprecated
	public int updateByPrimaryKey(T t){
		return baseGenericMapper.updateByPrimaryKey(t);
	}
	
	/**
	 * 分页查询，需要自定义XML检索条件
	 * @param pageNum  当前页码
	 * @param pageSize 每页显示记录数
	 * @param t 业务试实体类型，前端参数
	 * @param orderBy 排序字段
	 * @return Map<String, Object> 返回结果集
	 * eg:
	 *   {rows :[{t},{t}],total : total}
	 */
	public Map<String, Object> queryPage(int pageNum , int pageSize , T t,String  orderBy){
		Map<String, Object> result = new HashMap<String, Object>();
		
		Page<T> page = PageHelper.startPage(pageNum,pageSize,DefaultStringUtil.camelToUnderline(orderBy));
		result.put("rows", baseGenericMapper.queryPage(t));
		result.put("total",page.getTotal());
		
		return result;
	}
	   
	/**
	 * 批量删除数据
	 * @param ids 参数list
	 */
	public int deleteByIds(JSONArray ids){
		return baseGenericMapper.deleteByIds(ids);
	}
	

}
