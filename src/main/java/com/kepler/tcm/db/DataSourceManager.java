package com.kepler.tcm.db;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.alibaba.druid.pool.DruidDataSource;
import com.kepler.tcm.db.util.DataBaseUtil;
import com.kepler.tcm.domain.LocalSource;
import com.kepler.tcm.util.DefaultStringUtils;
 
 
/**
 * 多数据源管理器
 * @author wangsp
 * @date 2017年4月1日
 * @version V1.0
 */
public class DataSourceManager  implements EnvironmentAware{
  
    
    private static Logger log = LoggerFactory.getLogger(DataSourceManager.class);
    
    private RelaxedPropertyResolver propertyResolver;  

    //1、准备一个集合  key 数据源类型  value 数据源连接池实例
    private  Map<String,DataSource>   dataSourceMap = new HashMap<String, DataSource>();
    
  //  private  Connection conn = null ;
    
    /**
     * 设置私有默认构造器
     */
    private DataSourceManager() {
        
    }   
    
    /**
     * 静态内部类，实现单例，在类初始化的时候，实例化对象，省去new 关键字的消耗
     * @author wangsp
     * @date 2017年4月1日
     * @version V1.0
     */
    public static class Holder {
        
        private static DataSourceManager instance = new DataSourceManager();    
    }
    
    public static DataSourceManager getInstance() { 
        
        // 外围类能直接访问内部类（不管是否是静态的）的私有变量    
        return Holder.instance;    
    }
    
    
    @Override  
	public void setEnvironment(Environment env) {  
    	this.propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");  
    }
    
    /**
     * 获取数据库连接，在容器没有的情况下，始终保证一个线程创建数据源
     * @param ls 数据库配置信息
     * @return   Connection 返回数据库连接
     * @throws SQLException
     */
    public Connection  getConnection(LocalSource ls) throws SQLException{
    	Connection conn = null ;
        //1、根据数据源唯一标识，判断是否已有该数据库连接池
        if(!dataSourceMap.isEmpty() && dataSourceMap.containsKey(ls.getId())){
        	conn  = dataSourceMap.get(ls.getId()).getConnection(); 
        }else{
        	  if(conn == null ){
      			//当容器中没有获取到连接时，创建数据库连接，同步块保证只有一个线程创建数据源连接
                  synchronized(this){
      			    //防止其他线程再创建数据库连接，首先再获取一次连接
      				if(!dataSourceMap.isEmpty() && dataSourceMap.containsKey(ls.getId())){
      					conn = dataSourceMap.get(ls.getId()).getConnection(); 
      				}
      				if(conn == null ){
      					//创建数据库连接池
      					DataSource ds =  this.createDataSource(ls);
      					conn = ds.getConnection();
      					 //保存数据库连接池
      					dataSourceMap.put(ls.getId(), ds);
      					log.debug("thread-id = {},thread-name = {},put key = [{}] data source",
      							Thread.currentThread().getId(),Thread.currentThread().getName(),ls.getId());
      				}
      			}           
              }
        }
        return conn;
    }
 
    
    /**
     * 创建数据库连接
     * @param ipAddress 服务器地址
     * @param port   服务器端口号
     * @param userName 数据库用户名
     * @param password 数据库用户密码
     * @param dataBaseName 数据库名
     * @param dataBaseType 数据库类型；1：oracle、2：mysql、3：hive、4：xcloud
     * @return 返回null表示创建链接失败，成功则返回Connection对象
     * @throws SQLException 
     */
    public  Connection getConnection(String ipAddress,String port,
        String userName,String password,String dataBaseName,int dataBaseType) throws SQLException{
        
        return createDataSource(ipAddress,port,userName,password,dataBaseName,dataBaseType).getConnection();
    	
    }
    

    
    /**
     * 根据数据源信息，创建数据库连接池
     * @param ls  数据源配置信息
     * @return DruidDataSource 返回数据源
     * @throws
     */
    public synchronized DataSource createDataSource(LocalSource ls) {  
         log.debug("Configruing Base DataSource,"
         		+ "Info : [URL={},USERNAME={},PASSWRD=protected]",ls.getSourceUrl(),ls.getDbName());
         
         DruidDataSource datasource = new DruidDataSource();
         datasource.setUrl(ls.getSourceUrl());  
         datasource.setDriverClassName(DataBaseUtil.getDriverName(Integer.valueOf(ls.getSourceType())));
         datasource.setUsername(ls.getDbName());  
         datasource.setPassword(ls.getDbPwd());
         if(!StringUtils.isEmpty(ls.getInitialSize()) 
        		 && StringUtils.isNumeric(ls.getInitialSize())){
             datasource.setInitialSize(Integer.valueOf(ls.getInitialSize())); 
         }
         if(!StringUtils.isEmpty(ls.getMaxActive())
        		 && StringUtils.isNumeric(ls.getMaxActive())){
        	 datasource.setMaxActive(Integer.valueOf(ls.getMaxActive()));
         }
		 if(!StringUtils.isEmpty(ls.getMaxIdle())
				 && StringUtils.isNumeric(ls.getMaxIdle())){
			 datasource.setMinIdle(Integer.valueOf(ls.getMaxIdle()));
		 }
		 if(!StringUtils.isEmpty(ls.getMaxWait())
				 && StringUtils.isNumeric(ls.getMaxWait())){
			 datasource.setMaxWait(Integer.valueOf(ls.getMaxWait()));
		 }

		//超过时间限制是否回收
		 datasource.setRemoveAbandoned(true);
		 //关闭abanded连接时输出错误日志
		 datasource.setLogAbandoned(true);
		 //超时时间；单位为秒。180秒=3分钟 //福建改成5分钟
		 datasource.setRemoveAbandonedTimeout(120);
		 
         log.debug("create data source config info : [DataBaseType = {},URL={},USERNAME={},"
         		+ "PASSWRD=protected]",ls.getSourceType(),ls.getSourceUrl(),ls.getDbName());
         return datasource;  
     }

    /**
     *  根据数据源信息，创建数据库连接池
     * @param ipAddress 服务器地址
     * @param port   服务器端口号
     * @param userName 数据库用户名
     * @param password 数据库用户密码
     * @param dataBaseName 数据库名
     * @param dataBaseType 数据库类型；1：oracle、2：mysql、3：hive、4：xcloud
     @return DruidDataSource 返回数据源
     * @throws
     */
    public synchronized DataSource createDataSource(String ipAddress,String port,
            String userName,String password,String dataBaseName,int dataBaseType) {  
         log.debug("Configruing Base DataSource");  
         DruidDataSource datasource = new DruidDataSource();  
         datasource.setUrl(DataBaseUtil.getConnectionUrl(ipAddress, port,
        		 dataBaseName, dataBaseType));  
         datasource.setDriverClassName(DataBaseUtil.getDriverName(dataBaseType));
         datasource.setUsername(userName);  
         datasource.setPassword(password);
         datasource.setInitialSize(Integer.valueOf(propertyResolver.getProperty("initialSize")));
		 datasource.setMaxActive(Integer.valueOf(propertyResolver.getProperty("maxActive")));
		 datasource.setMinIdle(Integer.valueOf(propertyResolver.getProperty("minIdle")));
		 datasource.setMaxWait(Integer.valueOf(propertyResolver.getProperty("maxWait")));
		 log.debug("create data source config info : [DataBaseType = {},URL={},USERNAME={},"
	         		+ "PASSWRD=protected]",dataBaseType,DataBaseUtil.getConnectionUrl(ipAddress, port,
	               		 dataBaseName, dataBaseType),userName);
         return datasource;  
     }
     
	/**
	 *  创建JDBC连接
	 * 	 * @param ls
	 * @return 返回null表示创建链接失败，成功则返回Connection对象

	 */
	public static Connection createConnection(LocalSource ls){
	    try{   
	        Class.forName(DataBaseUtil.getDriverName(Integer.valueOf(ls.getSourceType()))) ;   ;
	        Connection con = DriverManager.getConnection(ls.getSourceUrl() ,DefaultStringUtils.nullToString(ls.getDbName()) ,
	        		DefaultStringUtils.nullToString(ls.getDbPwd()) );
	        return con;
	    }catch(ClassNotFoundException e){
			e.printStackTrace();
	    	log.error("找不到驱动程序类 ，加载驱动失败");   
	        return null;
	    } catch (SQLException e) {
			e.printStackTrace();
	    	log.error("数据库链接失败");
	        return null;
	    }   
	}

	/**
	 * 创建JDBC连接
	 * @param ipAddress 地址
	 * @param port 端口
	 * @param userName 用户名
	 * @param password 密码
	 * @param dataBaseName 数据库名
	 * @param dataBaseType 数据库类型
	 * @return 返回null表示创建链接失败，成功则返回Connection对象
	 */
	public static Connection createConnection(String ipAddress,String port,
	        String userName,String password,String dataBaseName,int dataBaseType){
	    try{   
	        Class.forName(DataBaseUtil.getDriverName(dataBaseType)) ;   
	        String url = DataBaseUtil.getConnectionUrl(ipAddress, port,dataBaseName, dataBaseType);
	        Connection con = DriverManager.getConnection(url , userName , DefaultStringUtils.nullToString(password) );
	        return con;
	    }catch(ClassNotFoundException e){   
	    	log.error("找不到驱动程序类 ，加载驱动失败");   
	        return null;
	    } catch (SQLException e) {
	    	log.error("数据库链接失败");
	        return null;
	    }   
	}
    
}