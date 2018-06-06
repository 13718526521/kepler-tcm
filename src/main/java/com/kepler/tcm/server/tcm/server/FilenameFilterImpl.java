package com.kepler.tcm.server.tcm.server;

import java.io.File;
import java.io.FilenameFilter;

class FilenameFilterImpl implements FilenameFilter {
	public boolean accept(File dir, String name) {
		if ((!new File(dir.getPath() + "/" + name).isDirectory()) && (name.toLowerCase().endsWith(".counter"))) {
			return true;
		}

		return false;
	}
}