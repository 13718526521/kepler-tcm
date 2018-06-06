package com.kepler.tcm.server.core.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class FileTools {
	public static final int CLASS_HEADER_MARK = -889275714;

	public static String extractFileExt(String _sFilePathName) {
		int nPos = _sFilePathName.lastIndexOf('.');
		return nPos < 0 ? "" : _sFilePathName.substring(nPos);
	}

	public static String deleteFileExt(String f) {
		int nPos = f.lastIndexOf('.');
		return nPos < 0 ? f : f.substring(0, nPos);
	}

	public static String changeFileExt(String f, String newExt) {
		return deleteFileExt(f) + newExt;
	}

	public static void appendFileText(String _sFileName, String _sAddContent) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(_sFileName, "rw");
		raf.seek(raf.length());
		raf.writeBytes(_sAddContent);
		raf.close();
	}

	public static void setFileText(String _sFileName, String _sAddContent) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(_sFileName, "rw");
		raf.setLength(_sAddContent.length());
		raf.writeBytes(_sAddContent);
		raf.close();
	}

	public static void setFileText(String _sFileName, String _sAddContent, String charset) throws IOException {
		setFileText(new File(_sFileName), _sAddContent, charset);
	}

	public static void setFileText(File _sFileName, String _sAddContent, String charset) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(_sFileName);

			if ((charset == null) || (charset.length() == 0))
				fos.write(_sAddContent.getBytes());
			else {
				fos.write(_sAddContent.getBytes(charset));
			}
		} finally {
			if (fos != null)
				fos.close();
		}
	}

	public static String getFileText(String fileName) throws IOException {
		return getFileText(fileName, null);
	}

	public static String getFileText(String fileName, String charsetName) throws IOException {
		return getFileText(new File(fileName), charsetName);
	}

	public static String getFileText(File f, String charsetName) throws IOException {
		if (!f.exists())
			return "";
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

	public static boolean fileExists(String _sPathFileName) {
		File file = new File(_sPathFileName);
		return file.exists();
	}

	public static boolean pathExists(String _sPathFileName) {
		String sPath = extractFilePath(_sPathFileName);
		return fileExists(sPath);
	}

	public static String extractFileName(String _sFilePathName) {
		int nPos = _sFilePathName.lastIndexOf('/');
		int nPos2 = _sFilePathName.lastIndexOf('\\');
		if (nPos2 > nPos)
			nPos = nPos2;
		return _sFilePathName.substring(nPos + 1);
	}

	public static String extractFilePath(String _sFilePathName) {
		int nPos = _sFilePathName.lastIndexOf('/');
		int nPos2 = _sFilePathName.lastIndexOf('\\');
		if (nPos2 > nPos)
			nPos = nPos2;
		return _sFilePathName.substring(0, nPos);
	}

	public static boolean deleteDir(String _sDir) {
		return deleteDir(_sDir, false);
	}

	public static boolean deleteDir(String _sDir, boolean _bDeleteChildren) {
		File file = new File(_sDir);
		if (!file.exists())
			return false;
		if (_bDeleteChildren) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory())
					deleteDir(files[i].getAbsolutePath(), _bDeleteChildren);
				else
					files[i].delete();
			}
		}
		return file.delete();
	}

	public static boolean copyFile(File file1, File file2) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			byte[] bytes = new byte[1024];
			int nOff = 0;
			try {
				fis = new FileInputStream(file1);
				try {
					file2.getParentFile().mkdirs();
					fos = new FileOutputStream(file2);
					while ((nOff = fis.read(bytes)) != -1) {
						fos.write(bytes, 0, nOff);
					}
				} finally {
					if (fos != null)
						fos.close();
				}

			} finally {
				if (fis != null)
					fis.close();
			}
			return true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	public static String extractClassName(String filename) {
		try {
			return extractClassName(new FileInputStream(filename));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	public static String extractClassName(InputStream is) {
		String name = null;
		DataInputStream in = null;
		try {
			in = new DataInputStream(is);
			if (in.readInt() != -889275714) {
				return null;
			}

			in.readUnsignedShort();
			in.readUnsignedShort();
			in.readUnsignedShort();
			in.readByte();
			in.readUnsignedShort();
			in.readByte();
			name = in.readUTF();

			in.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();

			if (in != null)
				try {
					in.close();
				} catch (Exception localException1) {
				}
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception localException2) {
				}
		}
		return name;
	}

	public static String addSeparator(String s) {
		if ((!s.endsWith("\\")) && (!s.endsWith("/")))
			s = s + File.separator;
		return s;
	}

	public static String contactPath(String path1, String path2) {
		if (path1 == null)
			return path2;
		if (path2 == null)
			return path1;

		if ((path1.endsWith("\\")) || (path1.endsWith("/"))) {
			if ((path2.startsWith("\\")) || (path2.startsWith("/")))
				path2 = path2.substring(1);
			return path1 + path2;
		}

		if ((path2.length() == 0) || (path2.startsWith("\\")) || (path2.startsWith("/")))
			return path1 + path2;
		return path1 + File.separator + path2;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getFileText("c:/plugin.properties", "UTF-8"));

		System.out.println(extractClassName("d:/PluginManager.class"));
	}
}
