package com.kepler.tcm.server.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;

public class ResourceLoader {
	public static Class loadClass(String name) throws ClassNotFoundException {
		ClassLoader loader = ClassLoaderResolver.getClassLoader(1);

		return Class.forName(name, false, loader);
	}

	public static URL getResource(String name) {
		ClassLoader loader = ClassLoaderResolver.getClassLoader(1);

		if (loader != null) {
			return loader.getResource(name);
		}
		return ClassLoader.getSystemResource(name);
	}

	public static String getResourcePath(String name) {
		try {
			return URLDecoder.decode(getResource(name).getPath(), "UTF-8");
		} catch (Exception e) {
		}
		return null;
	}

	public static InputStream getResourceAsStream(String name) {
		ClassLoader loader = ClassLoaderResolver.getClassLoader(1);

		if (loader != null) {
			return loader.getResourceAsStream(name);
		}
		return ClassLoader.getSystemResourceAsStream(name);
	}

	public static Enumeration getResources(String name) throws IOException {
		ClassLoader loader = ClassLoaderResolver.getClassLoader(1);

		if (loader != null) {
			return loader.getResources(name);
		}
		return ClassLoader.getSystemResources(name);
	}
}
