package com.kepler.tcm.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

public interface BaseGenericService<T , PK extends Serializable> {
	
	
	/**
	 * 删除一条，通过主键
	 * @param pk 主键
	 * @return int 返回记录数
	 */
	public int deleteByPrimaryKey(PK pk);

	/**
	 * 插入一条数据,全量插入
	 * @param t 业务实体类型
	 * @return int 返回记录数
	 */
	@Deprecated
	public int insert(T t);

	/**
	 * 插入一条数据
	 * @param t 业务实体类型
	 * @return int 返回记录数
	 */
	public int insertSelective(T t);

	/**
	 * 查询一条，通过主键
	 * @param pk 主键
	 * @return T 返回业务实体类型
	 */
	public T selectByPrimaryKey(PK pk);


	/**
	 * 更新一条数据,通过主键
	 * @param t 业务实体引用
	 * @return int 返回记录数
	 */
	public int updateByPrimaryKeySelective(T t);

	/**
	 * 更新一条数据,通过主键（全量）
	 * @param t 业务实体引用
	 * @return int 返回记录数
	 */
	@Deprecated
	public int updateByPrimaryKey(T t);
	
	/**
	 * 分页查询，需要自定义XML
	 * @param pageNum  当前页码
	 * @param pageSize 每页显示记录数
	 * @param t 业务试实体类型，前端参数
	 * @param orderBy 排序字段
	 * @return Map<String, Object> 返回结果集
	 * eg:
	 *   {rows :[{t},{t}],total : total}
	 */
	public Map<String, Object> queryPage(int pageNum , int pageSize , T t,String  orderBy);

	/**
	 * 批量删除数据
	 * @param ids
	 * @return
	 */
	public int deleteByIds(JSONArray ids);
	
	

	
}
