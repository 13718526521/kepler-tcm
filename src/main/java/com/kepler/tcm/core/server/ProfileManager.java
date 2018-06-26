package com.kepler.tcm.core.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.kepler.tcm.core.util.FileExtFilter;
import com.kepler.tcm.core.util.FileTools;
import com.kepler.tcm.core.util.StringList;
import com.kepler.tcm.core.util.StringTools;
import com.kepler.tcm.core.util.Util;

public class ProfileManager {
	public static void addCategory(String name, String memo, String paramStruct) throws Exception {
		String path = "./conf/profiles/" + name;
		File f = new File(path);
		if (f.exists())
			throw new Exception("分类目录已经存在");
		f.mkdirs();
		Util.write(path + "/category.memo", false, memo);
		Util.write(path + "/category.param", false, paramStruct);
	}

	public static boolean renameCategory(String oldname, String newname) {
		String path = "./conf/profiles/" + oldname;
		File f = new File(path);

		return f.renameTo(new File("./conf/profiles/" + newname));
	}

	public static void modifyCategory(String name, String memo, String paramStruct) throws Exception {
		String path = "./conf/profiles/" + name;
		File f = new File(path);
		if (!f.exists())
			throw new Exception("分类目录不存在");
		Util.write(path + "/category.memo", false, memo);
		Util.write(path + "/category.param", false, paramStruct);
	}

	public static boolean removeCaegory(String name) {
		FileTools.deleteDir("./conf/profiles/" + name, true);
		return !new File("./conf/profiles/" + name).exists();
	}

	public static String[] getCategoryNames() {
		File[] fs = new File("./conf/profiles").listFiles();

		ArrayList list = new ArrayList();
		if (fs != null) {
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isDirectory()) {
					list.add(fs[i].getName());
				}
			}
		}
		return (String[]) list.toArray(new String[0]);
	}

	public static String[][] getCategories() {
		String[] names = getCategoryNames();
		String[][] result = new String[names.length][2];
		for (int i = 0; i < names.length; i++) {
			result[i][0] = names[i];
			try {
				result[i][i] = FileTools.getFileText("./conf/profiles/" + names[i] + "/category.memo",
						Util.getCharset());
			} catch (Exception e) {
				result[i][i] = "";
			}

		}

		return result;
	}

	public static String[][] getCategoryParamStruct(String category) throws IOException {
		String path = "./conf/profiles/" + category + "/category.param";
		StringList list = new StringList();
		list.loadFromFile(path, Util.getCharset());
		return list.toArray2();
	}

	public static String getCategoryMemo(String category) throws IOException {
		return FileTools.getFileText("./conf/profiles/" + category + "/category.memo", Util.getCharset());
	}

	public static String[][] getCategoryProfiles(String category) {
		String[] names = new File("./conf/profiles/" + category).list(new FileExtFilter(".profile"));
		String[][] result = new String[names.length][2];
		for (int i = 0; i < names.length; i++) {
			result[i][0] = names[i];
			try {
				result[i][1] = FileTools.getFileText("./conf/profiles/" + category + "/" + names[i] + ".desc",
						Util.getCharset());
			} catch (Exception e) {
				result[i][1] = "";
			}
		}
		return result;
	}

	public static void addProfile(String category, String profile, String memo, Map params) throws Exception {
		StringList list = new StringList();
		if (!profile.endsWith(".profile"))
			profile = profile + ".profile";
		String path = "./conf/profiles/" + category + "/" + profile;
		Util.write(path + ".desc", false, memo);
		Iterator it = params.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			e.setValue(StringTools.encodeEnter((String) e.getValue()));
		}
		Util.writePropertyFile(params, path);
	}

	public static void modifyProfile(String category, String profile, String memo, Map params) throws Exception {
		addProfile(category, profile, memo, params);
	}

	public static boolean removeProfile(String category, String profile) {
		if (!profile.endsWith(".profile"))
			profile = profile + ".profile";
		String path = "./conf/profiles/" + category + "/" + profile;
		return new File(path).delete();
	}

	public static boolean renameProfile(String category, String oldProfile, String newProfile) {
		if (!oldProfile.endsWith(".profile"))
			oldProfile = oldProfile + ".profile";
		if (!newProfile.endsWith(".profile"))
			newProfile = newProfile + ".profile";
		String path1 = "./conf/profiles/" + category + "/" + oldProfile;
		String path2 = "./conf/profiles/" + category + "/" + newProfile;

		return (new File(path1).renameTo(new File(path2)))
				&& (new File(path1 + ".desc").renameTo(new File(path2 + ".desc")));
	}

	public static String[][] getProfile(String category, String profile) throws IOException {
		return getProfile(category + "/" + profile);
	}

	public static String getProfileMemo(String category, String profile) throws IOException {
		if (!profile.endsWith(".profile"))
			profile = profile + ".profile";
		return FileTools.getFileText("./conf/profiles/" + category + "/" + profile + ".desc", Util.getCharset());
	}

	public static String[][] getProfile(String path) throws IOException {
		StringList list = new StringList();
		if (!path.endsWith(".profile"))
			path = path + ".profile";
		list.loadFromFile("./conf/profiles/" + path, Util.getCharset());
		String[][] result = new String[list.size()][2];
		for (int i = 0; i < list.size(); i++) {
			String s = StringTools.decodeEnter(list.get(i));
			int n = s.indexOf('=');
			if (n == -1) {
				result[i][0] = s;
			} else {
				result[i][0] = s.substring(0, n).trim();
				result[i][1] = s.substring(n + 1).trim();
			}
		}

		return result;
	}

	public static Map getProfileMap(String category, String profile) throws IOException {
		return getProfileMap(category + "/" + profile);
	}

	public static Map getProfileMap(String path) throws IOException {
		StringList list = new StringList();
		if (!path.endsWith(".profile"))
			path = path + ".profile";
		list.loadFromFile(new File("./conf/profiles/" + path).getAbsolutePath(), Util.getCharset());
		HashMap map = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			String s = StringTools.decodeEnter(list.get(i));
			int n = s.indexOf('=');
			if (n == -1) {
				map.put(s, null);
			} else {
				map.put(s.substring(0, n).trim(), s.substring(n + 1).trim());
			}

		}

		return map;
	}

	public static void main(String[] args) {
		ProfileManager pm = new ProfileManager();
		String[] x = getCategoryNames();
		System.out.println(x.length);
		for (int i = 0; i < x.length; i++) {
			System.out.println(x[i] + " ");
		}
	}
}