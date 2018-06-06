package com.kepler.tcm.server.tcm.server;

public class Log4jTemplate {
	public static String XMLHEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
	public static String XMLDOCTYPE = "<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">";
	public static String XMLROOTHEADER = "<log4j:configuration  xmlns:log4j=\"http://jakarta.apache.org/log4j/\" threshold=\"all\" debug=\"false\">";
	public static String XMLROOTENDER = "</log4j:configuration>";

	public static String getLog4jTemplate() {
		StringBuffer sb = new StringBuffer();
		sb.append(XMLHEADER);
		sb.append(XMLDOCTYPE);
		sb.append(XMLROOTHEADER);
		sb.append(XMLROOTENDER);
		return sb.toString();
	}
}