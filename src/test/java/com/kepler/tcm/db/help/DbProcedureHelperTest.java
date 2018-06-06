package com.kepler.tcm.db.help;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.kepler.tcm.db.DataSourceManager;
import com.kepler.tcm.domain.LocalSource;

public class DbProcedureHelperTest {

	DataSourceManager dm = null ;
	
	@Before
	public void setUp(){
		dm = DataSourceManager.getInstance();
	}

	@Test
	public void testProcIntOutSimple() {
		
		//oracle test
		LocalSource dsOracle = new LocalSource();
		dsOracle.setDbName("kepler_sea_test");
		dsOracle.setDbPwd("kepler_sea_test");
		dsOracle.setSourceType("1");
		dsOracle.setSourceUrl("jdbc:oracle:thin:@172.16.14.25:1521:bonc1");
		dsOracle.setId("oracle");
		for (int i = 0; i < 6; i++) {
			try {
				/*//Connection conn = dm.getConnection(dsOracle);
				DbUtilHelpper dbUtilHelpper = DbUtilHelpper.getInstance(dsOracle);
				String sql = "select * from sys_user";
				Object result = dbUtilHelpper.selectMap(sql,null);
				System.out.println(JSONObject.toJSON(result));*/
				Connection conn = dm.getConnection(dsOracle);
				Object[] param = {33};
				Map map=new HashMap();
				String sql="{call PRO_RETURN_INT_RESULT(?,?)}";
				DbProcedureHelper jp=DbProcedureHelper.getInstance(conn);
				System.out.println(jp.procIntOutSimple(sql, param));
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	@Test
	public void testProcStringOutSimple() {
		
		//oracle test
		LocalSource dsOracle = new LocalSource();
		dsOracle.setDbName("kepler_sea_test");
		dsOracle.setDbPwd("kepler_sea_test");
		dsOracle.setSourceType("1");
		dsOracle.setSourceUrl("jdbc:oracle:thin:@172.16.14.25:1521:bonc1");
		dsOracle.setId("oracle");
		for (int i = 0; i < 6; i++) {
			try {
				/*//Connection conn = dm.getConnection(dsOracle);
				DbUtilHelpper dbUtilHelpper = DbUtilHelpper.getInstance(dsOracle);
				String sql = "select * from sys_user";
				Object result = dbUtilHelpper.selectMap(sql,null);
				System.out.println(JSONObject.toJSON(result));*/
				Connection conn = dm.getConnection(dsOracle);
				Object[] param = { "ssss"};
				Map map=new HashMap();
				String sql="{call PRO_RETURN_STRING_RESULT(?,?)}";
				DbProcedureHelper jp=DbProcedureHelper.getInstance(conn);
				System.out.println(jp.procStringOutSimple(sql, param));
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


	@Test
	public void testOracleProcOutMap() {
		//oracle test
		LocalSource dsOracle = new LocalSource();
		dsOracle.setDbName("kepler_sea_test");
		dsOracle.setDbPwd("kepler_sea_test");
		dsOracle.setSourceType("1");
		dsOracle.setSourceUrl("jdbc:oracle:thin:@172.16.14.25:1521:bonc1");
		dsOracle.setId("oracle");
		for (int i = 0; i < 6; i++) {
			try {
				/*//Connection conn = dm.getConnection(dsOracle);
				DbUtilHelpper dbUtilHelpper = DbUtilHelpper.getInstance(dsOracle);
				String sql = "select * from sys_user";
				Object result = dbUtilHelpper.selectMap(sql,null);
				System.out.println(JSONObject.toJSON(result));*/
				Connection conn = dm.getConnection(dsOracle);
				Object[] param = { };
				Map map=new HashMap();
				String sql="{call PRO_RETURN_REFCURSOR_RESULT(?)}";
				DbProcedureHelper jp=DbProcedureHelper.getInstance(conn);
				System.out.println(jp.oracleProcOutMap(sql, param));
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void oracleProcOutPageQuery() {
		//oracle test
		LocalSource dsOracle = new LocalSource();
		dsOracle.setDbName("kepler_sea_test");
		dsOracle.setDbPwd("kepler_sea_test");
		dsOracle.setSourceType("1");
		dsOracle.setSourceUrl("jdbc:oracle:thin:@172.16.14.25:1521:bonc1");
		dsOracle.setId("oracle");
		for (int i = 0; i < 6; i++) {
			try {
				/*//Connection conn = dm.getConnection(dsOracle);
				DbUtilHelpper dbUtilHelpper = DbUtilHelpper.getInstance(dsOracle);
				String sql = "select * from sys_user";
				Object result = dbUtilHelpper.selectMap(sql,null);
				System.out.println(JSONObject.toJSON(result));*/
				Connection conn = dm.getConnection(dsOracle);
				Object[] param = { };
				Map map=new HashMap();
				String sql="{call PRO_PAGE_QUERY(?,?,?,?,?,?,?,?,?)}";
				DbProcedureHelper jp=DbProcedureHelper.getInstance(conn);
				System.out.println(JSONObject.toJSON(jp.oracleProcOutPageQuery(sql, "SEA_INTERFACE_INFO", "", "ID", "desc", 1, 10)));
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testOracleProcOutList() {
		//oracle test
		LocalSource dsOracle = new LocalSource();
		dsOracle.setDbName("kepler_sea_test");
		dsOracle.setDbPwd("kepler_sea_test");
		dsOracle.setSourceType("1");
		dsOracle.setSourceUrl("jdbc:oracle:thin:@172.16.14.25:1521:bonc1");
		dsOracle.setId("oracle");
		for (int i = 0; i < 6; i++) {
			try {
				/*//Connection conn = dm.getConnection(dsOracle);
				DbUtilHelpper dbUtilHelpper = DbUtilHelpper.getInstance(dsOracle);
				String sql = "select * from sys_user";
				Object result = dbUtilHelpper.selectMap(sql,null);
				System.out.println(JSONObject.toJSON(result));*/
				Connection conn = dm.getConnection(dsOracle);
				Object[] param = { };
				Map map=new HashMap();
				String sql="{call PRO_RETURN_REFCURSOR_RESULT(?)}";
				DbProcedureHelper jp=DbProcedureHelper.getInstance(conn);
				System.out.println(jp.oracleProcOutList(sql, param));
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testMysqlProcOutMap() {
		fail("Not yet implemented");
	}

	@Test
	public void testMysqlProcOutList() {
		fail("Not yet implemented");
	}

	@Test
	public void testMysqlProcOutRs() {
		fail("Not yet implemented");
	}

	@Test
	public void testProcInOut() {
		fail("Not yet implemented");
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

}
