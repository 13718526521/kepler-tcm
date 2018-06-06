package com.kepler.tcm.server.tcm.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Calendar;

public class Log {
	private static String className = new Log().getClass().getName();
	private static PrintWriter logPrint;
	private static String logFile = "";
	private static String logName = "";
	private static String path = ".";

	private static boolean systemPrint = true;

	public static String getFileName() {
		return logFile;
	}

	public static String getPath() {
		return path;
	}

	public static synchronized void setSystemPrint(boolean b) {
		systemPrint = b;
	}

	public static boolean getsystemPrint() {
		return systemPrint;
	}

	public static synchronized void config(String path, String prefixName) {
		if (path != null) {
			if (new File(path).exists()) {
				path = path;
			} else if (new File(path).mkdirs())
				path = path;
			else {
				System.out.println("Log path " + path + " is not exists");
			}
		}
		if (prefixName == null) {
			prefixName = "";
		}
		logName = prefixName;
		checkDate();
	}

	public static synchronized void conifg(String prefixName) {
		config(null, prefixName);
	}

	private static String getLogFile() {
		String date = "";
		Calendar cd = Calendar.getInstance();
		int y = cd.get(1);
		int m = cd.get(2) + 1;
		int d = cd.get(5);

		date = path + File.separator + logName + y + "-";

		if (m < 10) {
			date = date + 0;
		}

		date = date + m + "-";

		if (d < 10) {
			date = date + 0;
		}

		date = date + d + ".log";

		return date;
	}

	private static void newLog() {
		logFile = getLogFile();
		try {
			logPrint = new PrintWriter(new FileWriter(logFile, true), true);
		} catch (IOException e) {
			new File(path).mkdir();
			try {
				logPrint = new PrintWriter(new FileWriter(logFile, true), true);
			} catch (IOException ex) {
				System.err.println("无法打开日志文件：" + logFile);
				logPrint = new PrintWriter(System.err);
			}
		}
	}

	private static void checkDate() {
		if ((logFile == null) || (logFile.trim().equals("")) || (!logFile.equals(getLogFile()))) {
			newLog();
		}
	}

	public static synchronized void log(String msg) {
		log(msg, systemPrint);
	}

	public static synchronized void log(String msg, boolean bSystemPrint) {
		checkDate();

		String s = getDate() + " " + msg;
		logPrint.println(s);
		if (bSystemPrint) {
			System.out.println(s);
		}
	}

	public static String getDate() {
		Calendar calendar = Calendar.getInstance();
		String strYear = String.valueOf(calendar.get(1));
		String strMonth = String.valueOf(calendar.get(2) + 1);
		if (strMonth.length() == 1) {
			strMonth = "0" + strMonth;
		}
		String strDate = String.valueOf(calendar.get(5));
		if (strDate.length() == 1) {
			strDate = "0" + strDate;
		}
		String strHours = String.valueOf(calendar.get(11));
		if (strHours.length() == 1) {
			strHours = "0" + strHours;
		}
		String strMinutes = String.valueOf(calendar.get(12));
		if (strMinutes.length() == 1) {
			strMinutes = "0" + strMinutes;
		}
		String strSeconds = String.valueOf(calendar.get(13));
		if (strSeconds.length() == 1) {
			strSeconds = "0" + strSeconds;
		}
		return strYear + "-" + strMonth + "-" + strDate + " " + strHours + ":" + strMinutes + ":" + strSeconds;
	}

	public static synchronized void log(Throwable e) {
		log(e.toString());
	}

	public static synchronized void throwLog(Exception e) throws Exception {
		log(e.toString());
		throw e;
	}

	public static synchronized void log(Throwable e, String msg) {
		log(e.toString() + " " + msg);
	}

	private static String getMethodName() {
		return "";
	}
}