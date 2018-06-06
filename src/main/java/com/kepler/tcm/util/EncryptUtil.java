package com.kepler.tcm.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;
/**
 * 加密解密
 * @author Administrator
 *
 */
@SuppressWarnings("restriction")
public class EncryptUtil {

	private static final String KEY = "abcdefgabcdefg12"; //AES秘钥  必须16位
	
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding"; 
    /**
     * base64加密
     * @param bytes
     * @return
     */
    public static String base64Encode(byte[] bytes){  
        return new String(Base64.encodeBase64(bytes));
    }  
    /**
     * base64解密
     * @param base64Code
     * @return
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception{  
        return new BASE64Decoder().decodeBuffer(base64Code);  
    }
    
    /**
     * 加密
     * @param content
     * @param encryptKey
     * @return
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128);  
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));  

        return cipher.doFinal(content.getBytes("utf-8"));  
    }  
    public static String aesEncrypt(String content, String encryptKey) throws Exception {  
        return base64Encode(aesEncryptToBytes(content, encryptKey));  
    }
    /**
     * 解密
     * @param encryptBytes
     * @param decryptKey
     * @return
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128);  

        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));  
        byte[] decryptBytes = cipher.doFinal(encryptBytes);  

        return new String(decryptBytes);  
    }  
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {  
        return aesDecryptByBytes(base64Decode(encryptStr), decryptKey);  
    }  


    /**
     * 测试
     * 
     */
    public static void main(String[] args) throws Exception {

    	String st="hSIShbuxIs6wyLIBDCzOsg==";
    	String key="3a7327a6";
    	System.out.println(aesDecrypt(st,key));
    }
}
