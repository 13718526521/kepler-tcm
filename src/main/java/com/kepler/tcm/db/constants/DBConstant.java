package com.kepler.tcm.db.constants;

public class DBConstant {
	//定义数据库连接常量
	//public static final String MYSQLCLASSNAME = "org.gjt.mm.mysql.Driver";
	
	public static final String MYSQLCLASSNAME = "com.mysql.jdbc.Driver";
    
	public static final String MYSQLCONNPATH = "jdbc:mysql://[ipaddress]:[port]/[databasename]";
    
	public static final String MYSQLDEFAULTPORT = "3306";
    
	public static final String POSTGRESQLCLASSNAME = "org.postgresql.Driver";
    
	public static final String POSTGRECONNPATH = "jdbc:postgresql://[ipaddress]:[port]/[databasename]";
    
	public static final String POSTGREDEFAULTPORT = "5432";
    
	public static final String ORACLECLASSNAME = "oracle.jdbc.driver.OracleDriver";
    
	public static final String ORACLECONNPATH = "jdbc:oracle:thin:@[ipaddress]:[port]:[databasename]";
    
	public static final String ORACLEDEFAULTPORT = "1521";
    
	public static final String SYBASECLASSNAME = "com.sybase.jdbc2.jdbc.SybDriver";
    
	public static final String SYBASECONNPATH = "jdbc:sybase:Tds:[ipaddress]:[port]";
    
	public static final String SYBASEDEFAULTPORT = "2638";
    
	public static final String MICREOSOFTCLASSNAME = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
    
	public static final String MICREOSOFTCONNPATH = "jdbc:microsoft:sqlserver://[ipaddress]:[port];databaseName=[databasename]";
    
	public static final String MICREOSOFTDEFAULTPORT = "1433";
    
	public static final String DB2CLASSNAME = "Com.ibm.db2.jdbc.net.DB2Driver";
    
    public static final String DB2CONNPATH = "jdbc:db2://[ipaddress]:[port]/[databasename]";
    
    public static final String DB2DEFAULTPORT = "6789";
    
    public static final String HIVECLASSNAME = "org.apache.hadoop.hive.jdbc.HiveDriver";
    
    public static final String HIVECONNPATH = "jdbc:hive://[ipaddress]:[port]/[databasename]";
    
    public static final String HIVEDEFAULTPORT = "10000";
    
    public static final String XCLOUDCLASSNAME = "com.bonc.xcloud.jdbc.XCloudDriver";
    
    public static final String XCLOUDCONNPATH = "jdbc:xcloud:@[ipaddress]:[port]/[databasename]";
    
    public static final String XCLOUDDEFAULTPORT = "8088";
    
    //用来替换的正则表达式
    public static final String regStr = "#[\\w\\d]+#";

}
