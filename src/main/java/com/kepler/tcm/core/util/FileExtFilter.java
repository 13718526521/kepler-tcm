package com.kepler.tcm.core.util;

import java.io.File;
import java.io.FilenameFilter;

public class FileExtFilter implements FilenameFilter {
	private String ext = "";

	public FileExtFilter(String ext) {
		this.ext = ext;
	}

	public boolean accept(File dir, String name) {
		if ((!new File(dir.getPath() + "/" + name).isDirectory()) && (name.toLowerCase().endsWith(this.ext))) {
			return true;
		}

		return false;
	}
}