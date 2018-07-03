package com.kepler.tcm.client;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.kepler.tcm.core.server.Server;
import com.kepler.tcm.core.util.FileUtil;

public class PluginClientTest {
	
	PluginClient pluginClient = null;

	@Before
	public void setUp() throws Exception {
		
		String agentAndServer = "127.0.0.1:1098@server01";
		pluginClient = pluginClient == null ? new PluginClient(agentAndServer) : pluginClient;
	}

	@Test
	public void testPluginClient() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlugins() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddPlugin() throws Exception {
		
		
		HashMap map = new HashMap();
		
		map.put("id", "10001");
		map.put("pluginName", "TCM TEST");
		map.put("pluginMemo", "测试添加插件");
		map.put("entryClass", "com.kepler.tcm.demo.DemoTask");
		map.put("fileList", "fileList");
		
		pluginClient.addPlugin(map);
	}

	@Test
	public void testEditPlugin() throws Exception {
		
		HashMap map = new HashMap();
		
		map.put("id", "10001");
		map.put("pluginName", "TCM TEST");
		map.put("pluginMemo", "测试编辑插件");
		map.put("entryClass", "com.kepler.tcm.demo.DemoTask");
		map.put("fileList", "");
		pluginClient.editPlugin(map);
	}

	@Test
	public void testRemovePlugin() throws Exception {
		pluginClient.removePlugin("10001");
	}
	
	
	@Test
	public void testAddPluginFile() throws Exception {
		String id = "10001";
		String serverName = "server01";
		
		String pluginPath = "F:/BONC TCM Server/servers/" + serverName + "/" + Server.PLUGIN_PATH + "/" + id + "/";
		
		String srcfilePath = "F:\\test.txt";
		File file = new File(srcfilePath);
		
		String fileName = file.getName();
		
		System.out.println("源文件名："+fileName);
		
		String prefix = fileName.substring(fileName.lastIndexOf(".")+1);
		
		System.out.println("源文件扩展名："+ prefix);
		
		InputStream input  = new FileInputStream(file);
		
		pluginClient.addPluginFile(input, pluginPath + fileName, -1);
	}
	
	@Test
	public void testAddPluginFile2() throws Exception {
		String id = "10001";
		String serverName = "server01";
		
		String pluginPath = "F:/BONC TCM Server/servers/" + serverName + "/" + Server.PLUGIN_PATH + "/" + id + "/";
		
		String srcfilePath = "F:\\test.txt";
		
		File file = new File(srcfilePath);
		
		String fileName = file.getName();
		
		System.out.println("源文件名："+fileName);
		
		String prefix = fileName.substring(fileName.lastIndexOf(".")+1);
		
		System.out.println("源文件扩展名："+ prefix);

		byte[] bytes = FileUtil.file2Byte(srcfilePath);
		
		
		pluginClient.addPluginFile(bytes, pluginPath + fileName, -1);
	}
	
	@Test
	public void testDeletePluginFile() {
		
	}

	@Test
	public void testReloadPlugin() throws Exception {
		pluginClient.reloadPlugin("10001");
	}

	@Test
	public void testGetPluginPropertyById() throws Exception {
		HashMap pluginProperty = pluginClient.getPluginPropertyById("10001");
		
		Iterator it = pluginProperty.keySet().iterator();
		while(it.hasNext()){
			String key = (String )it.next();
			System.out.println(key+":"+pluginProperty.get(key));
		}
	}

}
