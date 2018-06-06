package com.kepler.tcm.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * hash 算法
 * @author wangsp
 * @date 2017年3月29日
 * @version V1.0
 */
public class CipherUtil {

	private static char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D',
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '+', '/', };

	private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59,
			60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1,
			-1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
			38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1,
			-1, -1 };
	
	
	/**
	 * MD5算法
     * @param originstr 
     * @return 
     */
    public final static String md5calc(String originstr){

        String result = null;

        char hexDigits[] = {
             '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        if(originstr != null){
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");

                byte[] source = originstr.getBytes("utf-8");

                md.update(source);

  
                byte[] tmp = md.digest();

 
                char[] str = new char[32];

                for(int i=0,j=0; i < 16; i++){
                    byte b = tmp[i];


                    str[j++] = hexDigits[b>>>4 & 0xf];

                    str[j++] = hexDigits[b&0xf];
                }
                result = new String(str);
            } catch (NoSuchAlgorithmException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
        }
        return result;
    }
    /**
    *
    * @param dataString  	
    * @param md5String		
    * @return
    */
	public final static boolean validateMD5(String dataString, String md5String){
		if(md5String.equals(md5calc(dataString))){
           return true;
       }
       return false;
	}
	
	/**
	 * 混淆字符串
	 * @param source	要混淆的字符串
	 * @param pos		用于混淆的字符串种子
	 * @return			混淆后的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String puzzle(String source, String pos) throws UnsupportedEncodingException{
		return puzzle(source, pos.getBytes("UTF-8"));
	}
	
	private static String puzzle(String source, byte[] pos){
		int sourceLength = source.length();
		StringBuilder sb = new StringBuilder();
		sb.append( (char)(sourceLength+0x20));  //+0x20转换为可见字符。
		sb.append(source);
//		sb.append(getChecksum(source));
		
//		sb.append(StringUtils.repeat(padChar, length-sb.length()));
		int length = sb.length();
		for(int index:pos){
			index = Math.abs(index) % (length);
			char c = sb.charAt(index);
			sb.setCharAt(index, sb.charAt(length-index-1));
			sb.setCharAt(length-index-1, c);
		}
		
		return sb.toString();
	}
	
	/**
	 * 去除对字符串的混淆
	 * @param puzzleString		要去除混淆的字符串
	 * @param pos				混淆用的字符串种子
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String clearPuzzle(String puzzleString, String pos) throws UnsupportedEncodingException{
		return clearPuzzle(puzzleString, pos.getBytes("UTF-8"));
	}
	
	private static String clearPuzzle(String puzzleString,byte[] pos){
		StringBuilder sb = new StringBuilder(puzzleString);
		int length = sb.length();
		for(int i=pos.length-1; i>=0; i--){
			int index = Math.abs(pos[i]);
			index = index % (length);
			char c = sb.charAt(index);
			sb.setCharAt(index, sb.charAt(length-index-1));
			sb.setCharAt(length-index-1, c);
		}
		int realLength = sb.charAt(0)-0x20;				//从可见字符转换为数字
		return sb.substring(1, realLength+1);
	}
	
	/**
	 * 
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static  byte[] getMD5(String password) throws NoSuchAlgorithmException{
		//根据MD5算法生成MessageDigest对象  
        MessageDigest md5 = MessageDigest.getInstance("MD5");  
        byte[] srcBytes = password.getBytes();  
        //使用srcBytes更新摘要  
        md5.update(srcBytes);  
        //完成哈希计算，得到result  
        byte[] resultBytes = md5.digest();  
        return resultBytes;  
	}
	
	/**
	 * 
	 * @param info
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getSHA(String info) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("SHA");
		byte[] srcBytes = info.getBytes();
		// 使用srcBytes更新摘要
		md5.update(srcBytes);
		// 完成哈希计算，得到result
		byte[] resultBytes = md5.digest();
		return resultBytes;
	}
	
	/**
	 * 
	 * @param privateKey
	 * @param srcBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] getRSA(RSAPrivateKey privateKey, byte[] srcBytes)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if (privateKey != null) {
			// Cipher负责完成加密或解密工作，基于RSA
			Cipher cipher = Cipher.getInstance("RSA");
			// 根据公钥，对Cipher对象进行初始化
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] resultBytes = cipher.doFinal(srcBytes);
			return resultBytes;
		}
		return null;
	}
	
/*	// 将 s 进行 BASE64 编码
	public static String getBASE64(String s) {
		if (s == null)
			return null;
		return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	}

	// 将 BASE64 编码的字符串 s 进行解码
	public static String getFromBASE64(String s) {
		if (s == null)
			return null;
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}*/

	public static String ShaEncode(String source,String salt){
		ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder();
		return shaPasswordEncoder.encodePassword(source, salt);
	}
	
	public static String Md5Encode(String source,String salt){
		Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
		return md5PasswordEncoder.encodePassword(source, salt);
	}
	
	/**
	 * spring security  MD5加密
	 * @return
	 */
	public static String md5(String source) {       
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();       
        // false 表示：生成32位的Hex版, 这也是encodeHashAsBase64的, Acegi 默认配置; true  表示：生成24位的Base64版       
        md5.setEncodeHashAsBase64(true);       
        return md5.encodePassword(source, null);       
    } 
	
	/**
	 * spring security  SHA1加密 1, 256, 384, 512
	 * @return
	 */
    public static String sha256(String source) {         
		ShaPasswordEncoder sha = new ShaPasswordEncoder(256);       
		sha.setEncodeHashAsBase64(true);
        return sha.encodePassword(source, null);       
    }       
           
          
    public static String sha384(String source) {       
        ShaPasswordEncoder sha = new ShaPasswordEncoder(384);       
        sha.setEncodeHashAsBase64(true);       
        return sha.encodePassword(source, null);        
    }
    
    public static String sha512(String source) {       
        ShaPasswordEncoder sha = new ShaPasswordEncoder(512);       
        sha.setEncodeHashAsBase64(true);       
        return sha.encodePassword(source, null);        
    } 
           
          
    public static String md5SystemWideSaltSource (String source) {       
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();       
        md5.setEncodeHashAsBase64(true);       
               
        // 使用动态加密盐的只需要在注册用户的时候将第二个参数换成用户名即可       
        return md5.encodePassword(source, null);       
    }

	/**
	 * 解码 用于对jsbase64的解码，主要防止中文乱码以及冲突，与 resource/js/base64.js 配套使用
	 * @param str
	 * @return
	 */
	public static byte[] decodeBase64(String str) {
		byte[] data = str.getBytes();
		int len = data.length;
		ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
		int i = 0;
		int b1, b2, b3, b4;

		while (i < len) {
			do {
				b1 = base64DecodeChars[data[i++]];
			} while (i < len && b1 == -1);
			if (b1 == -1) {
				break;
			}

			do {
				b2 = base64DecodeChars[data[i++]];
			} while (i < len && b2 == -1);
			if (b2 == -1) {
				break;
			}
			buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

			do {
				b3 = data[i++];
				if (b3 == 61) {
					return buf.toByteArray();
				}
				b3 = base64DecodeChars[b3];
			} while (i < len && b3 == -1);
			if (b3 == -1) {
				break;
			}
			buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

			do {
				b4 = data[i++];
				if (b4 == 61) {
					return buf.toByteArray();
				}
				b4 = base64DecodeChars[b4];
			} while (i < len && b4 == -1);
			if (b4 == -1) {
				break;
			}
			buf.write((int) (((b3 & 0x03) << 6) | b4));
		}
		return buf.toByteArray();
	}

    
	
	public static void main(String arg[]) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		String str = "bonc";
		System.out.println("sha256="+sha256(str));
		System.out.println("sha384="+sha384(str));
		System.out.println("sha512="+sha512(str));
		System.out.println("md5="+md5(str));
		
	}
	
}
