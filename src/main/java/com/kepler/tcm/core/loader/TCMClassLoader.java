package com.kepler.tcm.core.loader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.kepler.tcm.core.startup.Bootstrap;
import com.kepler.tcm.core.util.ExtFileFilter;

/**
 * TCM自定义类加载器
 * 
 * @author wangsp
 * @date 2018年6月6日
 * @version V1.0
 */
public class TCMClassLoader extends ClassLoader {

	private String[][] paths = new String[0][0];

	private File[] jarFiles = null;

	public TCMClassLoader() {
	}

	public TCMClassLoader(ClassLoader parent) {
		super(parent);
	}

	public void setPath(String[][] p) {
		this.paths = p;
	}

	public Class findClass(String name) throws ClassNotFoundException {
		Class c = null;

		for (int i = 0; i < this.paths.length; i++) {
			if (this.paths[i][1].equals("classes")) {
				c = findInClassesPath(new File(this.paths[i][0]), name);
				if (c != null)
					break;
			} else if (this.paths[i][1].equals("lib")) {
				c = findInLibPath(new File(this.paths[i][0]), name);
				if (c != null) {
					break;
				}

			}

		}

		if (c == null)
			throw new ClassNotFoundException(name);

		return c;
	}

	private Class findInClassesPath(File f, String name) {
		Class c = null;

		if (f.isDirectory()) {
			String fileStub = name.replace('.', File.separatorChar);
			String classFilename = fileStub + ".class";
			try {
				File classFile = new File(f.getCanonicalPath() + File.separatorChar + classFilename);

				if (classFile.isFile())
					c = tryToLoadClass(classFile, name);
			} catch (Exception e) {
				System.out.println("findInClassesPath:" + e);
			}
		}

		return c;
	}

	private Class findInLibPath(File f, String name) {
		Class c = null;
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					c = findInLibPath(files[i], name);
					if (c != null)
						break;
				} else {
					c = findInJarFile(files[i], name);
					if (c != null)
						break;
				}
			}
		} else {
			c = findInJarFile(f, name);
		}

		return c;
	}

	private Class findInJarFile(File f, String name) {
		Class c = null;
		String lname = f.getName().toLowerCase();
		if ((lname.endsWith(".jar")) || (lname.endsWith(".zip")))
			try {
				JarInputStream jar = new JarInputStream(new FileInputStream(f));

				String fileStub = name.replace('.', '/');
				String classFilename = fileStub + ".class";
				JarEntry entry;
				while ((entry = jar.getNextJarEntry()) != null) {
					if (entry.getName().equals(classFilename)) {
						int nOff = 0;
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						BufferedInputStream bis = new BufferedInputStream(jar);
						convert(bis, baos);
						jar.closeEntry();
						c = defineClass(name, baos.toByteArray(), 0, baos.size());
						baos.close();
						if (Bootstrap.bStarted)
							break;
						System.out.print(".");

						break;
					}
				}
				jar.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		return c;
	}

	private Class tryToLoadClass(File f, String name) {
		Class c = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			FileInputStream is = new FileInputStream(f);
			convert(is, baos);
			is.close();
			c = defineClass(name, baos.toByteArray(), 0, baos.size());
			baos.close();
			if (c == null)
				System.out.println("define " + name + " is null");

		} catch (Exception e) {
			System.out.println(e);
		}
		return c;
	}

	protected URL findResource(String name) {
		URL c = null;

		for (int i = 0; i < this.paths.length; i++) {
			if (this.paths[i][1].equals("classes")) {
				c = findURLInClassesPath(new File(this.paths[i][0]), name);
				if (c != null)
					break;
			} else if (this.paths[i][1].equals("lib")) {
				c = findURLInLibPath(new File(this.paths[i][0]), name);
				if (c != null) {
					break;
				}
			}
		}
		return c;
	}

	private URL findURLInClassesPath(File f, String name) {
		URL c = null;

		if (f.isDirectory()) {
			String resourceName = name.replace('\\', '/');
			try {
				File resourceFile = new File(contactPath(f.getCanonicalPath(), resourceName));

				if (resourceFile.isFile()) {
					c = resourceFile.toURI().toURL();
				}
			} catch (Exception e) {
				System.out.println("findURLInClassesPath:" + e);
			}
		}

		return c;
	}

	private URL findURLInLibPath(File f, String name) {
		URL c = null;
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					c = findURLInLibPath(files[i], name);
					if (c != null)
						break;
				} else {
					c = findURLInJarFile(files[i], name);
					if (c != null)
						break;
				}
			}
		} else {
			c = findURLInJarFile(f, name);
		}

		return c;
	}

	private URL findURLInJarFile(File f, String name) {
		URL c = null;
		String lname = f.getName().toLowerCase();
		if ((lname.endsWith(".jar")) || (lname.endsWith(".zip")))
			try {
				JarInputStream jar = new JarInputStream(new FileInputStream(f));

				String resourceName = name.replace('\\', '/');
				JarEntry entry;
				while ((entry = jar.getNextJarEntry()) != null) {
					if (entry.getName().equals(resourceName)) {
						c = new URL(contact("jar:file:" + f.getCanonicalPath() + "!", '/', resourceName));
					}

				}

				jar.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		return c;
	}

	public ArrayList getJarList(File file, ExtFileFilter filter) {
		ArrayList fileList = new ArrayList();

		if (file.isDirectory()) {
			File[] files = file.listFiles(filter);
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					fileList.addAll(getJarList(files[i], filter));
				} else {
					fileList.add(files[i]);
				}
			}
		} else {
			fileList.add(file);
		}

		fileList.trimToSize();
		return fileList;
	}

	public static String contact(String s1, char separator, String s2) {
		if ((s1.length() > 0) && (s1.charAt(s1.length() - 1) == separator))
			s1 = s1.substring(0, s1.length() - 1);
		if ((s2.length() > 0) && (s2.charAt(0) == separator))
			s2 = s2.substring(1);
		return s1 + separator + s2;
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

	public OutputStream convert(InputStream is, OutputStream os) throws IOException {
		byte[] bs = new byte[512];
		int length = -1;

		while ((length = is.read(bs)) != -1) {
			os.write(bs, 0, length);
		}

		return os;
	}

	public static void main(String[] args) throws Exception {
		TCMClassLoader loader = new TCMClassLoader();
		loader.setPath(new String[][] { { "c:/a", "classes" }, { "c:/a/", "lib" } });
		URL u = loader.getResource("com/people/xinhua/plugin/config.properties");
		byte[] bs = new byte[100];
		u.openStream().read(bs);
		System.out.println(new String(bs));

		/*
		 * URL u = loader.getResource("cpnn/downloadversion.ini"); byte[] bs = new
		 * byte[100]; u.openStream().read(bs); System.out.println(new String(bs));
		 */
	}
}
