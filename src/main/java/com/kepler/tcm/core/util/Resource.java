package com.kepler.tcm.core.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
/**
 * 资源加载
 * @author wangsp
 * @date 2018年6月15日
 * @version V1.0
 */
public class Resource extends BaseResource {

	private HashMap map = null;

	public static Resource getResource(String fileName, String charsetName)
			throws Exception {
		return (Resource) getResource(Resource.class.getName(), fileName,
				charsetName);
	}

	protected void handleResrouce(InputStreamReader r) throws Exception {
		BufferedReader br = new BufferedReader(r);
		this.map = new HashMap(16);
		String line = null;
		String name = null;
		String value = null;
		boolean b = true;
		while ((line = br.readLine()) != null) {
			line = line.trim();

			if ((b) && (line.length() > 0) && (line.charAt(0) == 65279)
					&& (this.charset != null) && (this.charset.equals("UTF-8"))) {
				line = line.substring(1);
				b = false;
			}
			if ((!line.equals("")) && (line.charAt(0) != '#')
					&& (line.charAt(0) != ';')) {
				int n = line.indexOf('=');
				if (n == -1) {
					name = line;
					value = "";
				} else {
					name = line.substring(0, n);
					value = line.substring(n + 1);
				}

				this.map.put(name.trim(), value.trim());
			}
		}

		br.close();
	}

	public String getString(String name) {
		return (String) this.map.get(name);
	}

	public String getString(String name, String defaultValue) {
		String value = (String) this.map.get(name);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	public String getStr(String name, String defaultValue) {
		String value = (String) this.map.get(name);
		if ((value == null) || (value.length() == 0)) {
			value = defaultValue;
		}
		return value;
	}

	public int getInt(String name, int def) {
		return Convert.toInt(this.map.get(name), def);
	}

	public Enumeration getKeys() {
		return null;
	}
}
