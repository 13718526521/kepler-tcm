package com.kepler.tcm.server.tcm.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

public class Properties extends Hashtable {
	private static final long serialVersionUID = 4112578634029874840L;
	protected Properties defaults;
	private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	public Properties() {
		this(null);
	}

	public Properties(Properties defaults) {
		this.defaults = defaults;
	}

	public synchronized Object setProperty(String key, String value) {
		return put(key, value);
	}

	public synchronized void load(Reader reader) throws IOException {
		load0(new LineReader(reader));
	}

	public synchronized void load(InputStream inStream) throws IOException {
		load0(new LineReader(inStream));
	}

	private void load0(LineReader lr) throws IOException {
		char[] convtBuf = new char[1024];
		int limit;
		while ((limit = lr.readLine()) >= 0) {
			/* int limit; */
			char c = '\000';
			int keyLen = 0;
			int valueStart = limit;
			boolean hasSep = false;

			boolean precedingBackslash = false;
			while (keyLen < limit) {
				c = lr.lineBuf[keyLen];

				if (((c == '=') || (c == ':')) && (!precedingBackslash)) {
					valueStart = keyLen + 1;
					hasSep = true;
					break;
				}
				if (((c == ' ') || (c == '\t') || (c == '\f')) && (!precedingBackslash)) {
					valueStart = keyLen + 1;
					break;
				}
				if (c == '\\')
					precedingBackslash = !precedingBackslash;
				else {
					precedingBackslash = false;
				}
				keyLen++;
			}
			while (valueStart < limit) {
				c = lr.lineBuf[valueStart];
				if ((c != ' ') && (c != '\t') && (c != '\f')) {
					if ((hasSep) || ((c != '=') && (c != ':')))
						break;
					hasSep = true;
				}

				valueStart++;
			}
			String key = loadConvert(lr.lineBuf, 0, keyLen, convtBuf);
			String value = loadConvert(lr.lineBuf, valueStart, limit - valueStart, convtBuf);
			put(key, value);
		}
	}

	private String loadConvert(char[] in, int off, int len, char[] convtBuf) {
		if (convtBuf.length < len) {
			int newLen = len * 2;
			if (newLen < 0) {
				newLen = 2147483647;
			}
			convtBuf = new char[newLen];
		}

		char[] out = convtBuf;
		int outLen = 0;
		int end = off + len;

		while (off < end) {
			char aChar = in[(off++)];
			if (aChar == '\\') {
				aChar = in[(off++)];
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = in[(off++)];
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - 48;
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 97;
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 65;
							break;
						case ':':
						case ';':
						case '<':
						case '=':
						case '>':
						case '?':
						case '@':
						case 'G':
						case 'H':
						case 'I':
						case 'J':
						case 'K':
						case 'L':
						case 'M':
						case 'N':
						case 'O':
						case 'P':
						case 'Q':
						case 'R':
						case 'S':
						case 'T':
						case 'U':
						case 'V':
						case 'W':
						case 'X':
						case 'Y':
						case 'Z':
						case '[':
						case '\\':
						case ']':
						case '^':
						case '_':
						case '`':
						default:
							throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
						}
					}
					out[(outLen++)] = ((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					out[(outLen++)] = aChar;
				}
			} else {
				out[(outLen++)] = aChar;
			}
		}
		return new String(out, 0, outLen);
	}

	private String saveConvert(String theString, boolean escapeSpace, boolean escapeUnicode) {
		int len = theString.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = 2147483647;
		}
		StringBuffer outBuffer = new StringBuffer(bufLen);

		for (int x = 0; x < len; x++) {
			char aChar = theString.charAt(x);

			if ((aChar > '=') && (aChar < '')) {
				if (aChar == '\\') {
					outBuffer.append('\\');
					outBuffer.append('\\');
				} else {
					outBuffer.append(aChar);
				}
			} else
				switch (aChar) {
				case ' ':
					if ((x == 0) || (escapeSpace))
						outBuffer.append('\\');
					outBuffer.append(' ');
					break;
				case '\t':
					outBuffer.append('\\');
					outBuffer.append('t');
					break;
				case '\n':
					outBuffer.append('\\');
					outBuffer.append('n');
					break;
				case '\r':
					outBuffer.append('\\');
					outBuffer.append('r');
					break;
				case '\f':
					outBuffer.append('\\');
					outBuffer.append('f');
					break;
				case '!':
				case '#':
				case ':':
				case '=':
					outBuffer.append('\\');
					outBuffer.append(aChar);
					break;
				default:
					if ((((aChar < ' ') || (aChar > '~')) & escapeUnicode)) {
						outBuffer.append('\\');
						outBuffer.append('u');
						outBuffer.append(toHex(aChar >> '\f' & 0xF));
						outBuffer.append(toHex(aChar >> '\b' & 0xF));
						outBuffer.append(toHex(aChar >> '\004' & 0xF));
						outBuffer.append(toHex(aChar & 0xF));
					} else {
						outBuffer.append(aChar);
					}
					break;
				}
		}
		return outBuffer.toString();
	}

	private static void writeComments(BufferedWriter bw, String comments) throws IOException {
		bw.write("#");
		int len = comments.length();
		int current = 0;
		int last = 0;
		char[] uu = new char[6];
		uu[0] = '\\';
		uu[1] = 'u';
		while (current < len) {
			char c = comments.charAt(current);
			if ((c > 'ÿ') || (c == '\n') || (c == '\r')) {
				if (last != current)
					bw.write(comments.substring(last, current));
				if (c > 'ÿ') {
					uu[2] = toHex(c >> '\f' & 0xF);
					uu[3] = toHex(c >> '\b' & 0xF);
					uu[4] = toHex(c >> '\004' & 0xF);
					uu[5] = toHex(c & 0xF);
					bw.write(new String(uu));
				} else {
					bw.newLine();
					if ((c == '\r') && (current != len - 1) && (comments.charAt(current + 1) == '\n')) {
						current++;
					}
					if ((current == len - 1)
							|| ((comments.charAt(current + 1) != '#') && (comments.charAt(current + 1) != '!')))
						bw.write("#");
				}
				last = current + 1;
			}
			current++;
		}
		if (last != current)
			bw.write(comments.substring(last, current));
		bw.newLine();
	}

	/** @deprecated */
	public synchronized void save(OutputStream out, String comments) {
		try {
			store(out, comments);
		} catch (IOException localIOException) {
		}
	}

	public void store(Writer writer, String comments) throws IOException {
		store0((writer instanceof BufferedWriter) ? (BufferedWriter) writer : new BufferedWriter(writer), comments,
				false);
	}

	public void store(OutputStream out, String comments) throws IOException {
		store0(new BufferedWriter(new OutputStreamWriter(out, "8859_1")), comments, true);
	}

	private void store0(BufferedWriter bw, String comments, boolean escUnicode) throws IOException {
		if (comments != null) {
			writeComments(bw, comments);
		}
		bw.write("#" + new Date().toString());
		bw.newLine();
		synchronized (this) {
			for (Enumeration e = keys(); e.hasMoreElements();) {
				String key = (String) e.nextElement();
				String val = (String) get(key);
				key = saveConvert(key, true, escUnicode);

				val = saveConvert(val, false, escUnicode);
				bw.write(key + "=" + val);
				bw.newLine();
			}
		}
		bw.flush();
	}

	public String getProperty(String key) {
		Object oval = super.get(key);
		String sval = (oval instanceof String) ? (String) oval : null;
		return (sval == null) && (this.defaults != null) ? this.defaults.getProperty(key) : sval;
	}

	public String getProperty(String key, String defaultValue) {
		String val = getProperty(key);
		return val == null ? defaultValue : val;
	}

	public Enumeration propertyNames() {
		Hashtable h = new Hashtable();
		enumerate(h);
		return h.keys();
	}

	public Set stringPropertyNames() {
		Hashtable h = new Hashtable();
		enumerateStringProperties(h);
		return h.keySet();
	}

	public void list(PrintStream out) {
		out.println("-- listing properties --");
		Hashtable h = new Hashtable();
		enumerate(h);
		for (Enumeration e = h.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String val = (String) h.get(key);
			if (val.length() > 40) {
				val = val.substring(0, 37) + "...";
			}
			out.println(key + "=" + val);
		}
	}

	public void list(PrintWriter out) {
		out.println("-- listing properties --");
		Hashtable h = new Hashtable();
		enumerate(h);
		for (Enumeration e = h.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String val = (String) h.get(key);
			if (val.length() > 40) {
				val = val.substring(0, 37) + "...";
			}
			out.println(key + "=" + val);
		}
	}

	private synchronized void enumerate(Hashtable h) {
		if (this.defaults != null) {
			this.defaults.enumerate(h);
		}
		for (Enumeration e = keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			h.put(key, get(key));
		}
	}

	private synchronized void enumerateStringProperties(Hashtable h) {
		if (this.defaults != null) {
			this.defaults.enumerateStringProperties(h);
		}
		for (Enumeration e = keys(); e.hasMoreElements();) {
			Object k = e.nextElement();
			Object v = get(k);
			if (((k instanceof String)) && ((v instanceof String)))
				h.put((String) k, (String) v);
		}
	}

	private static char toHex(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}

	class LineReader {
		byte[] inByteBuf;
		char[] inCharBuf;
		char[] lineBuf = new char[1024];
		int inLimit = 0;
		int inOff = 0;
		InputStream inStream;
		Reader reader;

		public LineReader(InputStream inStream) {
			this.inStream = inStream;
			this.inByteBuf = new byte[8192];
		}

		public LineReader(Reader reader) {
			this.reader = reader;
			this.inCharBuf = new char[8192];
		}

		int readLine() throws IOException {
			int len = 0;
			char c = '\000';

			boolean skipWhiteSpace = true;
			boolean isCommentLine = false;
			boolean isNewLine = true;
			boolean appendedLineBegin = false;
			boolean precedingBackslash = false;
			boolean skipLF = false;
			while (true) {
				if (this.inOff >= this.inLimit) {
					this.inLimit = (this.inStream == null ? this.reader.read(this.inCharBuf)
							: this.inStream.read(this.inByteBuf));
					this.inOff = 0;
					if (this.inLimit <= 0) {
						if ((len == 0) || (isCommentLine)) {
							return -1;
						}
						return len;
					}
				}
				if (this.inStream != null) {
					c = (char) (0xFF & this.inByteBuf[(this.inOff++)]);
				} else
					c = this.inCharBuf[(this.inOff++)];

				if (skipLF) {
					skipLF = false;
					if (c == '\n')
						;
				} else if (skipWhiteSpace) {
					if ((c != ' ') && (c != '\t') && (c != '\f')
							&& ((appendedLineBegin) || ((c != '\r') && (c != '\n')))) {
						skipWhiteSpace = false;
						appendedLineBegin = false;
					}
				} else if (isNewLine) {
					isNewLine = false;
					if ((c == '#') || (c == '!')) {
						isCommentLine = true;
					}

				} else if ((c != '\n') && (c != '\r')) {
					this.lineBuf[(len++)] = c;
					if (len == this.lineBuf.length) {
						int newLength = this.lineBuf.length * 2;
						if (newLength < 0) {
							newLength = 2147483647;
						}
						char[] buf = new char[newLength];
						System.arraycopy(this.lineBuf, 0, buf, 0, this.lineBuf.length);
						this.lineBuf = buf;
					}

					if (c == '\\')
						precedingBackslash = !precedingBackslash;
					else {
						precedingBackslash = false;
					}

				} else if ((isCommentLine) || (len == 0)) {
					isCommentLine = false;
					isNewLine = true;
					skipWhiteSpace = true;
					len = 0;
				} else {
					if (this.inOff >= this.inLimit) {
						this.inLimit = (this.inStream == null ? this.reader.read(this.inCharBuf)
								: this.inStream.read(this.inByteBuf));
						this.inOff = 0;
						if (this.inLimit <= 0) {
							return len;
						}
					}
					if (!precedingBackslash)
						break;
					len--;

					skipWhiteSpace = true;
					appendedLineBegin = true;
					precedingBackslash = false;
					if (c == '\r')
						skipLF = true;
				}
			}
			return len;
		}
	}
}
