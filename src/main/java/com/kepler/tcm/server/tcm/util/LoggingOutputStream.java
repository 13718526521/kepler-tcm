package com.kepler.tcm.server.tcm.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;

public class LoggingOutputStream extends OutputStream {
	protected static final String LINE_SEPERATOR = System.getProperty("line.separator");
	static PrintStream defaultOutStream = System.out;
	static PrintStream defaultErrStream = System.err;
	PrintStream defaultStream = null;
	String type = null;
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	protected boolean hasBeenClosed = false;
	protected byte[] buf;
	protected int count;
	private int bufLength;
	public static final int DEFAULT_BUFFER_LENGTH = 2048;
	protected Category category;
	protected Priority priority;
	protected boolean defaultStreamEnable;

	private LoggingOutputStream() {
	}

	public LoggingOutputStream(Category cat, Priority priority, boolean defaultStreamEnable)
			throws IllegalArgumentException {
		if (cat == null) {
			throw new IllegalArgumentException("cat == null");
		}
		if (priority == null) {
			throw new IllegalArgumentException("priority == null");
		}
		this.priority = priority;
		this.category = cat;
		this.bufLength = 2048;
		this.buf = new byte[2048];
		this.count = 0;

		this.defaultStream = (priority.equals(Level.ERROR) ? defaultOutStream : defaultErrStream);
		this.type = (priority.equals(Level.ERROR) ? "[ERR] " : "[OUT] ");

		this.defaultStreamEnable = defaultStreamEnable;
	}

	public void close() {
		flush();
		this.hasBeenClosed = true;
	}

	public void write(int b) throws IOException {
		if (this.hasBeenClosed) {
			throw new IOException("The stream has been closed.");
		}

		if (b == 0) {
			return;
		}

		if (this.count == this.bufLength) {
			int newBufLength = this.bufLength + 2048;
			byte[] newBuf = new byte[newBufLength];
			System.arraycopy(this.buf, 0, newBuf, 0, this.bufLength);
			this.buf = newBuf;
			this.bufLength = newBufLength;
		}
		this.buf[this.count] = ((byte) b);
		this.count += 1;
	}

	public void flush() {
		if (this.count == 0) {
			return;
		}

		if (this.count == LINE_SEPERATOR.length()) {
			if (((char) this.buf[0] == LINE_SEPERATOR.charAt(0))
					&& ((this.count == 1) || ((this.count == 2) && ((char) this.buf[1] == LINE_SEPERATOR.charAt(1))))) {
				reset();
				return;
			}
		}
		byte[] theBytes = new byte[this.count];
		System.arraycopy(this.buf, 0, theBytes, 0, this.count);

		String bytes = new String(theBytes).trim();
		int line = -1;
		while ((line = bytes.indexOf(LINE_SEPERATOR)) != -1) {
			bytes = bytes.substring(0, line) + bytes.substring(line + LINE_SEPERATOR.length());
		}

		if (this.defaultStreamEnable)
			this.defaultStream.println(this.type + bytes);
		this.category.log(this.priority, dateFormat.format(new Date()) + " " + bytes);
		reset();
	}

	private void reset() {
		this.count = 0;
	}
}