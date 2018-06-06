package com.kepler.tcm.server.core.util;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		File f1 = (File) o1;
		File f2 = (File) o2;

		long n = f1.lastModified() - f2.lastModified();
		if (n > 0L)
			return 1;
		if (n < 0L)
			return -1;
		return 0;
	}
}
