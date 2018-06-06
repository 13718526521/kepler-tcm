package com.kepler.tcm.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * @ClassName DataSourceUtil
 * @Description 本地数据源测试连接的工具类
 * @author lvyx
 * @date 2017年3月29日
 * @version 1.0
 */
public class DataSourceUtil {
	/**
	 * 定义数据库连接常量
	 */
	//mysql 数据库连接的驱动
	private static final String MYSQL_CLASS_NAME = "com.mysql.jdbc.Driver";
	//mysql数据库连接的url
	private static final String MYSQL_CONN_PATH = "jdbc:mysql://[ipaddress]:[port]/[databasename]";
	//mysql 默认的端口
	private static final String MYSQL_DEFAULT_PORT = "3306";
	//oracle 数据库连接的驱动
	private static final  String ORACLE_CLASS_NAME = "jdbc.oracle.driver.OracleDriver";
	//oracle 数据库连接的url
	private static final String ORACLE_CONN_PATH = "jdbc:oracle:thin:@[ipaddress]:[port]:[databasename]";
	//oracle 默认的端口
	private static final String ORACLE_DEFAULT_PORT = "1521";
	//sybase数据库连接的驱动
	private static final String SYBASE_CLASS_NAME = "com.sybase.jdbc2.jdbc.SybDriver";
	//sybase数据库连接的url
	private static final String SYBASE_CONN_PATH = "jdbc:sybase:Tds:[ipaddress]:[port]";
	//sybase默认的端口
	private static final String SYBASE_DEFAULT_PORT = "2638";
	//SQLServer数据库连接的驱动
	private static final String MICREOSOFT_CLASS_NAME = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	//SQLServer数据库连接url
	private static final String MICREOSOFT_CONN_PATH = "jdbc:microsoft:sqlserver://[ipaddress]:[port];databaseName=[databasename]";
	//SQLServer数据库连接默认端口
	private static final String MICREOSOFT_DEFAULT_PORT = "1433";
	//DB2 数据库连接的驱动
	private static final String DB2_CLASS_NAME = "Com.ibm.db2.jdbc.net.DB2Driver";
	//DB2 数据库连接的url
	private static final String DB2_CONN_PATH = "jdbc:db2://[ipaddress]:[port]/[databasename]";
	//DB2 数据库连接的默认端口
	private static final String DB2_DEFAULT_PORT = "6789";
	//hive数据库连接的驱动
	private static final String HIVE_CLASS_NAME = "org.apache.hadoop.hive.jdbc.HiveDriver";
	//hive数据库连接url
	private static final String HIVE_CONN_PATH = "jdbc:hive://[ipaddress]:[port]/[databasename]";
	//hive数据库连接默认的端口
	private static final String HIVE_DEFAULT_PORT = "10000";
	//xcloud 数据库连接的驱动
	private static final String XCLOUD_CLASS_NAME = "com.bonc.xcloud.jdbc.XCloudDriver";
	//xcloud 数据库连接的url
	private static final String XCLOUD_CONN_PATH = "jdbc:xcloud:@[ipaddress]:[port]/[databasename]";
	//xcloud数据库连接默认的端口
	private static final String XCLOUD_EFAULT_PORT = "8088";
	
	//用来替换的正则表达式
	private static final String regStr = "#[\\w\\d]+#";
	
	/**
	 * 创建连接
	 * @param ipAddress 地址
	 * @param jdbcUrl 连接url
	 * @param dbName 用户名
	 * @param dbPassword 密码
	 * @return 返回null表示创建链接失败，成功则返回Connection对象
	 */
	public static Connection createConnection(String driverClassName,String jdbcUrl,String dbName,String dbPassword){
		try{  
			//加载数据库驱动
			Class.forName(driverClassName);
			//获取数据库连接
			Connection con = DriverManager.getConnection(jdbcUrl, dbName, dbPassword);
		    return con;
	    }catch(ClassNotFoundException e){   
		    System.out.println("找不到驱动程序类 ，加载驱动失败");   
		    return null;
	    } catch (SQLException e) {
	    	System.out.println("数据库链接失败");
			return null;
		}   
	}
	
	/**
	 * 连接url
	 * @param ipAddress 连接ip
	 * @param port 端口号
	 * @param dataBaseName 数据库服务名
	 * @param dataBaseType 数据库类型
	 * @return
	 */
	public static String getConnectionUrl(String ipAddress,String port,String dataBaseName,String dataBaseType){
		String className = null;
		String connPath = null;
		String defaultPort = null;
		switch (dataBaseType) {
		//oracle
		case "1":
			className = ORACLE_CLASS_NAME;
			connPath = ORACLE_CONN_PATH;
			defaultPort = ORACLE_DEFAULT_PORT;
			break;
		//mysql
		case "2":
			className = MYSQL_CLASS_NAME;
			connPath = MYSQL_CONN_PATH;
			defaultPort = MYSQL_DEFAULT_PORT;
			break;
		//hive
		case "3":
			className = HIVE_CLASS_NAME;
			connPath = HIVE_CONN_PATH;
			defaultPort = HIVE_DEFAULT_PORT;
			break;
		//cloud
		case "4":
			className = XCLOUD_CLASS_NAME;
			connPath = XCLOUD_CONN_PATH;
			defaultPort = XCLOUD_EFAULT_PORT;
			break;
		default:
			return null;
		}
		
	    String url = connPath.replace("[ipaddress]", ipAddress).replace("[databasename]", dataBaseName);
	    if(port==null||"".equals(port)){
	    	url = url.replace("[port]", defaultPort);
	    }else{
	    	url = url.replace("[port]", port);
	    }
	    return url;
	}
	
	
	/**
	 * 根据数据库类型，获取ClassName
	 * @param type 数据库类型1.ORACLE 2.HIVE 3.MYSQL、4:XCLOUD
	 * @return
	 */
	public static String getClassName(String type){
		switch (type) {
		case "1":
			return  ORACLE_CLASS_NAME;
		case "2":
			return  MYSQL_CLASS_NAME;
		case "3":
			return  HIVE_CLASS_NAME;
		case "4":
			return  XCLOUD_CLASS_NAME;
		default:
			return null;
		}
		
	}
	
	/**
	 * 创建连接
	 * @param ipAddress 地址
	 * @param port 端口
	 * @param userName 用户名
	 * @param password 密码
	 * @param dataBaseName 数据库名
	 * @param dataBaseType 数据库类型
	 * @return 返回null表示创建链接失败，成功则返回Connection对象
	 */
	public static Connection createConnection(String ipAddress,String port,String userName,String password,String dataBaseName,String dataBaseType){
		String className = null;
		String connPath = null;
		String defaultPort = null;
		switch (dataBaseType) {
		//oracle
		case "1":
			className = ORACLE_CLASS_NAME;
			connPath = ORACLE_CONN_PATH;
			defaultPort = ORACLE_DEFAULT_PORT;
			break;
		//mysql
		case "2":
			className = MYSQL_CLASS_NAME;
			connPath = MYSQL_CONN_PATH;
			defaultPort = MYSQL_DEFAULT_PORT;
			break;
		//hive
		case "3":
			className = HIVE_CLASS_NAME;
			connPath = HIVE_CONN_PATH;
			defaultPort = HIVE_DEFAULT_PORT;
			break;
		default:
			return null;
		}
		
		try{   
		    Class.forName(className) ;   
		    String url = connPath.replace("[ipaddress]", ipAddress).replace("[databasename]", dataBaseName);
		    if(port==null||"".equals(port)){
		    	url = url.replace("[port]", defaultPort);
		    }else{
		    	url = url.replace("[port]", port);
		    }
		    Connection con = DriverManager.getConnection(url , userName , DefaultStringUtils.nullToString(password) );
		    return con;
	    }catch(ClassNotFoundException e){   
		    System.out.println("找不到驱动程序类 ，加载驱动失败");   
		    return null;
	    } catch (SQLException e) {
	    	System.out.println("数据库链接失败");
			return null;
		}   
	}
	
	/**
	 * 查询数据方法
	 * @param sql
	 * @param params
	 * @param conn
	 * @return 返回空表示数据库查询失败,成功返回数组对象
	 */
	public static List<Map> queryList(String sql,Map paramsMap,Connection conn) throws Exception{
		if(sql==null){
			return null;
		}
		List<Map> mapList = new ArrayList<Map>();
		Map map = null;
		String paramSql = sql;
		Statement stmt = null;
		ResultSet rs = null;
		String key;
		String value;
		Iterator<String> iter = paramsMap.keySet().iterator();
		while (iter.hasNext()) {
		    key = iter.next();
		    paramSql = paramSql.replace("#"+key+"#", DefaultStringUtils.nullToString(paramsMap.get(key)));
		    paramSql = paramSql.replace("#"+key.toLowerCase()+"#", DefaultStringUtils.nullToString(paramsMap.get(key)));
		    paramSql = paramSql.replace("#"+key.toUpperCase()+"#", DefaultStringUtils.nullToString(paramsMap.get(key)));
		}
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(paramSql);
			ResultSetMetaData rsmd = rs.getMetaData();
			while(rs.next()){
				map = new HashMap();
				for(int i = 1;i<=rsmd.getColumnCount();i++){
					map.put(rsmd.getColumnName(i), rs.getObject(i));
				}
				mapList.add(map);
			}
			return mapList;
		} catch (SQLException e) {
			System.out.println("创建查询失败");
			throw e;
		}finally{
			try {
				if(stmt!=null){
					stmt.close();
				}
				if(rs!=null){
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 查询数据方法
	 * @param sql
	 * @param params
	 * @param conn
	 * @return 返回空表示数据库查询失败,成功返回数组对象
	 */
	public static Map queryOne(String sql,Map paramsMap,Connection conn) throws Exception{
		if(sql==null){
			return null;
		}
		List<Map> mapList = new ArrayList<Map>();
		Map map = null;
		String paramSql = sql;
		Statement stmt = null;
		ResultSet rs = null;
		String key;
		String value;
		Iterator<String> iter = paramsMap.keySet().iterator();
		while (iter.hasNext()) {
		    key = iter.next();
		    value = paramsMap.get(key).toString();
		    paramSql = paramSql.replace("#"+key+"#", value);
		    paramSql = paramSql.replace("#"+key.toLowerCase()+"#", value);
		    paramSql = paramSql.replace("#"+key.toUpperCase()+"#", value);
		}
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(paramSql);
			ResultSetMetaData rsmd = rs.getMetaData();
			if(rs.next()){
				map = new HashMap();
				for(int i = 1;i<=rsmd.getColumnCount();i++){
					map.put(rsmd.getColumnName(i), rs.getObject(i));
				}
			}
			return map;
		} catch (SQLException e) {
			System.out.println("创建查询失败");
			throw e;
		}finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 查询数据方法
	 * @param sql
	 * @param params
	 * @param conn
	 * @return 返回空表示数据库查询失败,成功返回数组对象
	 */
	public static Object queryValue(String sql,Map paramsMap,Connection conn) throws Exception{
		if(sql==null){
			return null;
		}
		List<Map> mapList = new ArrayList<Map>();
		Map map = null;
		String paramSql = sql;
		Statement stmt = null;
		ResultSet rs = null;
		String key;
		String value;
		Iterator<String> iter = paramsMap.keySet().iterator();
		while (iter.hasNext()) {
		    key = iter.next();
		    value = paramsMap.get(key).toString();
		    paramSql = paramSql.replace("#"+key+"#", value);
		    paramSql = paramSql.replace("#"+key.toLowerCase()+"#", value);
		    paramSql = paramSql.replace("#"+key.toUpperCase()+"#", value);
		}
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(paramSql);
			ResultSetMetaData rsmd = rs.getMetaData();
			if(rs.next()){
				return rs.getObject(0);
			}
			return null;
		} catch (SQLException e) {
			System.out.println("创建查询失败");
			throw e;
		}finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 直接通过参数执行查询方法
	 * @param ipAddress
	 * @param port
	 * @param userName
	 * @param password
	 * @param dataBaseName
	 * @param dataBaseType
	 * @param sql
	 * @param params
	 * @return
	 */
//	public static Map queryOne(String ipAddress,String port,String userName,String password,String dataBaseName,int dataBaseType,String sql,Map params,Connection conn) throws Exception{
//		if(conn==null){
//			conn = createConnection(ipAddress, port, userName, password, dataBaseName, dataBaseType);
//		}
//		return queryOne(sql,params,conn);
//	}
	/**
	 * 执行操作方法
	 * @param sql
	 * @param params
	 * @param conn
	 * @return 0表示数据库操作成功,1表示失败
	 */
	public static int process(String sql,String[] params,Connection conn){
		String paramSql = sql;
		Statement stmt = null;
		if(params!=null){
			for(String param:params){
				paramSql = paramSql.replaceFirst(regStr, param);
			}
		}
		try {
			stmt = conn.createStatement();
			stmt.execute(paramSql);
			conn.commit();
			return 0;
		} catch (SQLException e) {
			System.out.println("执行操作失败");
			System.out.println(paramSql);
			e.printStackTrace();
			return 1;
		}finally{
			try {
				if(conn!=null){
					conn.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 直接通过参数执行操作
	 * @param ipAddress
	 * @param port
	 * @param userName
	 * @param password
	 * @param dataBaseName
	 * @param dataBaseType
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int process(String ipAddress,String port,String userName,String password,String dataBaseName,String dataBaseType,String sql,String[] params){
		Connection conn = createConnection(ipAddress, port, userName, password, dataBaseName, dataBaseType);
		return process(sql,params,conn);
	}
	/**
	 * 验证方法
	 * @param sql
	 * @param params
	 * @param conn
	 * @return 0表示数据库操作失败，1表示查询有结果,2表示无结果
	 */
	public static int validation(String sql,String[] params,Connection conn){
		Map map = null;
		String paramSql = sql;
		Statement stmt = null;
		ResultSet rs = null;
		if(params!=null){
			for(String param:params){
				paramSql = paramSql.replaceFirst(regStr, param);
			}
		}
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(paramSql);
			ResultSetMetaData rsmd = rs.getMetaData();
			if(rs.next()){
				return 0;
			}else{
				return 2;
			}
		} catch (SQLException e) {
			System.out.println("验证操作失败");
			e.printStackTrace();
			return 1;
		}finally{
			try {
				if(conn!=null){
					conn.close();
				}
				if(stmt!=null){
					stmt.close();
				}
				if(rs!=null){
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 验证方法
	 * @param sql
	 * @param params
	 * @param conn
	 * @return 0表示数据库操作失败，1表示查询有结果,2表示无结果
	 */
	public static String validation(String sql,Connection conn){
		Map map = null;
		String paramSql = sql;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeQuery(paramSql);
			return "查询成功";
		} catch (SQLException e) {
			System.out.println("验证操作失败");
			e.printStackTrace();
			return e.getMessage();
		}finally{
			try {
				if(conn!=null){
					conn.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 直接通过参数执行操作
	 * @param ipAddress
	 * @param port
	 * @param userName
	 * @param password
	 * @param dataBaseName
	 * @param dataBaseType
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int validation(String ipAddress,String port,String userName,String password,String dataBaseName,String dataBaseType,String sql,String[] params){
		Connection conn = createConnection(ipAddress, port, userName, password, dataBaseName, dataBaseType);
		return validation(sql,params,conn);
	}
	
	public static List<Map> getColumn(Connection conn,String tableName) throws SQLException{
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery("select * from "+tableName+" where rownum = 1 ");
		ResultSetMetaData md = rs.getMetaData();
//		String[] columns = new String[md.getColumnCount()-1];
		List resList = new ArrayList();
		Map map = null;
		for(int i=1;i< md.getColumnCount();i++){
			map = new HashMap();
			map.put("column", md.getColumnLabel(i));
			map.put("type", md.getColumnTypeName(i));
			resList.add(map);
		}
		return resList;
	}
	
	public static boolean checkTable(Connection conn,String tableName){
		ResultSet rs = null;
		try {
			DatabaseMetaData meta = (DatabaseMetaData)conn.getMetaData();
			rs = meta.getTables(null, null,tableName.toUpperCase(), new String[]{"TABLE"});
			if(rs.next()){
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] arg) throws Exception {
		Connection conn = DataSourceUtil.createConnection("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@133.160.39.211:1522:hndss2", "hn_bkepler", "hnbkepler_1");
		System.out.println(getColumn(conn,"pure_user"));
		conn.close();
	}
}
