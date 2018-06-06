package com.kepler.tcm.server.core.util;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Common {
	public static void scriptAlert(Writer w, String s) throws IOException {
		w.write("<script>alert('" + s + "');</script>\r\n");
	}

	public static String format(String strFormat, Object[] args) {
		MessageFormat fmt = new MessageFormat(strFormat);

		return fmt.format(args);
	}

	public static String format(String strFormat, Object arg1) {
		MessageFormat fmt = new MessageFormat(strFormat);
		Object[] args = new Object[1];
		args[0] = arg1;
		return fmt.format(args);
	}

	public static String format(String strFormat, Object arg1, Object arg2) {
		MessageFormat fmt = new MessageFormat(strFormat);
		Object[] args = new Object[2];
		args[0] = arg1;
		args[1] = arg2;

		return fmt.format(args);
	}

	public static String format(String strFormat, Object arg1, Object arg2, Object arg3) {
		MessageFormat fmt = new MessageFormat(strFormat);
		Object[] args = new Object[3];
		args[0] = arg1;
		args[1] = arg2;
		args[2] = arg3;
		return fmt.format(args);
	}

	public static String format(String strFormat, Object arg1, Object arg2, Object arg3, Object arg4) {
		MessageFormat fmt = new MessageFormat(strFormat);
		Object[] args = new Object[4];
		args[0] = arg1;
		args[1] = arg2;
		args[2] = arg3;
		args[3] = arg4;
		return fmt.format(args);
	}

	public static String encodeUTF8(String s) throws Exception {
		if (s == null)
			s = "";
		return URLEncoder.encode(s, "UTF-8");
	}

	public static Locale getLocale(HttpSession session) {
		String strLanguage = Convert.toString(session.getAttribute("Language"));
		if ((strLanguage.length() == 0) || (strLanguage.equals("_")))
			return null;
		int n = strLanguage.indexOf('_');
		if (n == -1)
			return new Locale(strLanguage);
		String s1 = strLanguage.substring(0, n);
		String s2 = strLanguage.substring(n + 1);
		return new Locale(s1, s2);
	}

	public static ResourceBundle getBundle(HttpSession session) {
		return ResourceBundle.getBundle("com.trs.Resource.resource", getLocale(session));
	}

	public static void reloadBundles() throws Exception {
		clearMap(ResourceBundle.class, null, "cacheList");

		clearTomcatCache();
	}

	private static void clearTomcatCache() throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		Class cl = loader.getClass();

		if ("org.apache.catalina.loader.WebappClassLoader".equals(cl.getName())) {
			clearMap(cl, loader, "resourceEntries");
		} else {
			throw new Exception("class loader " + cl.getName() + " is not tomcat loader.");
		}
	}

	private static void clearMap(Class cl, Object obj, String name)
			throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Field field = cl.getDeclaredField(name);
		field.setAccessible(true);

		Object cache = field.get(obj);
		Class ccl = cache.getClass();
		Method sizeMethod = ccl.getMethod("size", null);

		Method clearMethod = ccl.getMethod("clear", null);
		clearMethod.invoke(cache, null);
	}

	public static String getCookieValue(HttpServletRequest request, String strName) {
		if ((request == null) || (strName == null))
			return null;
		Cookie[] cookieUserInfo = request.getCookies();
		if ((cookieUserInfo == null) || (cookieUserInfo.length == 0))
			return null;
		for (int i = 0; i < cookieUserInfo.length; i++) {
			if (cookieUserInfo[i].getName().equals(strName))
				return cookieUserInfo[i].getValue();
		}
		return null;
	}

	public static String getCookieValue(HttpServletRequest request, String strName, String def) {
		String v = getCookieValue(request, strName);
		if (v == null)
			return def;
		return v;
	}
}
