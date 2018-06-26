package com.kepler.tcm.core.util;

import java.util.Random;

public class StringTools {
	private static long lastTimeMillis = 0L;
	private static long lastCount = 0L;
	private static long constN = 1000L;
	private static Random rd = new Random();

	public static boolean stringInArray(String s, String[] ss, boolean needCase) {
		if (needCase) {
			for (int i = 0; i < ss.length; i++) {
				if (s.equals(ss[i]))
					return true;
			}
		} else {
			for (int i = 0; i < ss.length; i++)
				if (s.equalsIgnoreCase(ss[i]))
					return true;
		}
		return false;
	}

	public static boolean charInArray(char c, char[] cc, boolean whiteSpace) {
		for (int i = 0; i < cc.length; i++)
			if ((c == cc[i]) || ((whiteSpace) && (c <= ' ')))
				return true;
		return false;
	}

	public static String trimLeft(String s, char[] cc, boolean whiteSpace) {
		if (s == null)
			return null;
		int i = 0;
		while (i < s.length()) {
			if (!charInArray(s.charAt(i), cc, whiteSpace))
				break;
			i++;
		}

		return s.substring(i);
	}

	public static String trimRight(String s, char[] cc, boolean whiteSpace) {
		if (s == null)
			return null;
		int i = s.length() - 1;
		while (i >= 0) {
			if (!charInArray(s.charAt(i), cc, whiteSpace))
				break;
			i--;
		}
		return s.substring(0, i + 1);
	}

	public static String trim(String s, char[] cc, boolean whiteSpace) {
		return trimRight(trimLeft(s, cc, whiteSpace), cc, whiteSpace);
	}

	public static String replace(String strSrc, String strOld, String strNew) {
		if (strSrc == null)
			return "";
		StringBuffer sb = new StringBuffer();
		int oldlen = strOld.length();
		int pos = 0;

		for (int lastpos = 0; (lastpos < strSrc.length())
				&& ((pos = strSrc.indexOf(strOld, lastpos)) > -1); lastpos = pos + oldlen) {
			sb.append(strSrc.substring(lastpos, pos));
			sb.append(strNew);
		}

		/*
		 * if (lastpos < strSrc.length()) sb.append(strSrc.substring(lastpos));
		 */
		return new String(sb);
	}

	public static String repeat(char c, int times) {
		char[] cc = new char[times];
		for (int i = 0; i < times; i++)
			cc[i] = c;
		return cc.toString();
	}

	public static String repeat(String s, int times) {
		int n = s.length() * times;
		StringBuffer sb = new StringBuffer(n);
		for (int i = 0; i < times; i++)
			sb.append(s);
		return sb.toString();
	}

	public static String quote(String s) {
		if (s == null)
			return null;
		int len = s.length();
		StringBuffer sb = new StringBuffer(len + 2);
		sb.append("\"");
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '"')
				sb.append('\\');
			sb.append(s.charAt(i));
		}
		sb.append("\"");
		return sb.toString();
	}

	public static String contact(String s1, char separator, String s2) {
		if ((s1.length() > 0) && (s1.charAt(s1.length() - 1) == separator))
			s1 = s1.substring(0, s1.length() - 1);
		if ((s2.length() > 0) && (s2.charAt(0) == separator))
			s2 = s2.substring(1);
		return s1 + separator + s2;
	}

	public static String replaceEnter(String s) {
		return s.replaceAll("\\u005C", "\\\\\\\\").replaceAll("\\u000D", "\\\\\\r").replaceAll("\\u000A", "\\\\\\n")
				.replaceAll("/", "\\\\/");
	}

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

	public static synchronized long newID() {
		long n = System.currentTimeMillis();
		if (n == lastTimeMillis) {
			if (lastCount >= constN - 1L) {
				while (System.currentTimeMillis() == lastTimeMillis)
					;
				return newID();
			}

			return n * constN * constN + ++lastCount * constN + rd.nextInt(1000);
		}

		lastCount = 0L;
		lastTimeMillis = n;
		return n * constN * constN + rd.nextInt(1000);
	}

	public static String encodeEnter(String s) {
		if ((s == null) || (s.length() == 0))
			return s;
		StringBuffer sb = new StringBuffer(s.length());
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\\')
				sb.append("\\\\");
			else if (c == '\r')
				sb.append("\\r");
			else if (c == '\n')
				sb.append("\\n");
			else
				sb.append(c);
		}
		return sb.toString();
	}

	public static String decodeEnter(String s) {
		if ((s == null) || (s.length() == 0))
			return s;
		StringBuffer sb = new StringBuffer(s.length());
		int i = 0;
		int len = s.length();
		while (i < len) {
			char c = s.charAt(i);
			if ((c == '\\') && (i < len - 1)) {
				char c2 = s.charAt(i + 1);
				if (c2 == '\\') {
					sb.append('\\');
					i++;
				} else if (c2 == 'r') {
					sb.append('\r');
					i++;
				} else if (c2 == 'n') {
					sb.append('\n');
					i++;
				} else {
					sb.append(c);
				}
			} else {
				sb.append(c);
			}
			i++;
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		while (true)
			System.out.println(newID());
	}
}