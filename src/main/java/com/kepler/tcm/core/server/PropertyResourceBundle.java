package com.kepler.tcm.core.server;

import com.kepler.tcm.core.util.Resource;
import com.kepler.tcm.core.util.StringTools;
import com.kepler.tcm.core.util.Util;

public class PropertyResourceBundle {
	private Resource r = null;

	public PropertyResourceBundle(String path) throws Exception {
		this.r = Resource.getResource(path, Util.getCharset());
	}

	public final String getString(String strKey) {
		try {
			return StringTools.decodeEnter(this.r.getString(strKey));
		} catch (Exception e) {
			System.out.println(e);
		}
		return "";
	}

	public final String getStr(String strKey, String def) {
		try {
			return this.r.getStr(strKey, def);
		} catch (Exception e) {
			System.out.println(e);
		}
		return "";
	}

	public void close() throws Exception {
	}

	public static void main(String[] args) throws Exception {
		Resource r = Resource.getResource("c:/plugin.properties", "UTF-8");
		System.out.println(r.getString("pluginName"));
	}
}