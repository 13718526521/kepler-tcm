package com.kepler.tcm.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.kepler.tcm.db.help.DbUtilHelper;
import com.kepler.tcm.domain.LocalSource;

public class DataSourceManagerTest {
	
	DataSourceManager dm = null ;
	
	@Before
	public void setUp(){
		dm = DataSourceManager.getInstance();
	}

	@Test
	public void testGetInstance() {
		
		//oracle test
/*		LocalSource dsOracle = new LocalSource();
		dsOracle.setDbName("kepler_circle");
		dsOracle.setDbPwd("kepler_circle");
		dsOracle.setSourceType("1");
		dsOracle.setSourceUrl("jdbc:oracle:thin:@172.16.14.25:1521:bonc1");
		dsOracle.setId("oracle");
		for (int i = 0; i < 6; i++) {
			try {
				//Connection conn = dm.getConnection(dsOracle);
				DbUtilHelpper dbUtilHelpper = DbUtilHelpper.getInstance(dsOracle);
				String sql = "select * from sys_user";
				Object result = dbUtilHelpper.selectMap(sql,null);
				System.out.println(JSONObject.toJSON(result));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/

		
		
/*      //mysql test
		LocalSource dsMysql = new LocalSource();
		dsMysql.setDbName("root");
		dsMysql.setDbPwd("root");
		dsMysql.setSourceType("2");
		dsMysql.setSourceUrl("jdbc:mysql://localhost:3306/jspxcms605");
		dsMysql.setId("mysql");
		
		for (int i = 0; i < "111".length(); i++) {
			try {
				Connection conn = dm.getConnection(dsMysql);
				DbUtilHelpper dbUtilHelpper = DbUtilHelpper.getInstance(conn);
				String sql = "SELECT 	* FROM jspxcms605.cms_ad_slot";
				Object result = dbUtilHelpper.selectMap(sql,null);
				System.out.println(JSONObject.toJSON(result));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}*/

		//多线程测试
		for (int i = 0; i < "1".length(); i++) {
			System.out.println("======"+i);
			new Thread(new QueryThreads(dm)).start();;
		}
		
	}
	
}

/**
 * 模拟并发多线程测试
 * @author 作者 E-mail: wangshuping@bonc.com.cn
 * @date 创建时间：2017年4月2日 下午12:36:05 
 * @version V-1.0.0
 */
class QueryThreads implements Runnable{
	
	DataSourceManager dm ;
	
	QueryThreads(DataSourceManager dm){
		this.dm = dm ;
	}

	@Override
	public void run() {
		LocalSource dsOracle = new LocalSource();
		dsOracle.setDbName("kepler_circle");
		dsOracle.setDbPwd("kepler_circle");
		dsOracle.setSourceType("1");
		dsOracle.setSourceUrl("jdbc:oracle:thin:@172.16.14.25:1521:bonc1");
		dsOracle.setId("oracle");
		for (int i = 0; i < "1".length(); i++) {
			try {
				System.out.println("内部次数"+i);
				Connection conn = dm.getConnection(dsOracle);
				DbUtilHelper dbUtilHelpper = DbUtilHelper.getInstance(conn);
				String sql = "SELECT 	* FROM jspxcms605.cms_ad_slot";
				Object result = dbUtilHelpper.selectMap(sql,null);
				System.out.println(JSONObject.toJSON(result));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
}
