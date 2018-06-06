package com.kepler.tcm.cache;
/**
 * 缓存回调接口定义
 * @author 作者 E-mail: wangshuping@bonc.com.cn
 * @date 创建时间：2017年4月8日 下午9:49:53 
 * @version V-1.0.0
 */
public interface CacheCallback<T> {
	
	/**
	 * @throws Exception 
	 * 加载数据
	 * @return
	 * @throws
	 */
	public T load() throws Exception;
}
