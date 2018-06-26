package com.kepler.tcm.core.startup;

import java.io.File;
import java.lang.reflect.Method;
import java.rmi.RMISecurityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kepler.tcm.core.loader.TCMClassLoader;

/**
 * 启动入口类
 * 
 * @author wangsp
 * @date 2018年6月6日
 * @version V1.0
 */
public class Bootstrap {

	private static Logger log = LoggerFactory.getLogger(Bootstrap.class);

	public static boolean bStarted = false;

	public static void main(String[] args) throws Exception {
		log.info("Loading ");
		
		log.info(new File("../plugins/").getCanonicalPath());

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}
			
		//com.kepler.tcm.core.server.Server
		String progClass = args[0];
		String[] progArgs = new String[args.length - 1];
		System.arraycopy(args, 1, progArgs, 0, progArgs.length);

		TCMClassLoader tcl = new TCMClassLoader();

		//String[][] paths = { { "../../common/core", "classes" } };
		
		String[][] paths = { { "../../lib/*jar"} };

		tcl.setPath(paths);

		Class clas = tcl.loadClass(progClass);
		
		//log.info(clas.getTypeName());
		
		Class[] mainArgType = { new String[0].getClass() };
		Method main = clas.getMethod("main", mainArgType);
		Object[] argsArray = { progArgs };
		main.invoke(null, argsArray);
	}
}
