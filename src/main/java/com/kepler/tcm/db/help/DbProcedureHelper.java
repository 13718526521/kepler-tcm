package com.kepler.tcm.db.help;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.kepler.tcm.db.DataSourceManager;
import com.kepler.tcm.domain.LocalSource;

import oracle.jdbc.OracleTypes;
/**
 * jdbc调用存储过程
 * @author wangsp
 * @date 2017年5月16日
 * @version V1.0
 */
public class DbProcedureHelper {
	
	private static Logger log = LoggerFactory.getLogger(DbUtilHelper.class);

	private static Object stmt;
	
	   /**
	 * 连接串
	 */
	private Connection conn;
   
	private DbProcedureHelper(Connection conn){
		this.conn=conn;
	}
	/**
	* 得到对象方法，通过传递连接
	* @param conn
	* @return
	*/
	public static DbProcedureHelper getInstance(Connection conn){
	  	return new DbProcedureHelper(conn);
	}
	
	public static DbProcedureHelper getInstance(LocalSource ls) throws SQLException{
	  	return new DbProcedureHelper(DataSourceManager.
        		getInstance().getConnection(ls));
	}
	
	   
	/**
	* 调用存储过程：返回值是简单值非列表 
	* @param sql   调用存储过程sql
	* @param param  存储过程传入参数IN
	* @return   int 
	* @throws Exception
	*/
	public int procIntOutSimple(String sql,Object[] param) throws Exception {
		CallableStatement stmt = null ;
		int j=-1;
		try {   
			log.debug("sql:{}",sql);
			stmt = conn.prepareCall(sql);  
			if(param != null && param.length > 0){
				for(int i=0;i<param.length;i++){
					stmt.setObject(i+1, param[i]);
					log.debug("参数：{}",param[i]);
				}
			}  
  
			// out 注册的index 和取值时要对应  
			stmt.registerOutParameter(param.length+1, Types.INTEGER);
           
			stmt.execute();  
  
			// getXxx(index)中的index 需要和上面registerOutParameter的index对应  
			j = stmt.getInt(param.length+1);
       	} finally {  
       		//关闭连接
       		close(null,stmt);
       	}
       	return j;
   }
	
	/**
	* 调用存储过程：返回值是简单值非列表 
	* @param sql   调用存储过程sql
	* @param param  存储过程传入参数IN
	* @return   String 
	* @throws Exception
	*/
	public String procStringOutSimple(String sql,Object[] param) throws Exception {
		CallableStatement stmt = null ;
		String  result = null;
		try {   
			log.debug("sql:{}",sql);
			stmt = conn.prepareCall(sql);  
			if(param != null && param.length > 0){
				for(int i=0;i<param.length;i++){
					stmt.setObject(i+1, param[i]);
					log.debug("参数：{}",param[i]);
				}
			}
			// out 注册的index 和取值时要对应  
			stmt.registerOutParameter(param.length+1, Types.VARCHAR);
			stmt.execute();  
			result = stmt.getString(param.length+1);     
       	} finally {  
       		//关闭连接
       		close(null,stmt);
       	}
       	return result ;
   } 
	   
	/**
	* 调用oracle存储过程，返回json对象Map
	* @param sql
	* @param param
	* @return
	* @throws Exception
	*/
	public Map oracleProcOutMap(String sql,Object[] param) throws Exception {
		Map map = null ;
		CallableStatement stmt = null;
		ResultSet rs = null;
		try {   
			log.debug("sql:{}",sql);
			stmt = conn.prepareCall(sql);  
			if(param != null && param.length > 0){
				for(int i=0;i<param.length;i++){
					stmt.setObject(i+1, param[i]);
					log.debug("参数：{}",param[i]);
				}
			} 
			//oracle用游标获取列表
			stmt.registerOutParameter(param.length+1, OracleTypes.CURSOR);
			stmt.execute(); 
			map = new HashMap();
			// getXxx(index)中的index 需要和上面registerOutParameter的index对应  
			rs = (ResultSet) stmt.getObject(param.length+1);  
			int colunmCount = rs.getMetaData().getColumnCount(); 		
			while(rs.next()){
				for (int i = 0; i < colunmCount; i++) {
					map.put(rs.getMetaData().getColumnName(i + 1), rs.getString(i + 1)); 
				}
			}
	     } finally {  
	    	 //关闭连接
	       	 close(rs,stmt);
	     }
		return map;
	} 
	
	/**
	 * 调用oracle存储过程，实现分页查询
	 * @param sql  存储过程语句
	 * @param tableName --表名
	 * @param strWhere 查询条件
	 * @param orderColumn  排序字段
	 * @param orderStyle   排序格式
	 * @param curPage      当前页数
	 * @param pageSize     每页显示记录数
	 * @return   {total: n , pages : n , rows : []}
	 * @throws Exception
	 */
	public Map oracleProcOutPageQuery(String sql,String tableName ,String strWhere,String orderColumn,
			String orderStyle ,int curPage,int pageSize) throws Exception {
		Map result = new HashMap();
		List<Map<String, Object>> alist=new ArrayList<Map<String, Object>>();
		CallableStatement stmt = null;
		ResultSet rs = null;
		try {   
			log.debug("sql:{}",sql);
			stmt = conn.prepareCall(sql);
			//输入参数
			stmt.setString(1, tableName);
			stmt.setString(2, strWhere);
			stmt.setString(3, orderColumn);
			stmt.setString(4, orderStyle);
			stmt.setInt(5, curPage);
			stmt.setInt(6, pageSize);
			//输出参数
			stmt.registerOutParameter(7, OracleTypes.NUMBER);
			stmt.registerOutParameter(8, OracleTypes.NUMBER);
			stmt.registerOutParameter(9, OracleTypes.CURSOR);
			stmt.execute();  
			// getXxx(index)中的index 需要和上面registerOutParameter的index对应  
			int totalRecords = stmt.getInt(7); 
			int totalPages = stmt.getInt(8); 
			rs = (ResultSet) stmt.getObject(9);  
			int colunmCount = rs.getMetaData().getColumnCount();
			
			 while(rs.next()){
	    	   Map<String, Object> map=new HashMap<String, Object>();
	    	   for (int i = 0; i < colunmCount; i++) {
	    		   map.put(rs.getMetaData().getColumnName(i + 1), rs.getString(i + 1)); 
	    	   }
	    	   alist.add(map);
			 }
			 result.put("total", totalRecords);
			 result.put("pages", totalPages);
			 result.put("rows", alist); 
		} finally {  
			//关闭连接
			close(rs,stmt);
		}
		return result;
	} 
	   
	   
	/**
	* 调用oracle存储过程：有返回值且返回值为单个列表的   
	* @param sql   调用存储过程sql
	* @param param  存储过程传入参数IN
	* @return List<Map>
	* @throws Exception
	*/
	public  List<Map<String, Object>> oracleProcOutList(String sql,Object[] param) throws Exception {  
		List<Map<String, Object>> alist =  null ;
		CallableStatement stmt = null;  
		ResultSet rs = null; 
	   try {  
		   log.debug("sql:{}",sql);
	       stmt = conn.prepareCall(sql);  

	       if(param != null && param.length > 0){
				for(int i=0;i<param.length;i++){
					stmt.setObject(i+1, param[i]);
					log.debug("参数：{}",param[i]);
				}
			} 
	       //oracle用游标获取列表
	       stmt.registerOutParameter(param.length+1, OracleTypes.CURSOR);
	       stmt.execute(); 
	       alist = new ArrayList<Map<String, Object>>();
	       // getXxx(index)中的index 需要和上面registerOutParameter的index对应  
	       rs = (ResultSet) stmt.getObject(param.length+1);  
	       int colunmCount = rs.getMetaData().getColumnCount(); 
	       while(rs.next()){
	    	   Map<String, Object> map=new HashMap<String, Object>();
	    	   for (int i = 0; i < colunmCount; i++) {
	    		   map.put(rs.getMetaData().getColumnName(i + 1), rs.getString(i + 1)); 
	    	   }
	    	   alist.add(map);
	       }  
	   } finally {  
		   //关闭连接
	       close(rs,stmt);
	   }  
	       return alist;
	} 
	   
	   
	   /**
	* 调用mysql存储过程返回json对象
	* @param sql
	* @param param
	* @return
	* @throws Exception
	*/
	public Map mysqlProcOutMap(String sql,Object[] param) throws Exception {  
		Map map = new HashMap();
		CallableStatement stmt = null;  
		ResultSet rs = null; 
		try {
			log.debug("sql:{}",sql);
			stmt = conn.prepareCall(sql);  
			if(param != null && param.length > 0){
				for(int i=0;i<param.length;i++){
					stmt.setObject(i+1, param[i]);
					log.debug("参数：{}",param[i]);
				}
			}     
	       
			//mysql直接用 select 即可返回结果集
			stmt.execute();  
	   
			rs = stmt.getResultSet();  
			while (rs != null && rs.next()) {  
				for (int j = 0; j < rs.getMetaData().getColumnCount(); j++) {
					map.put(rs.getMetaData().getColumnName(j + 1), rs.getString(j + 1)); 
				}
			} 
	  
		} catch (Exception e) {  
			log.error("Exception info ：", e.getMessage());
			e.printStackTrace();  
		} finally {  
			//关闭连接
			close(rs,stmt);  
		}  
		return map;
	} 
	   
	/**
	* 调用mysql存储过程返回单个列表
	* @param sql
	* @param param
	* @return
	* @throws Exception
	*/
	public  List<Map<String, Object>> mysqlProcOutList(String sql,Object[] param) throws Exception {  
		List<Map<String, Object>> alist=new ArrayList<Map<String, Object>>();
		CallableStatement stmt = null;  
		ResultSet rs = null; 
		try {
			log.debug("sql:{}",sql);
			stmt = conn.prepareCall(sql);  
			if(param != null && param.length > 0){
				for(int i=0;i<param.length;i++){
					stmt.setObject(i+1, param[i]);
					log.debug("参数：{}",param[i]);
				}
			} 
			//mysql直接用 select 即可返回结果集
			stmt.execute();  
			rs = stmt.getResultSet();  
			while (rs != null && rs.next()) {  
				Map<String, Object> m=new HashMap<String, Object>();
				for (int j = 0; j < rs.getMetaData().getColumnCount(); j++) {
					m.put(rs.getMetaData().getColumnName(j + 1), rs.getString(j + 1)); 
				}
				alist.add(m);
			} 
		} catch (Exception e) {  
			log.error("Exception info ：", e.getMessage());
			e.printStackTrace();  
		} finally {  
			//关闭连接
			close(rs,stmt);  
		}  
		return alist;
	} 
	   
	   
	   
	/**
	* 调用mysql存储过程：有返回值且返回值为多个列表的   
	* @param sql   调用存储过程sql
	* @param param  存储过程传入参数IN
	* @return Map
	* @throws Exception
	*/
	public  Map mysqlProcOutRs(String sql,Object[] param) throws Exception {  
		Map map = new HashMap();
		CallableStatement stmt = null;  
		ResultSet rs = null;  
		try {
			log.debug("sql:{}",sql);
			stmt = conn.prepareCall(sql);  
			if(param != null && param.length > 0){
				for(int i=0;i<param.length;i++){
					stmt.setObject(i+1, param[i]);
					log.debug("参数：{}",param[i]);
				}
			}     
	       //mysql直接用 select 即可返回结果集
			//返回多个列表
			boolean hadResults = stmt.execute();  
			int i=0;  
			while (hadResults) {
				List<Map> alist = new ArrayList<Map>();
				alist.clear();
				log.debug("result No:----"+(++i));  
				rs = stmt.getResultSet();  
				while (rs != null && rs.next()) {  
					Map<String, String> m=new HashMap<String, String>();
					for (int j = 0; j < rs.getMetaData().getColumnCount(); j++) {
						m.put(rs.getMetaData().getColumnName(j + 1), rs.getString(j + 1)); 
					}
					alist.add(m);
				} 
	   
				map.put("list"+i, alist);
				System.out.println(JSONArray.toJSONString(alist));
				hadResults = stmt.getMoreResults(); //检查是否存在更多结果集  
	       
			} 
		} catch (Exception e) {  
			log.error("Exception info ：", e.getMessage());
			e.printStackTrace();  
		} finally {  
			//关闭连接
			close(rs,stmt);  
		}
		return map;
	} 
	   
	/**
	* 调用存储过程： INOUT同一个参数： 
	* @param sql   调用存储过程sql
	* @param param  存储过程传入参数IN INOUT
	* @return  int返回单个值
	* @throws Exception
	*/
	public int procInOut(String sql,Object[] param) throws Exception {  
		CallableStatement stmt = null;  
		int count=-1;
		try {  
			stmt = conn.prepareCall(sql);  
			if(param != null && param.length > 0){
				for(int i=0;i<param.length;i++){
					stmt.setObject(i+1, param[i]);
					log.debug("参数：{}",param[i]);
				}
			}      
			// 注意此次注册out 的index 和上面的in 参数index 相同  
			stmt.registerOutParameter(param.length, Types.INTEGER);  
			stmt.execute();  
			// getXxx(index)中的index 需要和上面registerOutParameter的index对应  
			count = stmt.getInt(param.length);   
		} catch (Exception e) {  
			log.error("Exception info ：", e.getMessage());
			e.printStackTrace();  
		} finally {  
			//关闭连接
			close(null,stmt);  
		} 
	    return count;
	}  
	
	/**
	 * 关闭连接
	 */
	public void close(ResultSet rs ,CallableStatement stmt){
		try {
			if (null != rs) {  
				rs.close();  
			}  
			if (null != stmt) {  
				stmt.close();  
			}   
			if(!conn.isClosed()){
				conn.close();
			}
		} catch (SQLException e1) {
			log.error("exception info : {}",e1.getMessage());
			e1.printStackTrace();
		}
	}  

}
