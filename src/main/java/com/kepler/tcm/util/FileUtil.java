package com.kepler.tcm.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;

import org.assertj.core.util.DateUtil;
import org.springframework.web.multipart.MultipartFile;

import com.kepler.tcm.common.constants.ConfigConstant;
import com.netflix.discovery.util.SystemUtil;




/**
 * 
* 类描述：   文件操作帮助类
* 创建人：姚林刚   
* 创建时间：2015年3月11日 下午4:50:44   
* 修改人：wangzc   
* 修改时间：2015年6月27日 下午4:50:44   
* 修改备注：   更新删除文件方法逻辑，增加文件流处理方法
* @version
 */
public class FileUtil {
	
	/**
	 * 创建文件方法
	 * @param path
	 * @return
	 */
	public static boolean createFile(String path) {
		Pattern pattern = Pattern.compile(System.getProperty("file.separator")+"+"); 
		boolean b = true;
		File file = null;
		String[] fileParents = pattern.split(path);
		for (String fileParent : fileParents) {
			if (file == null) {
				file = new File(fileParent);
			} else {
				file = new File(file.getPath()
						+ System.getProperty("file.separator") + fileParent);
			}
			if (!file.exists()) {
				if (!file.mkdir()) {
					b = false;
					return b;
				}
			}
		}
		return b;
	}
	
	/**
	 * 循环递归层级创建方法，如果上级文件夹不存在，则创建
	 * @return 
	 */
	public static void createFileRec(String path) throws Exception{
		if(path==null||"".equals(path)){
			return;
		}
		Pattern pattern = Pattern.compile("\\"+System.getProperty("file.separator")+"+"); 
		boolean b = true;
		String[] fileParents = pattern.split(path);
		String parentPath = path.substring(0,path.lastIndexOf(fileParents[fileParents.length-1]));
		if(new File(parentPath).exists()){
			new File(path).mkdir();
		}else{
			createFileRec(parentPath);
			new File(path).mkdir();
		}
	}
	
	public static void main(String arg[]){
		try {
			createFileRec("F:\\w\\fd\\a\\df\\dafg");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断linux系统路径   or  windows系统路径
	 * @param path
	 * @return
	 */
	public static String getFileStorePath(String rootPath, String linuxPath,String windowPath){
		String fileLocation = linuxPath;
		File f1 = new File(fileLocation);
		if(!f1.exists() || !f1.isDirectory()){
			String s1 = rootPath;
			fileLocation = s1+fileLocation;
			f1 = new File(fileLocation);
			if(!f1.exists() || !f1.isDirectory()){
				fileLocation = (new StringBuilder()).append(s1).append(windowPath).toString();
				f1 = new File(fileLocation);
				f1.mkdirs();
			}
		}else{
			fileLocation = fileLocation.replace('\\', '/');
		}
		
		return fileLocation;
	}
	public static void createDirIfNotExist(String file_path) {
		File file = new File(file_path);
		if(!file.exists()){
			createFile(file_path);
		}
		
	}

	/**
	 * 递归清空子文件夹方法
	 * @param dir
	 */
	public static void deleteFolder(String dir) {
		File delfolder = new File(dir);
		if (!delfolder.exists()) {
			return;
		}
		File oldFile[] = delfolder.listFiles();
		try {
			for (int i = 0; i < oldFile.length; i++) {
				if (oldFile[i].isDirectory()) {
					deleteFolder(dir + System.getProperty("file.separator") + oldFile[i].getName()); // 递归清空子文件夹
				}
				oldFile[i].delete();
			}
		} catch (Exception e) {
			System.out.println("清空文件夹操作出错!");
			e.printStackTrace();
		}
	}

	/**
	 * 复制文件方法,以字符串作为参数
	 * @param target
	 * @param source
	 * @return
	 */
	public static boolean copyFile(String target, String source) {
		try {
			InputStream ips = new FileInputStream(source);
			OutputStream ops = new FileOutputStream(target);
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = ips.read(buffer)) > 0) {
				ops.write(buffer, 0, length);
			}
			ops.close();
			ips.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 复制文件方法,以流作为参数
	 * @param ips
	 * @param ops
	 * @return
	 */
	public static boolean copyFile(InputStream ips, OutputStream ops) {
		try {
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = ips.read(buffer)) > 0) {
				ops.write(buffer, 0, length);
			}
			ops.close();
			ips.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 复制文件方法
	 * @param target
	 * @param source
	 * @return
	 */
	public static boolean copyFile(File target, File source) {
		try {
			InputStream ips = new FileInputStream(source);
			OutputStream ops = new FileOutputStream(target);
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = ips.read(buffer)) > 0) {
				ops.write(buffer, 0, length);
			}
			ops.close();
			ips.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 复制文件方法,出入源文件为springMVC提供的对象
	 * @param target
	 * @param multipartFile
	 * @return
	 */
	public static boolean copyFile(File target, MultipartFile multipartFile) {
		try {
			InputStream ips = multipartFile.getInputStream();
			OutputStream ops = new FileOutputStream(target);
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = ips.read(buffer)) > 0) {
				ops.write(buffer, 0, length);
			}
			ops.close();
			ips.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
    /**
     * 内容写入文件方法
     * @throws Exception 
     * @throws IOException 
     * */
    public static boolean makeFile(String filepath,String content) throws Exception{
    	//System.out.println("filepath is :"+filepath+"=======================");
    	File file=new File(filepath);
    	if(!file.exists()){
    		FileWriter fw = null;
        	PrintWriter out = null;
    		try{
    			file.createNewFile();
    			fw = new FileWriter(filepath);
    			out=new PrintWriter(fw);
    			out.write(content);
    			out.flush();
    		}catch(Exception e){
    			throw new Exception(e.getMessage());
    		}finally{
    			out.close();
        		fw.close();
    		}
    		return true;
    	}else{
    		return false;
    	}
    }
	/**
	 * 删除整个目录，包含所有子目录和文件
	 * 
	 * @param path
	 */
	public static void deleteDirs(String path) {
		File rootFile = new File(path);
		if (!rootFile.exists()) {
			return;
		}
		File[] files = rootFile.listFiles();
		if (null == files) {
			return;
		}
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				deleteDirs(file.getPath());
			} else {
				file.delete();
			}
		}
		rootFile.delete();
	}
	
	/**
	 * 删除指定文件
	 * @param filePath
	 * @return
	 */
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if(!file.exists()) {
			return false;
		}
		return file.delete();
	}
	/**
	 * 文件夹复制
	 * 
	 * @param srcFolder
	 * @param destFolder
	 * @return
	 */
	/*public static boolean copyDirctory(String srcFolder, String destFolder) {
		File srcFile = new File(srcFolder);
		File destFile = new File(destFolder);
		if (!srcFile.exists() || (srcFile.isDirectory() && destFile.isFile())) {
			return false;
		}
		//文件copy到文件
		if(srcFile.isFile() && destFile.isFile()) {
			return copyFile(srcFolder, destFolder);
		}
		//创建目标目录
		if (!destFile.exists() && !destFile.isFile()) {
			destFile.mkdir();
		}
		//文件copy到目录
		if(srcFile.isFile()) {
			String srcFileName = srcFile.getName();
			String destFilePath = wrapFilePath(getFullFilePath(srcFileName, destFolder));
			return copyFile(wrapFilePath(srcFolder), destFilePath);
		}
		
		//目录copy到目录
		File[] allFiles = srcFile.listFiles();
		String srcName = null;
		String desName = null;
		for (File file : allFiles) {
			srcName = file.getName();
			if (file.isFile()) {
				desName = wrapFilePath(getFullFilePath(srcName, destFolder));
				copyFile(wrapFilePath(file.getAbsolutePath()), desName);
			} else {
				copyDirctory(wrapFilePath(file.getAbsolutePath()),getFullFilePath(srcName, destFolder));
			}
		}
		return true;
	}*/

	/**
	 * 得到某文件夹下的所有文件
	 * 
	 * @param path
	 * @return
	 */
	public static List<File> getAllFile(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		List<File> fileList = new ArrayList<File>();
		for (File f : files) {
			if (f.isFile()) {
				fileList.add(f);
			}
		}
		return fileList;
	}

	/**
	 * 读取文件
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String filePath) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(new File(filePath)));
		StringBuffer content = new StringBuffer();
		String line = null;
		while ((line = in.readLine()) != null) {
			content.append(line).append("\n");
		}
		return content.toString().replaceAll("\n$", "");
	}
	
	/**
	 * 读取文件
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFile(File file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		StringBuffer content = new StringBuffer();
		String line = null;
		while ((line = in.readLine()) != null) {
			content.append(line).append("\n");
		}
		return content.toString().replaceAll("\n$", "");
	}
	
	/**
	 * 把字符串以指定编码写入文件，<br/>可以指定写入方式：追加/覆盖
	 * @param content    写入的字符串
	 * @param filePath   文件保存路径
	 * @param charset    写入编码
	 * @param append     是否追加
	 */
	public static boolean writeFile(String content,String filePath,String charset,boolean append) {
		BufferedWriter writer = null;
		OutputStream os = null; 
		OutputStreamWriter osw = null; 
		try { 
	        os = new FileOutputStream(filePath,append); 
	        osw = new OutputStreamWriter(os, charset); 
	        writer = new BufferedWriter(osw);
	        writer.write(content); 
	        writer.flush();
	        return true;
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	        return false;
	    } finally {
	    	try {
	    		os.close();
	    		osw.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
	
	/**
	 * 把字符串以指定编码写入文件，<br/>可以指定写入方式：追加/覆盖
	 * <br/>默认写入方式为覆盖
	 * @param content    写入的字符串
	 * @param filePath   文件保存路径
	 * @param charset    写入编码
	 */
	public static boolean writeFile(String content,String filePath,String charset){
		return writeFile(content, filePath, charset, false);
	}
	
	/**
	 * 把字符串以指定编码写入文件，<br/>可以指定写入方式：追加/覆盖
	 * <br/>默认写入编码为UTF-8
	 * @param content    写入的字符串
	 * @param filePath   文件保存路径
	 * @param append     是否追加
	 */
	public static boolean writeFile(String content,String filePath,boolean append){
		return writeFile(content, filePath, "UTF-8", append);
	}
	
	/**
	 * 把字符串以指定编码写入文件，<br/>可以指定写入方式：追加/覆盖
	 * <br/>默认写入方式为覆盖,默认写入编码为UTF-8
	 * @param content    写入的字符串
	 * @param filePath   文件保存路径
	 */
	public static boolean writeFile(String content,String filePath){
		return writeFile(content, filePath, "UTF-8", false);
	}

	/**
	 * 下载文件，
	 * 
	 * @param link
	 * @param filePath
	 * @throws IOException
	 */
	public static void download(String link, String filePath)
			throws IOException {
		URL url = new URL(link);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				url.openStream()));
		String inputLine = null;
		FileOutputStream fos = new FileOutputStream(filePath);
		while ((inputLine = in.readLine()) != null) {
			fos.write(inputLine.getBytes());
		}
		in.close();
		fos.close();
	}

	/**
	 * 获取远程文件的输入流
	 * 
	 * @param url
	 * @return
	 */
	public static byte[] getBinaryDataFromURL(String link) throws IOException {
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		try {
			url = new URL(link);
			httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());
			return inputStream2ByteArray(bis);
		} catch (IOException e) {
			return null;
		} finally {
			httpUrl.disconnect();
		}
	}

	/**
	 * 字符串转换成Clob
	 * 
	 * @param string
	 * @return
	 */
	public static Clob string2Clob(String string) {
		if (string == null || string.equals("")) {
			return null;
		}
		try {
			return new SerialClob(string.toCharArray());
		} catch (SerialException e) {
			return null;
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * Clob转换成字符串
	 * 
	 * @param clob
	 * @return
	 */
	public static String clob2String(Clob clob) {
		if (null == clob) {
			return null;
		}
		try {
			return clob.getSubString(1L, (int) clob.length());
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * 字节数组转换成Clob
	 * 
	 * @param byteArray
	 * @param charsetName
	 * @return
	 */
	public static Clob byteArray2Clob(byte[] byteArray, String charsetName) {
		if (null == byteArray) {
			return null;
		}
		try {
			String string = new String(byteArray,
					charsetName == null ? "UTF-8"
							: charsetName);
			return string2Clob(string);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 字节数组转换成Clob(重载) 若不显式指定编码，默认编码为UTF-8
	 * 
	 * @param byteArray
	 * @param charsetName
	 * @return
	 */
	public static Clob byteArray2Clob(byte[] byteArray) {
		return byteArray2Clob(byteArray, null);
	}

	/**
	 * Clob转换成字节数组
	 * 
	 * @param clob
	 * @return
	 */
	public static byte[] clob2ByteArray(Clob clob) {
		if (null == clob) {
			return null;
		}
		InputStream in = null;
		byte[] byteArray = null;
		int length = 0;
		try {
			length = (int) clob.length();
			byteArray = new byte[length];
			in = clob.getAsciiStream();
		} catch (SQLException e) {
			return null;
		}
		int offset = 0;
		int n = 0;
		try {
			do {
				n = in.read(byteArray, offset, length - offset);
			} while (n != -1);
		} catch (IOException e) {
			return null;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return byteArray;
	}

	/**
	 * 字节数组转换成Blob
	 * 
	 * @param byteArray
	 * @return
	 * @throws SQLException
	 * @throws SerialException
	 */
	public static Blob byteArray2Blob(byte[] byteArray) {
		if (null == byteArray) {
			return null;
		}
		try {
			return new SerialBlob(byteArray);
		} catch (SerialException e) {
			return null;
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * Blob转换成字节数组
	 * 
	 * @param blob
	 * @return
	 */
	public static byte[] blob2ByteArray(Blob blob) {
		BufferedInputStream is = null;
		try {
			is = new BufferedInputStream(blob.getBinaryStream());
			byte[] bytes = new byte[(int) blob.length()];
			int len = bytes.length;
			int offset = 0;
			int read = 0;
			while (offset < len
					&& (read = is.read(bytes, offset, len - offset)) != -1) {
				offset += read;
			}
			return bytes;
		} catch (SQLException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				return null;
			}
		}
	}

	/**
	 * 字节数组转换成输入流
	 * 
	 * @param byteArray
	 *            字节数组
	 * @return
	 */
	public static InputStream byteArray2InputStream(byte[] byteArray) {
		return new ByteArrayInputStream(byteArray);
	}

	/**
	 * 输入流转换成字节数组
	 * 
	 * @param is
	 *            输入流对象
	 * @return
	 * @throws IOException
	 */
	public static byte[] inputStream2ByteArray(InputStream is)
			throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch = 0;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		is.close();
		return imgdata;
	}

	/**
	 * String转换成输入流
	 * 
	 * @param text
	 * @param charset
	 * @return
	 */
	public static InputStream string2InputStream(String text, String charset) {
		try {
			return new ByteArrayInputStream(text.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 输入流转换成String(速度快但耗内存)
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line).append("\n");
		}
		return buffer.toString();
	}

	/**
	 * 输入流转换成String(消耗资源少但速度慢)
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] bt = new byte[4096];
		int i = -1;
		while ((i = is.read(bt)) > 0) {
			bos.write(bt, 0, i);
		}
		return bos.toString();
	}

	/**
	 * 文件转换成输入流
	 * 
	 * @param file
	 * @return
	 */
	public static InputStream file2InputStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 输入流写入文件
	 * 
	 * @param is
	 *            输入流
	 * @param filePath
	 *            文件保存目录路径
	 * @throws IOException
	 */
	public static void write2File(InputStream is, String filePath)
			throws IOException {
		OutputStream os = new FileOutputStream(filePath);
		int len = 8192;
		byte[] buffer = new byte[len];
		while ((len = is.read(buffer, 0, len)) != -1) {
			os.write(buffer, 0, len);
		}
		os.close();
		is.close();
	}
	
	/**
	 * 输入流写入文件
	 * 
	 * @param is
	 *            输入流
	 * @param filePath
	 *            文件保存目录路径
	 * @param append  是否追加
	 * @throws IOException
	 */
	public static void write2File(InputStream is, String filePath,boolean append)
			throws IOException {
		OutputStream os = new FileOutputStream(filePath,append);
		int len = 8192;
		byte[] buffer = new byte[len];
		while ((len = is.read(buffer, 0, len)) != -1) {
			os.write(buffer, 0, len);
		}
		os.close();
		is.close();
	}

	/**
	 * 获取文件的完整路径
	 * 
	 * @param fileName
	 *            文件名称
	 * @param filePath
	 *            文件保存路径
	 * @return
	 */
	public static String getFullFilePath(String fileName, String filePath) {
		if ((filePath == null || filePath.equals(""))
				|| (fileName == null || fileName.equals(""))) {
			return null;
		}
		filePath = wrapFilePath(filePath);
		return filePath + fileName;
	}

	/**
	 * 转换文件路径中的\\为/
	 * 
	 * @param filePath
	 * @return
	 */
	public static String wrapFilePath(String filePath) {
		if (filePath.split("\\\\").length > 1) {
			filePath = filePath.replace("\\", "/");
		}
		if (!filePath.endsWith("/")) {
			filePath = filePath + "/";
		}
		return filePath;
	}

	/**
	 * 从文件路径中获取文件所在路径
	 * 
	 * @param fullPath
	 *            文件全路径
	 * @return 文件所在路径
	 */
	public static String getFileDir(String fullPath) {
		int iPos1 = fullPath.lastIndexOf("/");
		int iPos2 = fullPath.lastIndexOf("\\");
		if (-1 == iPos1 && -1 == iPos2) {
			return fullPath;
		}
		iPos1 = (iPos1 > iPos2 ? iPos1 : iPos2);
		return fullPath.substring(0, iPos1 + 1);
	}

	/**
	 * 从文件路径中获取文件名称(包含后缀名)
	 * 
	 * @param fullPath
	 * @return
	 */
	public static String getFileName(String fullPath) {
		if (fullPath == null || fullPath.equals("")) {
			return "";
		}
		int iPos1 = fullPath.lastIndexOf("/");
		int iPos2 = fullPath.lastIndexOf("\\");
		if (-1 == iPos1 && -1 == iPos2) {
			return fullPath;
		}
		iPos1 = (iPos1 > iPos2 ? iPos1 : iPos2);
		return fullPath.substring(iPos1 + 1);
	}
	
	/**
	 * 获得指定文件的byte数组 
	 * @param filePath 文件绝对路径
	 * @return
	 */
	public static byte[] file2Byte(String filePath){
		ByteArrayOutputStream bos=null;
		BufferedInputStream in=null;
		try{
			File file=new File(filePath);
			if(!file.exists()){  
	            throw new FileNotFoundException("file not exists");  
	        }
			bos=new ByteArrayOutputStream((int)file.length());
			in=new BufferedInputStream(new FileInputStream(file));
			int buf_size=1024;
			byte[] buffer=new byte[buf_size];
			int len=0;
			while(-1 != (len=in.read(buffer,0,buf_size))){
				bos.write(buffer,0,len);
			}
			return bos.toByteArray();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
		}
		finally{
			try{
				if(in!=null){
					in.close();
				}
				if(bos!=null){
					bos.close();
				}
            }
			catch(Exception e){
				System.out.println(e.getMessage());
				e.printStackTrace();  
            }
        }
    }
    /**
     * 根据byte数组，生成文件 
     * @param bfile 文件数组
     * @param filePath 文件存放路径
     * @param fileName 文件名称
     */
	public static void byte2File(byte[] bfile,String filePath,String fileName){
		BufferedOutputStream bos=null;
		FileOutputStream fos=null;
		File file=null;
		try{
			File dir=new File(filePath);
			if(!dir.exists() && !dir.isDirectory()){//判断文件目录是否存在  
				dir.mkdirs();  
            }
			file=new File(filePath+fileName);
			fos=new FileOutputStream(file);
			bos=new BufferedOutputStream(fos);
			bos.write(bfile);
		} 
		catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();  
        }
		finally{
			try{
				if(bos != null){
					bos.close(); 
				}
				if(fos != null){
					fos.close();
				}
			}
			catch(Exception e){
				System.out.println(e.getMessage());
				e.printStackTrace();  
			}
		}
    }	
	
	
	/**
	 * 从URL链接中提取文件名称
	 * @param url
	 * @return
	 */
	public static String getFileNameFromUrl(String url) {
		if (url == null || url.equals("")) {
			return "";
		}
		if(url.endsWith("/")) {
    		url = url.substring(0,url.length() - 1);
    	}
    	url = url.replaceAll("(?:http|https)://www\\.([\\s\\S]*)", "$1");
    	return getFileName(url);
	}

	/**
	 * 从文件路径中获取文件名称(去除后缀名)
	 * 
	 * @param fullPath
	 * @return
	 */
	public static String getPureFileName(String fullPath) {
		String fileFullName = getFileName(fullPath);
		int index = fileFullName.lastIndexOf(".");
		if (index != -1) {
			return fileFullName.substring(0, index);
		}
		return fileFullName;
	}

	/**
	 * 获得文件名中的后缀名
	 * 
	 * @param fileName
	 *            源文件名
	 * @return String 后缀名
	 */
	public static String getFileSuffix(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index != -1) {
			return fileName.substring(index + 1, fileName.length());
		}
		return fileName;
	}
	/**
	 * 
	 * @Description: 根据扩展名获取mime类型
	 * @param docType 文件扩展名
	 * @return mine类型字符串
	 */
	public static String getMimeType(String docType){
		  docType = docType.toLowerCase();
		  String mime = "";
		  HashMap<String,String> h = new HashMap<String,String>();
		   
		  h.put("","application/octet-stream");
		  h.put("txt", "text/plain");
		  h.put("rar", "application/rar");
		  h.put("html", "text/html");
		  h.put("apk", "application/vnd.android.package-archive");
		  h.put("323","text/h323");
		  h.put("acx","application/internet-property-stream");
		  h.put("ai","application/postscript");
		  h.put("aif","audio/x-aiff");
		  h.put("aifc","audio/x-aiff");
		  h.put("aiff","audio/x-aiff");
		  h.put("asf","video/x-ms-asf");
		  h.put("asr","video/x-ms-asf");
		  h.put("asx","video/x-ms-asf");
		  h.put("au","audio/basic");
		  h.put("avi","video/x-msvideo");
		  h.put("axs","application/olescript");
		  h.put("bas","text/plain");
		  h.put("bcpio","application/x-bcpio");
		  h.put("bin","application/octet-stream");
		  h.put("bmp","image/bmp");
		  h.put("c","text/plain");
		  h.put("cat","application/vnd.ms-pkiseccat");
		  h.put("cdf","application/x-cdf");
		  h.put("cer","application/x-x509-ca-cert");
		  h.put("class","application/octet-stream");
		  h.put("clp","application/x-msclip");
		  h.put("cmx","image/x-cmx");
		  h.put("cod","image/cis-cod");
		  h.put("cpio","application/x-cpio");
		  h.put("crd","application/x-mscardfile");
		  h.put("crl","application/pkix-crl");
		  h.put("crt","application/x-x509-ca-cert");
		  h.put("csh","application/x-csh");
		  h.put("css","text/css");
		  h.put("dcr","application/x-director");
		  h.put("der","application/x-x509-ca-cert");
		  h.put("dir","application/x-director");
		  h.put("dll","application/x-msdownload");
		  h.put("dms","application/octet-stream");
		  h.put("doc","application/msword");
		  h.put("dot","application/msword");
		  h.put("dvi","application/x-dvi");
		  h.put("dxr","application/x-director");
		  h.put("eps","application/postscript");
		  h.put("etx","text/x-setext");
		  h.put("evy","application/envoy");
		  h.put("exe","application/octet-stream");
		  h.put("fif","application/fractals");
		  h.put("flr","x-world/x-vrml");
		  h.put("gif","image/gif");
		  h.put("gtar","application/x-gtar");
		  h.put("gz","application/x-gzip");
		  h.put("h","text/plain");
		  h.put("hdf","applicatin/x-hdf");
		  h.put("hlp","application/winhlp");
		  h.put("hqx","application/mac-binhex40");
		  h.put("hta","application/hta");
		  h.put("htc","text/x-component");
		  h.put("htm","text/html");
		  h.put("html","text/html");
		  h.put("htt","text/webviewhtml");
		  h.put("ico","image/x-icon");
		  h.put("ief","image/ief");
		  h.put("iii","application/x-iphone");
		  h.put("ins","application/x-internet-signup");
		  h.put("isp","application/x-internet-signup");
		  h.put("jfif","image/pipeg");
		  h.put("jpe","image/jpeg");
		  h.put("jpeg","image/jpeg");
		  h.put("jpg","image/jpeg");
		  h.put("png","image/png");
		  h.put("js","application/x-javascript");
		  h.put("latex","application/x-latex");
		  h.put("lha","application/octet-stream");
		  h.put("lsf","video/x-la-asf");
		  h.put("lsx","video/x-la-asf");
		  h.put("lzh","application/octet-stream");
		  h.put("m13","application/x-msmediaview");
		  h.put("m14","application/x-msmediaview");
		  h.put("m3u","audio/x-mpegurl");
		  h.put("man","application/x-troff-man");
		  h.put("mdb","application/x-msaccess");
		  h.put("me","application/x-troff-me");
		  h.put("mht","message/rfc822");
		  h.put("mhtml","message/rfc822");
		  h.put("mid","audio/mid");
		  h.put("mny","application/x-msmoney");
		  h.put("mov","video/quicktime");
		  h.put("movie","video/x-sgi-movie");
		  h.put("mp2","video/mpeg");
		  h.put("mp3","audio/mpeg");
		  h.put("mpa","video/mpeg");
		  h.put("mpe","video/mpeg");
		  h.put("mpeg","video/mpeg");
		  h.put("mpg","video/mpeg");
		  h.put("mpp","application/vnd.ms-project");
		  h.put("mpv2","video/mpeg");
		  h.put("ms","application/x-troff-ms");
		  h.put("mvb","application/x-msmediaview");
		  h.put("nws","message/rfc822");
		  h.put("oda","application/oda");
		  h.put("p10","application/pkcs10");
		  h.put("p12","application/x-pkcs12");
		  h.put("p7b","application/x-pkcs7-certificates");
		  h.put("p7c","application/x-pkcs7-mime");
		  h.put("p7m","application/x-pkcs7-mime");
		  h.put("p7r","application/x-pkcs7-certreqresp");
		  h.put("p7s","application/x-pkcs7-signature");
		  h.put("pbm","image/x-portable-bitmap");
		  h.put("pdf","application/pdf");
		  h.put("pfx","application/x-pkcs12");
		  h.put("pgm","image/x-portable-graymap");
		  h.put("pko","application/ynd.ms-pkipko");
		  h.put("pma","application/x-perfmon");
		  h.put("pmc","application/x-perfmon");
		  h.put("pml","application/x-perfmon");
		  h.put("pmr","application/x-perfmon");
		  h.put("pmw","application/x-perfmon");
		  h.put("pnm","image/x-portable-anymap");
		  h.put("pot,","application/vnd.ms-powerpoint");
		  h.put("ppm","image/x-portable-pixmap");
		  h.put("pps","application/vnd.ms-powerpoint");
		  h.put("ppt","application/vnd.ms-powerpoint");
		  h.put("prf","application/pics-rules");
		  h.put("ps","application/postscript");
		  h.put("pub","application/x-mspublisher");
		  h.put("qt","video/quicktime");
		  h.put("ra","audio/x-pn-realaudio");
		  h.put("ram","audio/x-pn-realaudio");
		  h.put("ras","image/x-cmu-raster");
		  h.put("rgb","image/x-rgb");
		  h.put("rmi","audio/mid");
		  h.put("roff","application/x-troff");
		  h.put("rtf","application/rtf");
		  h.put("rtx","text/richtext");
		  h.put("scd","application/x-msschedule");
		  h.put("sct","text/scriptlet");
		  h.put("setpay","application/set-payment-initiation");
		  h.put("setreg","application/set-registration-initiation");
		  h.put("sh","application/x-sh");
		  h.put("shar","application/x-shar");
		  h.put("sit","application/x-stuffit");
		  h.put("snd","audio/basic");
		  h.put("spc","application/x-pkcs7-certificates");
		  h.put("spl","application/futuresplash");
		  h.put("src","application/x-wais-source");
		  h.put("sst","application/vnd.ms-pkicertstore");
		  h.put("stl","application/vnd.ms-pkistl");
		  h.put("stm","text/html");
		  h.put("svg","image/svg+xml");
		  h.put("sv4cpio","application/x-sv4cpio");
		  h.put("sv4crc","application/x-sv4crc");
		  h.put("swf","application/x-shockwave-flash");
		  h.put("t","application/x-troff");
		  h.put("tar","application/x-tar");
		  h.put("tcl","application/x-tcl");
		  h.put("tex","application/x-tex");
		  h.put("texi","application/x-texinfo");
		  h.put("texinfo","application/x-texinfo");
		  h.put("tgz","application/x-compressed");
		  h.put("tif","image/tiff");
		  h.put("tiff","image/tiff");
		  h.put("tr","application/x-troff");
		  h.put("trm","application/x-msterminal");
		  h.put("tsv","text/tab-separated-values");
		  h.put("txt","text/plain");
		  h.put("uls","text/iuls");
		  h.put("ustar","application/x-ustar");
		  h.put("vcf","text/x-vcard");
		  h.put("vrml","x-world/x-vrml");
		  h.put("wav","audio/x-wav");
		  h.put("wcm","application/vnd.ms-works");
		  h.put("wdb","application/vnd.ms-works");
		  h.put("wks","application/vnd.ms-works");
		  h.put("wmf","application/x-msmetafile");
		  h.put("wps","application/vnd.ms-works");
		  h.put("wri","application/x-mswrite");
		  h.put("wrl","x-world/x-vrml");
		  h.put("wrz","x-world/x-vrml");
		  h.put("xaf","x-world/x-vrml");
		  h.put("xbm","image/x-xbitmap");
		  h.put("xla","application/vnd.ms-excel");
		  h.put("xlc","application/vnd.ms-excel");
		  h.put("xlm","application/vnd.ms-excel");
		  h.put("xls","application/vnd.ms-excel");
		  h.put("xlt","application/vnd.ms-excel");
		  h.put("xlw","application/vnd.ms-excel");
		  h.put("xof","x-world/x-vrml");
		  h.put("xpm","image/x-xpixmap");
		  h.put("xwd","image/x-xwindowdump");
		  h.put("z","application/x-compress");
		  h.put("zip","application/zip");
		  mime = h.get(docType);
		  if(mime==null){
		   mime = "application/octet-stream";
		  }
		  return mime;
	}
	


}
