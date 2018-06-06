package com.kepler.tcm.db.util;

import com.kepler.tcm.db.constants.DBConstant;
/**
 * 数据库工具类
 * @author wangsp
 * @date 2017年4月1日
 * @version V1.0
 */
public class DataBaseUtil {
	
	
    /**
     * 获取数据库链接地址
     * @param ipAddress
     * @param port
     * @param dataBaseName
     * @param dataBaseType 1：oracle、2：mysql、3：hive、4：xcloud
     * @return
     */
    public static String getConnectionUrl(String ipAddress,String port,String dataBaseName,int dataBaseType){
        String connPath = null;
        String defaultPort = null;
        switch (dataBaseType) {
        case 1:
            connPath = DBConstant.ORACLECONNPATH;
            defaultPort = DBConstant.ORACLEDEFAULTPORT;
            break;
        case 2:
            connPath = DBConstant.HIVECONNPATH;
            defaultPort = DBConstant.HIVEDEFAULTPORT;
            break;
        case 3:
            connPath = DBConstant.MYSQLCONNPATH;
            defaultPort = DBConstant.MYSQLDEFAULTPORT;
            break;
        case 4:
            connPath = DBConstant.XCLOUDCONNPATH;
            defaultPort = DBConstant.XCLOUDDEFAULTPORT;
            break;
        default:
            return null;
        }
        
        String url = connPath.replace("[ipaddress]", ipAddress).replace("[databasename]", dataBaseName);
        if(port == null || "".equals(port)){
            url = url.replace("[port]", defaultPort);
        }else{
            url = url.replace("[port]", port);
        }
        return url;
    }
    
    
    /**
     * 根据数据库类型，获取ClassName
     * @param dataBaseType 数据库类型  1：oracle、2：mysql、3：hive、4：xcloud
     * @return
     */
    public static String getDriverName(int dataBaseType){
        switch (dataBaseType) {
        case 1:
            return  DBConstant.ORACLECLASSNAME;
        case 2:
            return  DBConstant.MYSQLCLASSNAME;
        case 3:
            return  DBConstant.HIVECLASSNAME;
        case 4:
            return  DBConstant.XCLOUDCLASSNAME;
        default:
            return null;
        }
        
    }
    

}
