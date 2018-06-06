package com.kepler.tcm.server.core.util;

public class Convert {

	public static String toString(String s) {
		return toString(s, "");
	}

	public static String toString(Object obj) {
		return toString(obj, "");
	}

	public static String toString(Object obj, String def) {
		if (obj == null)
			return def;
		return obj.toString();
	}

	public static int toInt(String s, int nDefault) {
		if ((s == null) || (s.length() == 0))
			return nDefault;
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
		}
		return nDefault;
	}

	public static int toInt(Object o, int nDefault) {
		if (o == null)
			return nDefault;
		try {
			if ((o instanceof String))
				return toInt((String) o, nDefault);

			return ((Number) o).intValue();
		} catch (Exception e) {
		}

		return nDefault;
	}

	public static Integer toInteger(String s, int nDefault) {
		int n = toInt(s, nDefault);
		return new Integer(n);
	}

	public static String toString(String[] array, String separator, String padding) {
		if ((array == null) || (array.length == 0))
			return "";
		if (padding == null)
			padding = "";
		String s = "";
		for (int nIndex = 0; nIndex < array.length; nIndex++) {
			if (nIndex > 0)
				s = s + separator;
			s = s + padding + array[nIndex] + padding;
		}
		return s;
	}
}
