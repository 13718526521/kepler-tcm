package com.kepler.tcm.core.util;

import java.io.File;
import java.util.Map;

public class Config {
	
	private String tcmconf = "";
	
	private String agentconf = "";
	
	/**
	 * 
	 */
	private static String basePath = "..";

	
	/**
	 * 设置服务根路径
	 * @param basePath 
	 * @param type  1-代理服务 ； 2--应用服务
	 * @param server  服务名
	 */
	public static  void setBasePath(String basePath, int type, String server) {
		Config.basePath = basePath;
		if (type == 1)
			copyAgent();
		if (type == 2)
			copyServer(server);
	}

	/**
	 * 备份代理服务原配置/conf/agent.conf to /conf/original/agent.conf
	 */
	public static void copyAgent() {
		FileTools.copyFile(new File(basePath + "/conf/agent.conf"), new File(basePath
				+ "/conf/original/agent.conf"));
	}

	/**
	 * 备份服务原配置/conf/server.conf to /conf/original/server.conf
	 * @param server
	 */
	public static void copyServer(String server) {
		FileTools.copyFile(new File(basePath + "/servers/" + server
				+ "/conf/server.conf"), new File(basePath + "/servers/" + server
				+ "/conf/original/server.conf"));
	}

	public static Resource getAgentConfig(boolean original) throws Exception {
		if (original) {
			return Resource.getResource(basePath + "/conf/original/agent.conf", "");
		}
		return Resource.getResource(basePath + "/conf/agent.conf", "");
	}

	public static Resource getServerConfig(String server, boolean original)
			throws Exception {
		if (original)
			return Resource.getResource(basePath + "/servers/" + server
					+ "/conf/original/server.conf", "");
		return Resource.getResource(basePath + "/servers/" + server
				+ "/conf/server.conf", "");
	}

	public static String getCharset(String server) {
		try {
			return getServerConfig(server, true).getStr("charset", "UTF-8");
		} catch (Exception e) {
		}
		return "UTF-8";
	}

	public static String getServerMemo(String server) throws Exception {
		return FileTools.getFileText(basePath + "/servers/" + server
				+ "/conf/memo.txt", getCharset(server));
	}

	public static void setServerConfig(String server, Map map) throws Exception {
		String memo = (String) map.get("memo");

		if (memo != null) {
			FileTools.setFileText(basePath + "/servers/" + server
					+ "/conf/memo.txt", memo, getCharset(server));
		}

		map.remove(memo);

		Util.setFields(basePath + "/servers/" + server + "/conf/server.conf", "",
				map);
	}
}