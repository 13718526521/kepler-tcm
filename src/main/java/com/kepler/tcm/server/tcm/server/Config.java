package com.kepler.tcm.server.tcm.server;

import com.kepler.tcm.server.core.util.FileTools;
import com.kepler.tcm.server.core.util.Resource;
import java.io.File;
import java.util.Map;

public class Config {
	private String tcmconf = "";
	private String agentconf = "";
	private static String base = "";

	public static void setBase(String base, int type, String server) {
		base = base;
		if (type == 1)
			copyAgent();
		if (type == 2)
			copyServer(server);
	}

	public static void copyAgent() {
		FileTools.copyFile(new File(base + "/conf/agent.conf"), new File(base + "/conf/original/agent.conf"));
	}

	public static void copyServer(String server) {
		FileTools.copyFile(new File(base + "/servers/" + server + "/conf/server.conf"),
				new File(base + "/servers/" + server + "/conf/original/server.conf"));
	}

	public static Resource getAgentConfig(boolean original) throws Exception {
		if (original) {
			return Resource.getResource(base + "/conf/original/agent.conf", "");
		}
		return Resource.getResource(base + "/conf/agent.conf", "");
	}

	public static Resource getServerConfig(String server, boolean original) throws Exception {
		if (original)
			return Resource.getResource(base + "/servers/" + server + "/conf/original/server.conf", "");
		return Resource.getResource(base + "/servers/" + server + "/conf/server.conf", "");
	}

	public static String getCharset(String server) {
		try {
			return getServerConfig(server, true).getStr("charset", "UTF-8");
		} catch (Exception e) {
		}
		return "UTF-8";
	}

	public static String getServerMemo(String server) throws Exception {
		return FileTools.getFileText(base + "/servers/" + server + "/conf/memo.txt", getCharset(server));
	}

	public static void setServerConfig(String server, Map map) throws Exception {
		String memo = (String) map.get("memo");

		if (memo != null) {
			FileTools.setFileText(base + "/servers/" + server + "/conf/memo.txt", memo, getCharset(server));
		}

		map.remove(memo);

		Util.setFields(base + "/servers/" + server + "/conf/server.conf", "", map);
	}
}