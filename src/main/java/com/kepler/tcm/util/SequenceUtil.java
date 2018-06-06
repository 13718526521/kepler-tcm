package com.kepler.tcm.util;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 序列工具类
 * @author wangsp
 * @date 2017年5月6日
 * @version V1.0
 */
public class SequenceUtil {

	
	private static final int IP;
	
	private static short counter = (short) 0;

	private static final int JVM = (int) (System.currentTimeMillis() >>> 8);
	
	/**
	 * 初始化IP
	 */
	static {
		int ipadd;
		try {
			ipadd = toInt(InetAddress.getLocalHost().getAddress());
		} catch (Exception e) {
			ipadd = 0;
		}
		IP = ipadd;
	}
	
	/**
	 * 生成32位UUID随机码，可做主键使用
	 * @return UUID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	
	/**
	 * 生成AppSecret
	 */
	public static String getAppSecret(String appId){
		return  CipherUtil.md5calc(appId);
	}
	
	/**
	 * 生成TOKEN
	 */
	public static String getToken(String appId){
		String[] str = {"+","/","?","%","#","&"};
		
		String result = CipherUtil.sha256(appId).trim();
		for (int i = 0; i < str.length; i++) {
			result = result.replace(str[i], "-");
		}
		return result ;
	}
	
	/**
	 * 生成PUBLIC_ID
	 */
	public static String getPublicId(String appId){
		String result = "sa_" ;
		StringBuffer buffer = new StringBuffer();
		String s = CipherUtil.md5calc(appId);
		int n = s.length();
		for(int i = 0; i < n; i++){
			// 因为索引是从0开始，所以索引为偶数的是奇数位字符
			if(i % 2 == 0){
				buffer.append(s.charAt(i));
			}
		}
		result = result + buffer  ;
		return result ;
	}
	
	/**
	 * 生成4位验证码，做校验码使用
	 * @return  验证码
	 */
	public static String getVerifyCode(){
		String[] beforeShuffle = new String[] { "2", "3", "4", "5", "6", "7",  
				  "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",  
				  "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",  
				  "W", "X", "Y", "Z" };  
	     List<String> list = Arrays.asList(beforeShuffle);  
	     Collections.shuffle(list);  
	     StringBuilder sb = new StringBuilder();  
	     for (int i = 0; i < list.size(); i++) {  
	         sb.append(list.get(i));  
	     }  
	    String afterShuffle = sb.toString();  
	    String resultStr = afterShuffle.substring(5, 9);  
		return  resultStr;
	}
	/**
	 * 生成6位短信验证码
	 */
	public static String getSMSCode(){
		Random random = new Random();
		String result="";
		for(int i=0;i<6;i++){
			result+=random.nextInt(10);
		}
		return  result;
	}
	
	/**
	 * 产生一个32位的id
	 * */
	public static String getGenerateID() {
		return new StringBuilder(32).append(format(getIP()))
			.append(format(getJVM())).append(format(getHiTime()))
			.append(format(getLoTime())).append(format(getCount()))
			.toString();
	}

	private final static String format(int intval) {
		String formatted = Integer.toHexString(intval);
		StringBuilder buf = new StringBuilder("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	private final static String format(short shortval) {
		String formatted = Integer.toHexString(shortval);
		StringBuilder buf = new StringBuilder("0000");
		buf.replace(4 - formatted.length(), 4, formatted);
		return buf.toString();
	}

	private final static int getJVM() {
		return JVM;
	}

	private final static short getCount() {
		synchronized (SequenceUtil.class) {
			if (counter < 0)
				counter = 0;
			return counter++;
		}
	}

	/**
	 * Unique in a local network
	 */
	private final static int getIP() {
		return IP;
	}

	/**
	 * Unique down to millisecond
	 */
	private final static short getHiTime() {
		return (short) (System.currentTimeMillis() >>> 32);
	}

	private final static int getLoTime() {
		return (int) System.currentTimeMillis();
	}

	private final static int toInt(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}
	
	public static String randomNum(int step) {
		StringBuilder str=new StringBuilder();//定义变长字符串
		Random random=new Random();
		//随机生成数字，并添加到字符串
		for(int i = 0;i < step ; i++){
			str.append(random.nextInt(10));
		}
		return str.toString();		
	}
	
   public static void main(String[] args) {
	   String str = "bon";
	   System.out.println(getVerifyCode());
	   System.out.println(getSMSCode());
	   System.out.println(getUUID());;
	   System.out.println(getGenerateID());
	   System.out.println(getToken(str));
	   System.out.println(getAppSecret(str));
	   System.out.println(getPublicId(str));
	   
   }
}
