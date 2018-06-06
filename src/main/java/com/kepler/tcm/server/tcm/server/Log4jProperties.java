package com.kepler.tcm.server.tcm.server;

public class Log4jProperties implements Comparable {
	private String logType = "";

	private String logMaxSize = "";

	private String logBackNums = "";

	private String logLevel = "";
	private String logLevel2 = "";

	private String merged = "";

	public Log4jProperties(String logType, String logMaxSize, String logBackNums, String logLevel, String logLevel2,
			String merged) {
		this.logType = logType;
		this.logMaxSize = logMaxSize;
		this.logBackNums = logBackNums;
		this.logLevel = logLevel;
		this.logLevel2 = logLevel2;
		this.merged = merged;
	}

	public Log4jProperties() {
	}

	public String getLogBackNums() {
		return this.logBackNums;
	}

	public void setLogBackNums(String logBackNums) {
		this.logBackNums = logBackNums;
	}

	public String getLogLevel() {
		return this.logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getLogLevel2() {
		return this.logLevel2;
	}

	public void setLogLevel2(String logLevel2) {
		this.logLevel2 = logLevel2;
	}

	public String getLogMaxSize() {
		return this.logMaxSize;
	}

	public void setLogMaxSize(String logMaxSize) {
		this.logMaxSize = logMaxSize;
	}

	public String getLogType() {
		return this.logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getMerged() {
		return this.merged;
	}

	public void setMerged(String merged) {
		this.merged = merged;
	}

	private boolean e(Object o1, Object o2) {
		if ((o1 == null) && (o2 == null))
			return true;
		if (o1 == null)
			return o2.equals(o1);
		return o1.equals(o2);
	}

	public int compareTo(Object o) {
		Log4jProperties lp2 = (Log4jProperties) o;
		if ((e(this.logBackNums, lp2.logBackNums)) && (e(this.logType, lp2.logType)) && (e(this.merged, lp2.merged))
				&& (e(this.logLevel, lp2.logLevel)) && (e(this.logLevel2, lp2.logLevel2))
				&& (e(this.logBackNums, lp2.logBackNums))) {
			return 0;
		}
		return -1;
	}
}