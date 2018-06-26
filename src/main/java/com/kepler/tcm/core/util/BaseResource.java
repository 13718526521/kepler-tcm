package com.kepler.tcm.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Properties;

import com.kepler.tcm.core.loader.ResourceLoader;

public abstract class BaseResource {

	private String fileName = null;

	private String realFileName = null;

	private long lastFileTime = 0L;

	private Properties p;

	protected String charset = null;

	private HashMap map = null;

	private static HashMap resourceMap = null;

	private static synchronized BaseResource getResourceSync(String className,
			String fileName, String charsetName) throws Exception {
		BaseResource obj = (BaseResource) resourceMap.get(fileName);
		if ((obj == null) || (obj.changed())) {
			Class cls = Class.forName(className);

			obj = (BaseResource) cls.newInstance();
			obj.loadResource(fileName, charsetName);
			resourceMap.put(fileName, obj);
		}

		return obj;
	}

	public boolean changed() throws IOException {
		if ((this.realFileName == null) || (this.realFileName.length() == 0))
			return true;
		File file = new File(this.realFileName);
		if (!file.exists())
			return true;
		return this.lastFileTime != file.lastModified();
	}

	protected static BaseResource getResource(String className,
			String fileName, String charsetName) throws Exception {
		if (resourceMap == null)
			resourceMap = new HashMap();

		BaseResource obj = (BaseResource) resourceMap.get(fileName);

		if ((obj == null) || (obj.changed())) {
			return getResourceSync(className, fileName, charsetName);
		}
		return obj;
	}

	protected abstract void handleResrouce(
			InputStreamReader paramInputStreamReader) throws Exception;

	protected void loadResource(String fileName, String charsetName)
			throws Exception {
		InputStream is = null;
		InputStreamReader ir = null;
		if (new File(fileName).exists()) {
			this.realFileName = fileName;
			is = new FileInputStream(fileName);

		} else {
			this.realFileName = null;
			URL url = ResourceLoader.getResource(fileName);
			if (url != null) {
				this.realFileName = URLDecoder.decode(url.getPath(), "UTF-8");
				is = url.openStream();
			}
		}

		if (is == null) {
			System.out.println("Resource " + fileName
					+ " is not exists. Try to create it.");
			File f = new File(fileName);
			f.getParentFile().mkdirs();
			if (f.createNewFile()) {
				System.out.println("Resource file " + f.getCanonicalPath()
						+ " created");
				this.realFileName = fileName;
				is = new FileInputStream(fileName);
			}

		}

		if (is == null)
			throw new IOException("Resource " + fileName + " is not exists.");

		if ((charsetName == null) || (charsetName.equals(""))) {
			ir = new InputStreamReader(is);
		} else {
			ir = new InputStreamReader(is, charsetName);
		}
		this.charset = charsetName;

		handleResrouce(ir);

		ir.close();
	}

	public String getString(String name) {
		return (String) this.map.get(name);
	}

	public String getString(String name, String defaultValue) {
		String value = (String) this.map.get(name);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}
}
