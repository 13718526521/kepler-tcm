package com.kepler.tcm.server.tcm.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;

public class DataConvert {
	final int COUNT = 512;
	private String charset1 = null;
	private String charset2 = null;

	public void setCharset(String charset1, String charset2) {
		this.charset1 = charset1;
		this.charset2 = charset2;
	}

	public Object convert(Object o1, Object o2) throws IOException {
		if (o1 == null)
			return null;

		if ((o1 instanceof InputStream)) {
			InputStream src = (InputStream) o1;
			if ((o2 instanceof String)) {
				return convert(src);
			}

			if ((o2 instanceof InputStream)) {
				return convert(src, (InputStream) o2);
			}
			if ((o2 instanceof byte[])) {
				return convert(src, (byte[]) o2);
			}
			if ((o2 instanceof Reader)) {
				return convert(src, (Reader) o2);
			}
		} else if ((o1 instanceof byte[])) {
			byte[] src = (byte[]) o1;
			if ((o2 instanceof String)) {
				return convert(src);
			}

			if ((o2 instanceof InputStream)) {
				return convert(src, (InputStream) o2);
			}
			if ((o2 instanceof byte[])) {
				return convert(src, (byte[]) o2);
			}
			if ((o2 instanceof Reader)) {
				return convert(src, (Reader) o2);
			}
		} else if ((o1 instanceof Reader)) {
			Reader src = (Reader) o1;
			if ((o2 instanceof String)) {
				return convert(src);
			}

			if ((o2 instanceof InputStream)) {
				return convert(src, (InputStream) o2);
			}
			if ((o2 instanceof byte[])) {
				return convert(src, (byte[]) o2);
			}
			if ((o2 instanceof Reader)) {
				return convert(src, (Reader) o2);
			}

		} else {
			String src = o1.toString();
			if ((o2 instanceof String)) {
				return convert(src);
			}

			if ((o2 instanceof InputStream)) {
				return convert(src, (InputStream) o2);
			}
			if ((o2 instanceof byte[])) {
				return convert(src, (byte[]) o2);
			}
			if ((o2 instanceof Reader)) {
				return convert(src, (Reader) o2);
			}
		}

		return o1;
	}

	public String convert(String s) {
		return s;
	}

	public OutputStream convert(String s, OutputStream os) throws IOException {
		os.write(convert(s, new byte[0]));
		return os;
	}

	public byte[] convert(String s, byte[] bs) throws IOException {
		if (this.charset2 == null)
			return s.getBytes();
		return s.getBytes(this.charset2);
	}

	public Writer convert(String s, Writer w) throws IOException {
		w.write(s);
		return w;
	}

	public String convert(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			convert(is, bos);
			String str;
			if (this.charset1 == null)
				return bos.toString();
			return bos.toString(this.charset1);
		} finally {
			bos.close();
		}
	}

	public OutputStream convert(InputStream is, OutputStream os) throws IOException {
		byte[] bs = new byte[512];
		int length = -1;

		while ((length = is.read(bs)) != -1) {
			os.write(bs, 0, length);
		}

		return os;
	}

	public Writer convert(InputStream is, Writer w) throws IOException {
		InputStreamReader r = null;
		if (this.charset1 == null)
			r = new InputStreamReader(is);
		else
			r = new InputStreamReader(is, this.charset1);
		convert(r, w);
		return w;
	}

	public byte[] convert(InputStream is, byte[] bs) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			convert(is, bos);
			return bos.toByteArray();
		} finally {
			bos.close();
		}
	}

	public String convert(byte[] bs) throws IOException {
		if (this.charset1 == null)
			return new String(bs);
		return new String(bs, this.charset1);
	}

	public OutputStream convert(byte[] bs, OutputStream os) throws IOException {
		os.write(bs);
		return os;
	}

	public byte[] convert(byte[] bs, byte[] bs2) {
		bs2 = bs;
		return bs2;
	}

	public Writer convert(byte[] bs, Writer w) throws IOException {
		if (this.charset1 == null)
			w.write(new String(bs));
		else
			w.write(new String(bs, this.charset1));
		return w;
	}

	public String convert(Reader r) throws IOException {
		StringWriter sw = new StringWriter();
		try {
			char[] buf = new char[512];
			int length = -1;
			while ((length = r.read(buf)) != -1) {
				sw.write(buf, 0, length);
			}
			return sw.toString();
		} finally {
			sw.close();
		}
	}

	public OutputStream convert(Reader r, OutputStream os) throws IOException {
		OutputStreamWriter w = null;
		if (this.charset2 == null)
			w = new OutputStreamWriter(os);
		else
			w = new OutputStreamWriter(os, this.charset2);
		convert(r, w);
		return os;
	}

	public byte[] convert(Reader r, byte[] bs) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			convert(r, bos);
			return bos.toByteArray();
		} finally {
			bos.close();
		}
	}

	public Writer convert(Reader r, Writer w) throws IOException {
		char[] buf = new char[512];
		int length = -1;
		while ((length = r.read(buf)) != -1) {
			w.write(buf, 0, length);
		}
		return w;
	}

	public static void main(String[] args) {
		byte[] a = new byte[10];
		if ((a instanceof byte[]))
			System.out.println("yes");
	}

	public void close(Object o) {
		if (o != null) {
			try {
				Method m = o.getClass().getMethod("close", null);
				m.invoke(o, null);
			} catch (Exception localException) {
			}
		}
	}
}