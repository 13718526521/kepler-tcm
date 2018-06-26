 package com.kepler.tcm.core.agent;

import java.io.File;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kepler.tcm.core.server.Server;
import com.kepler.tcm.core.server.ServerListener;
import com.kepler.tcm.core.util.Config;
import com.kepler.tcm.core.util.Resource;
/**
 * 代理服务器启动入口
 * @author wangsp
 * @date 2018年6月15日
 * @version V1.0
 */
public class Agent {
	
	private static final Logger logger = LoggerFactory.getLogger(Agent.class);
	
	
	public static final String TCM_BASE = "..";
	
	public static String agentPort = "1098";
	
	public static String host = "127.0.0.1";
	
	public static String logPath = "../logs";
	
	public static String charset = "UTF-8";

	static RemoteAgent remoteAgent = null;

	public static void main(String[] args) throws Exception {
		
		
		Config.setBasePath("..", 1, "");

		Resource resource = Config.getAgentConfig(true);

		host = resource.getStr("host", Server.host);
		
		agentPort = resource.getStr("agentPort", agentPort);

		logPath = resource.getStr("logPath", "");
		if (logPath.length() == 0)
			logPath = "../logs";
		System.setProperty("log.path", logPath);
		//DOMConfigurator.configure("../conf/log4j.xml");

		charset = resource.getStr("charset", charset);
		
		String rmi = "rmi://" + host + ":" + agentPort + "/tcm_agent";
		
		Registry registry =  null ;
		try{
			/**启动注册服务器，使用了这个语句就不再需要在命令行环境中
             *启动registry服务了
            */
            registry = LocateRegistry.getRegistry();
            /* 若没有获得连接，则此句会抛出异常，后面在捕获后进行相关处理 */
            registry.list();
            
            logger.info("TCM Agent 2.6 the exist server :" + rmi);
		}catch (RemoteException re) {
            try {
            	
            	LocateRegistry.createRegistry(Integer.parseInt(agentPort));
         
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
				
		remoteAgent = new RemoteAgentImpl();

		Naming.rebind(rmi, remoteAgent);
		
		logger.info("TCM Agent 2.6 started at " + rmi);

		new ServerListener(resource.getInt("monitorPort", 1097), true).start();
		
		System.out.println(new Date().toLocaleString() + " TCM Agent listening on " + agentPort);
		logger.info("TCM Agent listening on " + agentPort);

		System.out.println("System logs write to "
				+ new File(new StringBuffer(String.valueOf(logPath)).append("/agent.log").toString())
						.getCanonicalPath());
		boolean defaultStreamEnable = System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0;

		//System.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO, defaultStreamEnable), true));
		//System.setErr(new PrintStream(new LoggingOutputStream(logger, Level.ERROR, defaultStreamEnable), true));
	}
}