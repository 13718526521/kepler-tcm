package com.kepler.tcm.core.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.kepler.tcm.core.util.FileTools;

public class Counter {
	private static Map map = new HashMap();

	private String name = null;

	private String filename = null;

	private long initValue = 0L;

	private static Counter getCounter(String name, long initValue) throws IOException {
		if (name == null)
			throw new NullPointerException("Counter name is null");

		Counter counter = (Counter) map.get(name);
		if (counter == null) {
			counter = newCounter(name, initValue);
		}
		return counter;
	}

	public static Counter getCounter(String name) throws IOException {
		return getCounter(name, 0L);
	}

	protected Counter(String name, long initValue) throws IOException {
		this.name = name;
		this.initValue = initValue;
		this.filename = "./conf/counters/";

		new File(this.filename).mkdir();
		this.filename = (this.filename + name + ".counter");

		File f = new File(this.filename);
		if (!f.isFile())
			throw new FileNotFoundException("找不到计数器" + name + "，请先创建" + f.getCanonicalPath());
	}

	protected static synchronized Counter newCounter(String name, long initValue) throws IOException {
		Counter counter = (Counter) map.get(name);

		if (counter == null)
			counter = new Counter(name, initValue);

		return counter;
	}

	public synchronized void setValue(long n) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(this.filename));
		try {
			pw.write((int) n);
		} finally {
			pw.close();
		}
	}

	public synchronized long getValue() throws IOException {
		File f = new File(this.filename);

		if (!f.isFile())
			throw new FileNotFoundException("找不到计数器" + this.name + "，请先创建");

		BufferedReader in = new BufferedReader(new FileReader(this.filename));
		try {
			String s = in.readLine();
			long l;
			if ((s != null) && (s.length() > 0))
				return Long.parseLong(s.trim());

			return this.initValue;
		} finally {
			in.close();
		}
	}

	public synchronized long step(long n) throws IOException {
		long value = getValue() + n;
		setValue(value);
		return value;
	}

	public static void main(String[] args) throws IOException {
		Counter counter = getCounter("counter1", 100L);
		System.out.println(counter.step(5L));
	}

	public String getName() {
		return this.name;
	}

	public static String[] getCounterNames() {
		File f = new File("./conf/counters");
		String[] list = f.list(new FilenameFilterImpl());
		for (int i = 0; i < list.length; i++)
			list[i] = FileTools.deleteFileExt(list[i]);
		return list;
	}

	public static String[][] getCounters() {
		File f = new File("./conf/counters");
		File[] fs = f.listFiles(new FilenameFilterImpl());
		if ((fs == null) || (fs.length == 0))
			return null;
		String[][] counters = new String[fs.length][2];
		for (int i = 0; i < fs.length; i++) {
			counters[i][0] = FileTools.deleteFileExt(fs[i].getName());
			try {
				counters[i][1] = getCounter(counters[i][0]).getValue() + "";
			} catch (Exception e) {
				counters[i][1] = "0";
			}
		}
		return counters;
	}

	public static synchronized void z_addCounter(String name, long initValue) throws IOException {
		String dir = "./conf/counters/";
		new File(dir).mkdir();
		String f = dir + name + ".counter";
		if (new File(f).exists())
			throw new IOException("计数器已经存在");

		PrintWriter pw = new PrintWriter(new FileWriter(f));
		try {
			pw.write((int) initValue);
		} finally {
			pw.close();
		}
	}

	public static synchronized void z_removeCounter(String name) {
		new File("./conf/counters/" + name + ".counter").delete();
	}
}