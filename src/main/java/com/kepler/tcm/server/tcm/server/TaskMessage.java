package com.kepler.tcm.server.tcm.server;

import java.io.Serializable;

public class TaskMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private int MaxLength = 11;

	private int succNum = 0;

	private int waitNum = 0;

	private int failNum = 0;

	private int passNum = 0;

	private String other = "";

	private String lastSuccTime = DateTool.getCurrentDate();
	private String lastFailTime = DateTool.getCurrentDate();
	private String lastWaitTime = DateTool.getCurrentDate();
	private String lastPassTime = DateTool.getCurrentDate();

	public int incSucc() {
		this.lastSuccTime = DateTool.getCurrentDate();
		return ++this.succNum;
	}

	public int incWait() {
		this.lastWaitTime = DateTool.getCurrentDate();
		return ++this.waitNum;
	}

	public int incFail() {
		this.lastFailTime = DateTool.getCurrentDate();
		return ++this.failNum;
	}

	public int incPass() {
		this.lastPassTime = DateTool.getCurrentDate();
		return ++this.passNum;
	}

	public int decSucc() {
		this.lastSuccTime = DateTool.getCurrentDate();
		return --this.succNum;
	}

	public int deccWait() {
		this.lastWaitTime = DateTool.getCurrentDate();
		return --this.waitNum;
	}

	public int decFail() {
		this.lastFailTime = DateTool.getCurrentDate();
		return --this.failNum;
	}

	public int decPass() {
		this.lastPassTime = DateTool.getCurrentDate();
		return --this.passNum;
	}

	public String getOther() {
		return this.other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getPassNum() {
		return this.passNum + "";
	}

	public void setPassNum(int passNum) {
		this.lastPassTime = DateTool.getCurrentDate();
		this.passNum = passNum;
	}

	public String getSuccNum() {
		return this.succNum + "";
	}

	public void setSuccNum(int succNum) {
		this.lastSuccTime = DateTool.getCurrentDate();
		this.succNum = succNum;
	}

	public String getWaitNum() {
		return this.waitNum + "";
	}

	public int getWait() {
		return this.waitNum;
	}

	public void setWaitNum(int waitNum) {
		this.lastWaitTime = DateTool.getCurrentDate();
		this.waitNum = waitNum;
	}

	public String getFailNum() {
		return this.failNum + "";
	}

	public int getFail() {
		return this.failNum;
	}

	public void setFailNum(int failNum) {
		this.lastFailTime = DateTool.getCurrentDate();
		this.failNum = failNum;
	}

	public String getLastSuccTime() {
		return this.lastSuccTime;
	}

	public String getLastFailTime() {
		return this.lastFailTime;
	}

	public String getLastWaitTime() {
		return this.lastWaitTime;
	}

	public String getLastPassTime() {
		return this.lastPassTime;
	}

	private String format(String input) {
		input = input.trim();
		if ("".equals(input)) {
			return input;
		}
		StringBuffer sb = new StringBuffer(this.MaxLength);
		int n = input.length();
		if (n >= this.MaxLength) {
			return input;
		}
		for (int i = 0; i < this.MaxLength - n; i++) {
			sb.append(" ");
		}
		sb.append(input);
		return sb.toString();
	}
}
