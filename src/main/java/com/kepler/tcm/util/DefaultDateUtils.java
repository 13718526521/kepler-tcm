package com.kepler.tcm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * 
* 类描述：  时间操作类 
* 创建人：姚林刚   
* 创建时间：2015年3月11日 下午4:55:01   
* 修改人：Administrator   
* 修改时间：2015年3月11日 下午4:55:01   
* 修改备注：   
* @version
 */
public class DefaultDateUtils {
	private static SimpleDateFormat mmDateTime = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private static SimpleDateFormat spDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat ypDateTime = new SimpleDateFormat("yyyyMMddHHmmss");
	private static SimpleDateFormat spDate = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat spTime = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat sp = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat ym = new SimpleDateFormat("yyyyMM");
	
	public static String time2mmString(Date date){
		return mmDateTime.format(date);
	}
	public static String ym2String(Date date){		
		return ym.format(date);
	}
	public static String dateTime2NString(Date date){
		return ypDateTime.format(date);
	}
	public static String simDateTime2String(Date date){		
		return sp.format(date);
	}
	public static String dateTime2String(Date date){
		return spDateTime.format(date);
	}
	
	public static String date2String(Date date){
		return spDate.format(date);
	}
	
	public static String time2String(Date date){
		return spTime.format(date);
	}
	
	public static String time2String(String format,Date date){
		SimpleDateFormat mySp = new SimpleDateFormat(format);
		return mySp.format(date);
	}
	
	public static Date string2Time(String format,String str){
		SimpleDateFormat mySp = new SimpleDateFormat(format);
		if("".equals(str)||str==null||"".equals(format)||format==null){
			return null;
		}
		try {
			return mySp.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date string2DateTime(String str){
		if("".equals(str)||str==null){
			return null;
		}
		try {
			return spDateTime.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date string2Date(String str){
		if("".equals(str)||str==null){
			return null;
		}
		try {
			return spDate.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date string2Time(String str){
		if("".equals(str)||str==null){
			return null;
		}
		try {
			return spTime.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int getYear(){
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	public static int getMonth(){
		return Calendar.getInstance().get(Calendar.MONTH)+1;
	}
	
	public static int getDay(){
		return Calendar.getInstance().get(Calendar.DATE);
	}
	

	public static int getYear(Date date) {
		if (date == null)
			return 0;
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		int year = now.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 获取该日期的月份
	 * @param date 日期
	 * @return 对应的月份
	 */
	public static int getMonth(Date date) {
		if (date == null)
			return 0;
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		int month = now.get(Calendar.MONTH);
		return month;
	}

	/**
	 * 获取该日期在当前月的最大天数
	 * @param date 日期
	 * @return 天数
	 */
	public static int getDaysOfMonth(Date date) {
		if (date == null)
			return 0;
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		int days = now.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days;
	}

	/**
	 * 获取当前日期
	 * @return 格式：yyyy年MM月dd日
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	/**
	 * 
	 * @Description: 获取当前日期
	 * @param format
	 * @return
	 */
	public static String getStringDateShort(String format) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	/**
	 * 获得未来指定月后的时间
	 * @param amount
	 * @return
	 */
	public static Date getAfterMonth(int amount){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, amount);
		return calendar.getTime();
	}
	
	/**
	 * 获得未来指定天后的时间
	 * @param amount
	 * @return
	 */
	public static Date getAfterDay(int amount){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, amount);
		return calendar.getTime();
	}
	
	/**
	 * 获得未来指定小时后的时间
	 * @param amount
	 * @return
	 */
	public static Date getAfterHour(int amount){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, amount);
		return calendar.getTime();
	}
	
	/**
	 * 获得未来指定分钟后的时间
	 * @param amount
	 * @return
	 */
	public static Date getAfterMinute(int amount){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, amount);
		return calendar.getTime();
	}
	/**
	 * 时间范围校验
	 * @param curTime  当前时间
	 * @param sdFormat 校验的日期格式 ：yyyy-MM-dd-HH:mm:ss
	 *    eg : 10:00-10:37 ---->HH:mm
	 * @param ranges  时间范围段 ，支持可扩展多个时间段
	 * @return   Boolean 
	 * @throws ParseException
	 */
	public static boolean rangeVerify(Date curTime,String sdFormat, String ... ranges) throws ParseException{
		boolean flag = false ;
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		if(sdFormat == null || sdFormat == ""){
			sdFormat = "HH:mm";
		}
		SimpleDateFormat sdf2 = new SimpleDateFormat(sdFormat);
		String curTimeStr = sdf1.format(curTime);
		curTime = sdf2.parse(curTimeStr.substring(curTimeStr.lastIndexOf("-")+1));
		for(String range : ranges ){
			//数值转换9:00-12:00 
			String[] rangDates  = range.trim().split("-");
			long startTime = sdf2.parse(rangDates[0].trim()).getTime();
			long endTime = sdf2.parse(rangDates[1].trim()).getTime();
			if(curTime.getTime() >= startTime && curTime.getTime() <= endTime){
				flag = true ;
				break ;
			}
		}
		return flag;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(rangeVerify(new Date(), "HH:mm","10:00-10:37","12:00-21:00"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
