package com.kepler.tcm.core.agent;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.kepler.tcm.core.task.Task;
import com.kepler.tcm.core.util.FileTools;

public class ClassLoaderByClass extends ClassLoader {
	Class base = null;
	byte[] classBytes;
	boolean isJar = false;

	public ClassLoaderByClass(String filename, Class base) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			this.isJar = filename.endsWith(".jar");
			is = new FileInputStream(filename);
			byte[] bs = new byte[512];
			int length = -1;

			while ((length = is.read(bs)) != -1) {
				bos.write(bs, 0, length);
			}
			this.classBytes = bos.toByteArray();
		} finally {
			if (is != null)
				is.close();
			bos.close();
		}
		this.base = base;
	}

	public ClassLoaderByClass(byte[] classBytes, boolean isJar, Class base) {
		this.classBytes = classBytes;

		this.base = base;
		this.isJar = isJar;
	}

	private boolean classIn(Class c, Class[] cc) {
		if (cc == null)
			return false;
		for (int i = 0; i < cc.length; i++)
			if (c == cc[i])
				return true;
		return false;
	}

	public boolean classOf(Class c, Class base) {
		if ((c == null) || (base == null))
			return false;
		if (c == base)
			return true;
		Class[] cc = c.getInterfaces();
		if (classIn(base, cc))
			return true;
		if (classOf(c.getSuperclass(), base))
			return true;
		return false;
	}

	public Class loadClass() throws ClassNotFoundException {
		return loadClass("@#$%ClassFindClassLoader");
	}

	protected Class findClass(String name) throws ClassNotFoundException {
		Class c = null;
		byte[] bytes = new byte[1024];
		JarInputStream jar = null;
		try {
			Class localClass1;
			if (!this.isJar) {
				String className = FileTools
						.extractClassName(new ByteArrayInputStream(
								this.classBytes));
				c = defineClass(className.replace('/', '.'), this.classBytes,
						0, this.classBytes.length);
				if (classOf(c, this.base))
					return c;

			} else {
				jar = new JarInputStream(new ByteArrayInputStream(
						this.classBytes));
				JarEntry entry;
				while ((entry = jar.getNextJarEntry()) != null) {
					/* JarEntry entry; */
					String ename = entry.getName();
					if (!ename.endsWith(".class")) {
						jar.closeEntry();
					} else {
						ename = ename.substring(0, ename.length() - 6).replace(
								'/', '.');

						int nOff = 0;
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						BufferedInputStream bis = new BufferedInputStream(jar);
						while ((nOff = bis.read(bytes, 0, bytes.length)) != -1) {
							baos.write(bytes, 0, nOff);
						}
						jar.closeEntry();
						c = defineClass(ename, baos.toByteArray(), 0,
								baos.size());
						baos.close();

						if (classOf(c, this.base))
							return c;
					}
				}
			}

		} catch (Exception e) {
			throw new ClassNotFoundException("", e);
		} finally {
			if (jar != null)
				try {
					jar.close();
				} catch (Exception localException3) {
				}
		}
		if (jar != null) {
			try {
				jar.close();
			} catch (Exception localException4) {
			}
		}

		if (c == null)
			throw new ClassNotFoundException(this.base.getName());
		return c;
	}

	public static void main(String[] args) throws Exception {
		ClassLoaderByClass cl = new ClassLoaderByClass(
				"D:\\workspaces\\workspace_dbg\\web_tcm_plugin\\classes\\tcm\\plugin\\demo1\\Demo.class",
				Task.class);
		Class c = cl.loadClass();
		Object o = c.newInstance();
		System.out.println(o.getClass().getName());

		System.out.println(c.getMethod("getVersion", null).invoke(null, null));

		ClassLoaderByClass cl1 = new ClassLoaderByClass("c:/people_xinhua.jar",
				Task.class);
		Class c1 = cl1.loadClass();
		Object o1 = c1.newInstance();
		System.out.println(o1.getClass().getName());

		System.out.println(c1.getMethod("getVersion", null).invoke(null, null));
	}
}