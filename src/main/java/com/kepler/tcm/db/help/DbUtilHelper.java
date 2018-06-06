package com.kepler.tcm.db.help;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kepler.tcm.db.DataSourceManager;
import com.kepler.tcm.domain.LocalSource;

public class DbUtilHelper {
	
	private static Logger log = LoggerFactory.getLogger(DbUtilHelper.class);
	
	
	/**
	 * 查询对象
	 */
    private QueryRunner queryRunner;
    
    /**
     * 连接串
     */
    private Connection conn;
    
    /**
     * 私有化构造方法，是用户不能随便构造
     * @param conn 数据库连接对象
     */
    private DbUtilHelper(Connection conn){
        this.queryRunner = new QueryRunner();
        this.conn = conn;
    }
    
    
    /**
     * 得到对象方法，通过传递连接
     * @param conn 连接对象
     * @return  DbUtilHelpper对象
     */
    public static DbUtilHelper getInstance(Connection conn){
        return new DbUtilHelper(conn);
    }
    
    /**
     * 得到对象方法，通过传递连接
     * @param ls 数据源信息
     * @return  DbUtilHelpper对象
     * @throws SQLException 
     */
    public static DbUtilHelper getInstance(LocalSource ls) 
    		throws SQLException{
        return new DbUtilHelper (DataSourceManager.
        		getInstance().getConnection(ls));
    }

    
     
    /**
     * 查询单个值
     * @param sql 查询语句
     * @param param 查询参数
     * @return 返回值
     * @throws SQLException sql语句执行中异常
     */
    
	public Object queryValue(String sql,Object... param) 
    		throws Exception{

        
		Object result = null;
        try {
    		Map<?, ?> map = (Map<?, ?>)queryRunner.query(conn,  sql, new MapHandler(),  param);
            Set<String> keySet = queryRunner.query(conn,  sql, new MapHandler(),  param).keySet();
            result =  map.get(keySet.iterator().next());
		}finally{
			close();
		}
        return result ; 
    }
    
    /**
     * 查询单个数据，以map方式封装
     * @param sql 查询语句
     * @param param 查询参数
     * @return 返回Map值
     * @throws SQLException sql语句执行中异常
     */
    public Map<String, Object> selectOne(String sql,Object... param) 
    		throws SQLException{
    	Map<String, Object> result = null;
        try {
        	result = queryRunner.query(conn,  sql, new MapHandler(),  param);
		}finally{
			close();
		}
        return result ; 
    }
    
    /**
     * 查询结果，自定义封装方式
     * @param sql 查询语句
     * @param param 参数
     * @param rsh 自定义封装实现
     * @return 返回值
     * @throws SQLException sql语句执行中异常
     */
    public Object select(String sql,ResultSetHandler<?> rsh ,Object... param)
    		throws SQLException{
    	Object result = null;
        try {
        	result = queryRunner.query(conn,  sql,  rsh,  param);
		}finally{
			close();
		}
        return result ; 
    }
    
    /**
     * 查询多条结果记录返回List
     * @param sql 查询语句
     * @param param 查询参数
     * @return  查询多条记录值
     * @throws SQLException sql语句执行中异常
     */
    public List<Map<String, Object>> selectList(String sql,Object... param) 
    		throws SQLException{
    	List<Map<String, Object>> result = null;
        try {
        	result = queryRunner.query(conn,  sql, new MapListHandler(),  param) ;
		}finally{
			close();
		}
        return result ; 
    }
    
    /**
     * 查询结果，自定义封装方式
     * @param sql 查询语句
     * @param param 参数
     * @return MyHandler 处理类处理后的结果集
     * @throws SQLException sql语句执行中异常
     */
	public Object selectMap(String sql,Object... param) 
    		throws SQLException{
		Object result = null;
        try {
        	result = queryRunner.query(conn,  sql, new DefaultMapHandler(),  param); 
		}finally{
			close();
		}
        return result ; 
    }
    
    /**
     * 查询结果，自定义封装方式
     * @param sql 查询语句
     * @param param 参数
     * @return  MyListHandler 处理类处理后的结果集
     * @throws SQLException sql语句执行中异常
     */
    public Object selectMapList(String sql,Object... param) throws SQLException{
        Object result = null;
        try {
        	result = queryRunner.query(conn,  sql, new DefaultListHandler(),  param);
		}finally{
			close();
		}
        return result ; 
    }
    
    /**
     * 操作更新方法
     * @param sql 操作sql
     * @param param 操作参数
     * @return  更新数据条数
     * @throws SQLException sql语句执行中异常
     */
    public int update(String sql,Object... param) throws SQLException{
        int result = 0;
        
        try {
        	result = queryRunner.update(conn, sql, param); 
		}finally{
			close();
		}
        return result ; 
    }
    
    /**
     * 批量操作更新方法
     * @param sql 操作sql
     * @param params 操作参数
     * @return 批量更新后的int数组对应入参数组
     * @throws SQLException sql语句执行中异常
     */
    public int[] batchUpdate(String sql,Object[][] params) throws SQLException{
        return queryRunner.batch(conn,sql, params);
    }
    
    
    /**
     * 自定义返回list处理类
     * @author wangsp
     *
     */
    public class DefaultListHandler extends AbstractListHandler<Map<String, Object>>{
 
 
        protected Map<String, Object> handleRow(ResultSet rs) throws SQLException {
            ResultSetMetaData rsmd = rs.getMetaData();
            Map<String,Object> map = new HashMap<String,Object>();
            if(rs.next()){
                for(int i = 1;i<=rsmd.getColumnCount();i++){
                    map.put(rsmd.getColumnLabel(i), rs.getObject(i));
                }
                return map;
            }else{
                return null;
            }
        }
    }
    
    /**
     * 自定义返回map处理类
     * @author Administrator
     *
     */
    public class DefaultMapHandler implements ResultSetHandler<Object>{
        @Override
        public Object handle(ResultSet rs) throws SQLException {
            ResultSetMetaData rsmd = rs.getMetaData();
            if(rs.next()){
                Map<String, Object> map = new HashMap<String, Object>();
                for(int i = 1;i<=rsmd.getColumnCount();i++){
                    map.put(rsmd.getColumnLabel(i), rs.getObject(i));
                }
                return map;
            }else{
                return null;
            }
        }
    }
    
    /**
     * 关闭连接
     */
   public void close(){
	   try {
			if(!conn.isClosed()){
				conn.close();
			}
		} catch (SQLException e1) {
			log.error("exception info : {}",e1.getMessage());
			e1.printStackTrace();
		}
   }

}
