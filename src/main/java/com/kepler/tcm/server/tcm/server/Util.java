package com.kepler.tcm.server.tcm.server;

import com.kepler.tcm.server.core.util.StringList;
import com.kepler.tcm.server.core.util.StringTools;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Util {
	public static String fillZero(String strSource, int nNum) {
		String returnValue = "";
		if (strSource.length() < nNum) {
			int n = nNum - strSource.length();
			for (int i = 0; i < n; i++) {
				returnValue = returnValue + "0";
			}
			returnValue = returnValue + strSource;
		} else {
			for (int i = 0; i < nNum; i++) {
				returnValue = returnValue + strSource.charAt(i);
			}
		}
		return returnValue;
	}

	public static synchronized void writePropertyFile(Map propertyMap, String strFile) throws Exception {
		writePropertyFile1(propertyMap, strFile, getCharset());
	}

	public static synchronized void writePropertyFile1(Map propertyMap, String strFile, String charset)
			throws Exception {
		new File(strFile).getParentFile().mkdirs();

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strFile);
			String strTemp = "";

			Iterator iterator = propertyMap.keySet().iterator();
			while (iterator.hasNext()) {
				strTemp = (String) iterator.next();
				String s = strTemp + " = " + StringTools.encodeEnter((String) propertyMap.get(strTemp))
						+ System.getProperty("line.separator");
				if ((charset == null) || (charset.length() == 0))
					fos.write(s.getBytes());
				else
					fos.write(s.getBytes(charset));
			}

		} finally {
			if (fos != null)
				fos.close();
		}
	}

	public static synchronized void write(String strFileName, boolean bMode, String str) throws Exception {
		write1(strFileName, bMode, str, getCharset());
	}

	public static synchronized void write1(String strFileName, boolean bMode, String str, String charset)
			throws Exception {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strFileName);

			if ((charset == null) || (charset.length() == 0))
				fos.write(str.getBytes());
			else
				fos.write(str.getBytes(charset));

		} finally {
			if (fos != null)
				fos.close();
		}
	}

	public static synchronized void setField(String filename, String charset, String field, String value)
			throws Exception {
		new File(filename).getParentFile().mkdirs();
		StringList list = new StringList();
		if (new File(filename).isFile())
			list.loadFromFile(filename, charset);
		list.setValue(field, value);
		list.saveToFile(filename, "");
	}

	public static synchronized void setFields(String filename, String charset, Map propertyMap) throws Exception {
		new File(filename).getParentFile().mkdirs();
		StringList list = new StringList();
		if (new File(filename).isFile())
			list.loadFromFile(filename, charset);

		Iterator iterator = propertyMap.keySet().iterator();
		while (iterator.hasNext()) {
			String strTemp = (String) iterator.next();
			list.setValue(strTemp, (String) propertyMap.get(strTemp));
		}
		list.saveToFile(filename, "");
	}

	public static synchronized String getField(String filename, String fieldname, String charset) {
		try {
			StringList list = new StringList();
			if (new File(filename).isFile())
				list.loadFromFile(filename, charset);
			return list.getValue(fieldname);
		} catch (Exception e) {
		}
		return null;
	}

	public static String getCharset() {
		return Config.getCharset(Server.SERVER);
	}

	public static String eToString(Throwable t) {
		StringWriter w = new StringWriter();
		PrintWriter p = new PrintWriter(w);
		t.printStackTrace(p);
		return w.toString();
	}

	public static void main(String[] args) throws Exception {
	}
}
