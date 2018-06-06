package com.kepler.tcm.server.tcm.server;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import com.kepler.tcm.server.loader.ResourceLoader;

public class ClassReloader {
	public ClassReloader(String className) {
		URL url = ResourceLoader.getResource(className);

		System.out.println(url.toExternalForm());
	}

	public static void main(String[] args) throws Exception {
		ClassReloader r = new ClassReloader("org/apache/log4j/Appender.class");

		URL l = ResourceLoader.getResource("org/apache/log4j/Appender.class");

		URLClassLoader cl = new URLClassLoader(
				new URL[] { new File("D:\\workspace\\web_tcm_server\\common\\libs\\trs_tool_zk401.jar").toURL() });

		Class c = cl.loadClass("com.trs.tool.StringTool");
		if (c == null)
			System.out.println("c is null");
		Object o = c.newInstance();
		System.out.println(o.getClass().getName());
	}
}