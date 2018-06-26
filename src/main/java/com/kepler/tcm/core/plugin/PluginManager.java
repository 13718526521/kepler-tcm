package com.kepler.tcm.core.plugin;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kepler.tcm.core.server.PropertyResourceBundle;
import com.kepler.tcm.core.task.ErrorClassHandleTask;
import com.kepler.tcm.core.task.TaskListener;
import com.kepler.tcm.core.util.FileTools;
import com.kepler.tcm.core.util.Util;
import com.kepler.tcm.core.util.StringList;

public class PluginManager {
	private Logger serverLogger = LoggerFactory.getLogger("logger_server."
			+ getClass());
	private final String DELETE_PLUGIN_FLAG = "_delete_plugin_flag";

	private LinkedHashMap alPluginIds = null;

	public PluginManager() {
		this.alPluginIds = new LinkedHashMap();

		Collections.synchronizedMap(this.alPluginIds);
	}

	public void load() {
		try {
			File file = new File("plugins");
			this.serverLogger.info("load plugin at " + file.getCanonicalPath());
			if ((!file.isDirectory()) || (!file.exists())) {
				throw new Exception("路径'plugins'不是目录或不存在!");
			}
			File[] plugin = file.listFiles();
			for (int i = 0; i < plugin.length; i++) {
				if (plugin[i].isDirectory()) {
					try {
						if ("classes;lib;plugins".indexOf(plugin[i].getName()
								.toLowerCase()) < 0) {
							if (new File(plugin[i].getPath()
									+ "_delete_plugin_flag").exists()) {
								FileTools.deleteDir(plugin[i].getPath(), true);
								if (!new File(plugin[i].getPath()).exists()) {
									new File(plugin[i].getPath()
											+ "_delete_plugin_flag").delete();
								}
							} else {
								String id = plugin[i].getName();
								String profile = plugin[i].getPath() + "/"
										+ "plugin.properties";
								if (!new File(profile).isFile()) {
									this.serverLogger.error("插件配置文件" + profile
											+ "不存在，忽略此插件");
								} else {
									String fileList = FileTools.getFileText(
											plugin[i].getPath()
													+ "/file.properties",
											Util.getCharset());

									PropertyResourceBundle prb = new PropertyResourceBundle(
											plugin[i].getPath() + "/"
													+ "plugin.properties");
									if ((prb.getString("pluginName") == null)
											|| (prb.getString("pluginName")
													.length() == 0)) {
										this.serverLogger
												.error("插件配置文件"
														+ profile
														+ "没有包含插件名称(pluginName=)，忽略此插件");
									} else {
										Plugin p = new Plugin(id,
												prb.getString("pluginName"),
												prb.getString("pluginMemo"),
												prb.getString("entryClass"),
												plugin[i].getPath(), fileList,
												"", System.currentTimeMillis());

										this.alPluginIds.put(id, p);

										prb.close();
									}
								}
							}
						}
					} catch (Exception ex) {
						this.serverLogger.error("加载插件'" + plugin[i].getPath()
								+ "'时出现异常!", ex);
					}
				}
			}
		} catch (Exception e) {
			this.serverLogger.error("加载插件时出现异常!", e);
		}
	}

	public void reload() {
		this.alPluginIds.clear();
		load();
	}

	public String[][] getPlugins() {
		Object[] array = this.alPluginIds.keySet().toArray();

		String[][] plugins = new String[array.length][3];

		for (int i = 0; i < array.length; i++) {
			String id = (String) array[i];

			Plugin plugin = (Plugin) this.alPluginIds.get(id);

			plugins[i][0] = id;
			plugins[i][1] = plugin.getPluginName();
			plugins[i][2] = plugin.getEntryClass();
		}

		return plugins;
	}

	public HashMap getPropertyById(String id) {
		Plugin plugin = (Plugin) this.alPluginIds.get(id);
		if (plugin != null) {
			HashMap hashMap = new HashMap();
			hashMap.put("id", plugin.getId());
			hashMap.put("pluginName", plugin.getPluginName());
			hashMap.put("pluginMemo", plugin.getPluginMemo());
			hashMap.put("entryClass", plugin.getEntryClass());
			hashMap.put("fileList", plugin.getFileList());
			hashMap.put("pluginPath", plugin.getPluginPath());
			hashMap.put("version", plugin.getVersion());
			hashMap.put("error", plugin.getError());
			return hashMap;
		}

		return null;
	}

	public boolean addPlugin(HashMap propertyMap) throws Exception {
		try {
			String id = (String) propertyMap.get("id");
			String name = (String) propertyMap.get("pluginName");
			if ((id == null) || (id.length() == 0))
				throw new Exception("插件ID不能为空");
			if ((name == null) || (name.length() == 0))
				throw new Exception("插件名称不能为空");
			if (this.alPluginIds.get(id) != null)
				throw new Exception("插件ID已经存在:" + id);

			String path = "plugins/" + id;
			if (!new File(path).exists())
				new File(path).mkdirs();

			if (new File(path + "_delete_plugin_flag").exists())
				new File(path + "_delete_plugin_flag").delete();

			String fileList = (String) propertyMap.get("fileList");

			Util.write(path + "/file.properties", false, fileList);

			HashMap tempMap = (HashMap) propertyMap.clone();
			tempMap.remove("fileList");

			Util.writePropertyFile(tempMap, path + "/"
					+ "plugin.properties");
			Plugin p = new Plugin(id, name,
					(String) propertyMap.get("pluginMemo"),
					(String) propertyMap.get("entryClass"), path, fileList, "",
					System.currentTimeMillis());
			this.alPluginIds.put(id, p);

			return true;
		} catch (Exception e) {
			this.serverLogger.error("添加新插件异常!", e);
			throw e;
		}
	}

	public boolean modifyPlugin(HashMap propertyMap) throws Exception {
		try {
			String id = (String) propertyMap.get("id");
			Plugin p = (Plugin) this.alPluginIds.get(id);
			if (p != null) {
				String entryClass = (String) propertyMap.get("entryClass");
				String path = p.getPluginPath();
				if (!new File(path).exists())
					new File(path).mkdirs();

				if (new File(path + "_delete_plugin_flag").exists())
					new File(path + "_delete_plugin_flag").delete();

				String fileList = (String) propertyMap.get("fileList");

				Util.write(path + "/file.properties", false, fileList);

				HashMap tempMap = (HashMap) propertyMap.clone();
				tempMap.remove("fileList");
				Util.writePropertyFile(tempMap, path + "/"
						+ "plugin.properties");

				long modify = p.getClassLastModify();

				p = new Plugin(id, (String) propertyMap.get("pluginName"),
						(String) propertyMap.get("pluginMemo"),
						(String) propertyMap.get("entryClass"), path, fileList,
						"", modify);

				this.alPluginIds.put(id, p);

				return true;
			}

			this.serverLogger.error("编辑插件失败，ID为" + id + "的插件不存在");
			return false;
		} catch (Exception e) {
			this.serverLogger.error("修改插件异常!", e);
			throw e;
		}
	}

	public boolean removePlugin(String pluginId) throws Exception {
		try {
			Plugin plugin = (Plugin) this.alPluginIds.get(pluginId);
			if (plugin != null) {
				new File(plugin.getPluginPath() + "_delete_plugin_flag")
						.createNewFile();
				this.serverLogger.info("removePlugin " + pluginId);

				this.alPluginIds.remove(plugin.getId());
			}

			return true;
		} catch (Exception e) {
			this.serverLogger.error("删除插件异常!", e);
			throw e;
		}
	}

	public long getClassLastModify(String pluginId) {
		Plugin plugin = (Plugin) this.alPluginIds.get(pluginId);
		if (plugin == null)
			return -1L;
		return plugin.getClassLastModify();
	}

	public void setClassLastModify(String pluginId, long classLastModify) {
		Plugin plugin = (Plugin) this.alPluginIds.get(pluginId);
		if (plugin != null) {
			plugin.setClassLastModify(classLastModify);
			this.serverLogger.info("PluginManager.setClassLastModify");
		}
	}

	public TaskListener getTaskInstance(String pluginId) {
		Plugin plugin = (Plugin) this.alPluginIds.get(pluginId);
		if (plugin != null) {
			return plugin.getTaskInstance();
		}

		ErrorClassHandleTask result = new ErrorClassHandleTask();
		result.setError("", new Exception("找不到插件：id=" + pluginId
				+ ",将用ErrorClassHandleTask来接管此任务。请配置正确的任务插件并重启任务。"));
		return result;
	}

	public void deletePluginFile(String pluginId, String file) throws Exception {
		try {
			Plugin plugin = (Plugin) this.alPluginIds.get(pluginId);

			if (plugin != null) {
				File f = new File(plugin.getPluginPath() + "/" + file);

				if ((!f.exists()) || (f.delete())) {
					StringList fileList = new StringList();
					fileList.setAllText(plugin.getFileList());
					fileList.removeByName(file);
					String text = fileList.getAllText();
					Util.write(plugin.getPluginPath() + "/file.properties",
							false, text);
					plugin.setFileList(text);
				} else {
					throw new Exception("删除插件文件失败：" + file + " . "
							+ f.getCanonicalPath());
				}

			}

		} catch (Exception e) {
			this.serverLogger.error("删除插件文件出错", e);
		}
	}
}
