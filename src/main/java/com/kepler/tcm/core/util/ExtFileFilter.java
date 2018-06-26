package com.kepler.tcm.core.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 文件过滤器
 * 
 * @author wangsp
 * @date 2018年6月6日
 * @version V1.0
 */
public class ExtFileFilter implements FilenameFilter {

	private String strExtendName = "";

	public ExtFileFilter(String extendName) {
		this.strExtendName = extendName;
	}

	public boolean accept(File dir, String name) {
		if ((new File(dir.getPath() + "/" + name).isDirectory())
				|| (name.toLowerCase().endsWith(this.strExtendName.toLowerCase()))) {
			return true;
		}

		return false;
	}
}
