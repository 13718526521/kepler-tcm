package com.kepler.tcm.server.tcm.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class Ansi2Utf8 {
	public static void main(String[] args) throws Exception {
		System.out.print("程序运行路径：");
		System.out.println(new File("./").getCanonicalPath());
		System.out.println("把本地编码的配置文件转成UTF-8编码的文件");
		System.out.println("此程序只能运行一次，不能多次运行，否则会造成乱码！");
		System.out.print("要继续进行吗[y/n]?");
		int c = System.in.read();
		if ((c != 121) && (c != 89))
			return;

		set("plugins", "plugin.properties");
		set("./working", "task.properties");
		set("./working", "config.properties");
	}

	public static void setFileText(File _sFileName, String _sAddContent, String charset) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(_sFileName);

			if ((charset == null) || (charset.length() == 0))
				fos.write(_sAddContent.getBytes());
			else
				fos.write(_sAddContent.getBytes(charset));

		} finally {
			if (fos != null)
				fos.close();
		}
	}

	public static String getFileText(File f, String charsetName) throws IOException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(f);
			byte[] bs = new byte[in.available()];
			in.read(bs);
			String str;
			if ((charsetName == null) || (charsetName.length() == 0))
				return new String(bs);
			/* byte[] bs; */
			return new String(bs, charsetName);
		} finally {
			if (in != null)
				in.close();
		}
	}

	private static void set(String path, String name) throws Exception {
		File f = new File(path + "/");
		System.out.println(f.getCanonicalPath());
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++)
				if (files[i].isDirectory()) {
					File pf = new File(path + "/" + files[i].getName() + "/" + name);
					System.out.println(pf.getCanonicalPath());
					try {
						setFileText(pf, getFileText(pf, ""), "UTF-8");
					} catch (Exception e) {
						System.out.println(e);
					}
				}
		}
	}
}