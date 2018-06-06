package com.kepler.tcm.server.tcm.server;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTool {
	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static final DecimalFormat decimalFormat = new DecimalFormat("00");

	private String nowDate = "";
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;

	public static void main(String[] args) {
		System.out.println(simpleDateFormat.format(Calendar.getInstance().getTime()));
	}

	public DateTool(int day) {
		Calendar cal = Calendar.getInstance();
		this.nowDate = simpleDateFormat.format(cal.getTime());
		cal.add(5, day);
		this.year = cal.get(1);
		this.month = (cal.get(2) + 1);
		this.day = cal.get(5);
		this.hour = cal.get(10);
		this.minute = cal.get(12);
		this.second = cal.get(13);
	}

	public String getNowDate() {
		return this.nowDate;
	}

	public int getDay() {
		return this.day;
	}

	public int getMonth() {
		return this.month;
	}

	public int getYear() {
		return this.year;
	}

	public static String getCurrentDate() {
		return simpleDateFormat.format(Calendar.getInstance().getTime());
	}

	public static Date parse(String date) {
		try {
			return simpleDateFormat.parse(date);
		} catch (Exception e) {
		}
		return null;
	}

	public int getHour() {
		return this.hour;
	}

	public int getMinute() {
		return this.minute;
	}

	public int getSecond() {
		return this.second;
	}

	public String getFormatYear() {
		return decimalFormat.format(this.year);
	}

	public String getFormatDay() {
		return decimalFormat.format(this.day);
	}

	public String getFormatMonth() {
		return decimalFormat.format(this.month);
	}

	public String getFormatHour() {
		return decimalFormat.format(this.hour);
	}

	public String getFormatMinute() {
		return decimalFormat.format(this.minute);
	}

	public String getFormatSecond() {
		return decimalFormat.format(this.second);
	}
}