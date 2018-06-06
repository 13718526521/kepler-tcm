package com.kepler.tcm.server.core.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

public class LogA extends Log {
	private static LogA log = null;

	private String name = null;

	File[] files = null;

	public static void main(String[] args) throws Exception {
		File f = new File("D:\\workspace\\web_tcm_server\\servers\\server1\\working\\00000368");
		File[] files = f.listFiles(new FileFilter() {
			public boolean accept(File f) {
				String tmp = f.getName().toLowerCase();
				if (tmp.startsWith("task.log"))
					return true;
				return false;
			}
		});
		if (files == null)
			files = new File[0];
		Arrays.sort(files, new FileComparator());
		for (int i = 0; i < files.length; i++)
			System.out.println(files[i].getName() + "\t" + files[i].lastModified());
	}

	public LogA(String path, String logName, String charset) {
		File f = new File(path);
		this.name = logName;
		this.files = f.listFiles(new FileFilter() {
			public boolean accept(File f) {
				String tmp = f.getName().toLowerCase();
				if (tmp.startsWith(LogA.this.name))
					return true;
				return false;
			}
		});
		Arrays.sort(this.files, new FileComparator());
		doLog(path, this.files.length);
		try {
			this.charset = charset;
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public String getName(int daysAgo) {
		if (this.files.length == 0)
			return this.name;

		if (daysAgo < this.files.length)
			return this.files[(this.files.length - 1 - daysAgo)].getName();

		return this.files[(this.files.length - 1)].getName();
	}

	public long getTotalSize() {
		long n = 0L;
		for (int i = 0; i < this.files.length; i++)
			n += this.files[i].length();
		return n;
	}

	public File[] getFiles() {
		return this.files;
	}
}