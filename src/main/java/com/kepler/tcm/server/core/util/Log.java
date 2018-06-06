package com.kepler.tcm.server.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Log {
	private OutputStream logPrint = null;

	private String logFile = "";

	private String logName = "";

	private String logPath = "";

	private static boolean systemPrint = true;

	private int logDays = 1;

	public static int LEVEL_INFO = 0;

	public static int LEVEL_OK = 1;

	public static int LEVEL_FAIL = 2;

	private static String[] colors = { "black", "red", "green" };

	private static Log log = null;

	public String charset = "UTF-8";

	public synchronized Log getLog(String path, int days) {
		if (log == null)
			log = new Log(path, days);
		return log;
	}

	protected Log() {
	}

	protected Log(String path, int days) {
		doLog(path, days);
	}

	protected void doLog(String path, int days) {
		if (path != null) {
			File f = new File(path);
			if (!f.exists()) {
				f.mkdirs();
			}

			if (f.exists()) {
				this.logPath = path;
			} else {
				System.out.println("Log path " + path + " is not exists");
			}

		}

		this.logDays = days;
	}

	public String getName() {
		return this.logName;
	}

	public String getFileName() {
		return this.logFile;
	}

	public String getPath() {
		return this.logPath;
	}

	public int getLogDays() {
		return this.logDays;
	}

	public synchronized void setSystemPrint(boolean b) {
		systemPrint = b;
	}

	public boolean getsystemPrint() {
		return systemPrint;
	}

	public String getName(int daysAgo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		GregorianCalendar gc = new GregorianCalendar();

		gc.setTime(d);

		if (daysAgo != 0)
			gc.add(5, -daysAgo);

		return sdf.format(gc.getTime()) + ".log";
	}

	private void newLog() {
		this.logName = getName(0);
		this.logFile = (this.logPath + File.separator + this.logName);

		if (this.logPrint != null) {
			try {
				this.logPrint.close();
			} catch (Exception localException) {
			}

		}

		try {
			this.logPrint = new FileOutputStream(this.logFile, true);
		} catch (IOException e) {
			new File(this.logPath).mkdir();
			try {
				this.logPrint = new FileOutputStream(this.logFile, true);
			} catch (IOException ex) {
				System.err.println("无法创建日志文件：" + this.logFile);
				this.logPrint = System.err;
			}
		}
	}

	private void checkDate() {
		if ((this.logFile == null) || (this.logFile.trim().equals("")) || (!this.logName.equals(getName(0)))) {
			newLog();
			deleteOldLog();
		}
	}

	public void deleteOldLog() {
		File f = new File(this.logPath);
		File[] files = f.listFiles();
		String compareName = getName(this.logDays);
		for (int i = 0; i < files.length; i++) {
			String name = files[i].getName();
			if ((name.endsWith(".log")) && (name.compareTo(compareName) <= 0)) {
				files[i].delete();
				try {
					new File(files[i].getCanonicalPath() + ".len").delete();
				} catch (Exception localException) {
				}
			}
		}
	}

	public synchronized void log(String msg) {
		log(msg, LEVEL_INFO, systemPrint);
	}

	public synchronized void log(String msg, int level, boolean bSystemPrint) {
		checkDate();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = sdf.format(new Date());

		String s = "<font color='" + colors[level] + "'>" + d + " " + msg + "</font><br>\r\n";
		try {
			this.logPrint.write(s.getBytes(this.charset));
			this.logPrint.flush();
		} catch (Exception localException) {
		}

		if (bSystemPrint) {
			System.out.println(s);
		}
	}

	public synchronized void log(Throwable e) {
		log(e.toString());
	}

	public synchronized void throwLog(Exception e) throws Exception {
		log(e.toString());
		throw e;
	}

	public synchronized void log(Throwable e, String msg) {
		log(e.toString() + " " + msg);
	}

	private static String getMethodName() {
		return "";
	}

	public void close() throws IOException {
		if (this.logPrint != null)
			this.logPrint.close();
	}

	private long getFileCharLength(String filename) {
		try {
			String s = FileTools.getFileText(filename);
			if (s.length() > 0) {
				return Long.parseLong(s);
			}
		} catch (Exception localException) {
		}
		return 0L;
	}

	private void dowrite(String file, Writer w, long begin, long end) throws IOException {
		File f = new File(file);
		if (!f.exists())
			return;
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			if (begin > 0L)
				is.skip(begin);
			if (end < begin) {
				InputStreamReader r = new InputStreamReader(is, this.charset);
				int n = -1;
				do {
					n = r.read();
					if (n == -1)
						break;
				} while (n == 65533);

				while (n != -1) {
					w.write(n);
					n = r.read();
				}

				w.flush();
			} else {
				if (f.length() < end)
					end = f.length();
				byte[] bs = new byte[(int) (end - begin)];
				is.read(bs);

				w.write(new String(bs, this.charset));
				w.flush();
			}
		} finally {
			if (is != null)
				is.close();
		}
	}

	public void displayReverse(Writer w, int daysAgo, int pageNo, int pageSize) throws IOException {
		long begin = (pageNo - 1) * pageSize;
		long end = pageNo * pageSize;

		long absfileBegin = 0L;

		long fileBegin = 0L;

		int index1 = -1;
		int index2 = -1;
		long posi1 = -1L;
		long posi2 = -1L;
		int day1 = 0;
		int day2 = 0;
		if (daysAgo >= 0) {
			day1 = daysAgo;
			day2 = daysAgo;
		} else {
			day1 = this.logDays - 1;
			day2 = 0;
		}

		for (int i = day2; i <= day1; i++) {
			String name = getName(i);
			File f = new File(this.logPath + File.separator + name);
			if (f.exists()) {
				long fileLength = f.length();

				if (fileLength != 0L) {
					if ((begin >= absfileBegin) && (begin <= absfileBegin + fileLength)) {
						index1 = i;
						posi1 = absfileBegin + fileLength - begin;
					}
					if ((end >= absfileBegin) && (end < absfileBegin + fileLength)) {
						index2 = i;
						posi2 = absfileBegin + fileLength - end;
					}

					absfileBegin += fileLength;
				}

			}

		}

		if (index1 != -1) {
			if (index2 == -1) {
				index2 = day1;
				posi2 = 0L;
			}

			if (index1 == index2) {
				String file = this.logPath + File.separator + getName(index1);

				if (posi2 >= 2L)
					posi2 -= 2L;

				posi1 += 2L;

				dowrite(file, w, posi2, posi1);
			} else {
				for (int i = index2; i >= index1; i--) {
					String file = this.logPath + File.separator + getName(i);
					long b = 0L;
					long e = -1L;

					if (i == index2) {
						b = posi2;
						if (b >= 2L)
							b -= 2L;
					}
					if (i == index1) {
						e = posi1 + 2L;
					}

					dowrite(file, w, b, e);
				}
			}
		}
	}

	public void display(Writer w, int daysAgo, int pageNo, int pageSize) throws IOException {
		long begin = (pageNo - 1) * pageSize;
		long end = pageNo * pageSize;

		long absfileBegin = 0L;

		long fileBegin = 0L;

		int index1 = -1;
		int index2 = -1;
		long posi1 = -1L;
		long posi2 = -1L;
		int day1 = 0;
		int day2 = 0;
		if (daysAgo >= 0) {
			day1 = daysAgo;
			day2 = daysAgo;
		} else {
			day1 = this.logDays - 1;
			day2 = 0;
		}

		for (int i = day1; i >= day2; i--) {
			String name = getName(i);
			File f = new File(this.logPath + File.separator + name);
			if (f.exists()) {
				long fileLength = f.length();

				if (fileLength != 0L) {
					if ((begin >= absfileBegin) && (begin <= absfileBegin + fileLength)) {
						index1 = i;
						posi1 = begin - absfileBegin;
					}
					if ((end >= absfileBegin) && (end < absfileBegin + fileLength)) {
						index2 = i;
						posi2 = end - absfileBegin;
					}

					absfileBegin += fileLength;
				}

			}

		}

		if (index1 != -1) {
			if (index2 == -1)
				index2 = day2;

			if (index1 == index2) {
				String file = this.logPath + File.separator + getName(index1);

				if (posi1 >= 2L)
					posi1 -= 2L;
				if (posi2 != -1L)
					posi2 += 2L;
				dowrite(file, w, posi1, posi2);
			} else {
				for (int i = index1; i >= index2; i--) {
					String file = this.logPath + File.separator + getName(i);
					long b = 0L;
					long e = -1L;
					if ((i == index1) && (posi1 >= 2L))
						b = posi1 - 2L;
					if ((i == index2) && (posi2 != -1L))
						e = posi2 + 2L;
					dowrite(file, w, b, e);
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println("begin");

		LogA log = new LogA("D:\\workspace\\web_tcm_server\\servers\\server1\\working\\00000368", "task.log", "UTF-8");
		PrintWriter w = new PrintWriter(System.out);

		int pageCount = 722;
		System.out.println(pageCount);
		log.displayReverse(w, -1, pageCount - 1, 5120);
		w.write("end");
		w.println("end");
		w.flush();
	}
}
