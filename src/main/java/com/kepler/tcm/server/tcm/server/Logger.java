package com.kepler.tcm.server.tcm.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

public class Logger {
	private File logFile;
	private FileWriter fileWriter;
	private int nMaxSize = 0;

	public Logger(String strFilePath) throws IOException {
		this.logFile = new File(strFilePath);
		if (!this.logFile.exists()) {
			if (!this.logFile.createNewFile()) {
				throw new IOException("创建日志文件" + strFilePath + "失败");
			}
		}
		this.fileWriter = new FileWriter(this.logFile, true);
	}

	public Logger(String strFilePath, int nSize) throws IOException {
		this(strFilePath);
		this.nMaxSize = nSize;
	}

	public synchronized void log(String strFrom, String strMessage) {
		String strNowDate = DateTool.getCurrentDate();
		String strLine = strNowDate + "|" + strFrom + "|" + strMessage.trim() + System.getProperty("line.separator");
		try {
			this.fileWriter.write(strLine);
			this.fileWriter.flush();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public synchronized void log(String strMessage) {
		String strNowDate = DateTool.getCurrentDate();
		String strLine = strNowDate + "|" + strMessage.trim() + System.getProperty("line.separator");
		try {
			this.fileWriter.write(strLine);
			this.fileWriter.flush();
			if ((this.nMaxSize > 0) && (this.logFile.length() >= this.nMaxSize * 1000000)) {
				try {
					this.fileWriter.close();
					this.fileWriter = new FileWriter(this.logFile, false);
				} catch (Exception localException1) {
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void close() {
		try {
			this.fileWriter.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}