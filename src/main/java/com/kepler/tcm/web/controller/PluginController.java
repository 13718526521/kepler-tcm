package com.kepler.tcm.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kepler.tcm.domain.Plugin;
import com.kepler.tcm.service.PluginService;

@RestController
@RequestMapping(value = "/plugin")
public class PluginController {

	@Autowired
	private PluginService pluginService;

	private static final Logger log = LoggerFactory
			.getLogger(PluginController.class);

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public Map<String, Object> query(String agentAndServer) throws Exception {
		return pluginService.query(agentAndServer);
	}

	@RequestMapping(value = "/querydetail", method = RequestMethod.POST)
	public Map<String, Object> querydetail(String agentAndServer)
			throws Exception {
		return pluginService.querydetail(agentAndServer);
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public Map<String, Object> add(String agentAndServer, String id,
			String pluginName, String pluginMemo, String entryClass)
			throws Exception {
		return pluginService.add(agentAndServer, id, pluginName, pluginMemo,
				entryClass);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public Map<String, Object> edit(String agentAndServer, String id,
			String pluginName, String pluginMemo, String entryClass)
			throws Exception {
		return pluginService.edit(agentAndServer, id, pluginName, pluginMemo,
				entryClass);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> delete(String agentAndServer, String id)
			throws Exception {
		return pluginService.delete(agentAndServer, id);
	}

	@RequestMapping(value = "/reload", method = RequestMethod.POST)
	public Map<String, Object> reload(String agentAndServer, String id)
			throws Exception {
		return pluginService.reload(agentAndServer, id);
	}

	@RequestMapping(value = "/getpropertybyid", method = RequestMethod.GET)
	public Map<String, Object> getpropertybyid(String agentAndServer, String id)
			throws Exception {
		return pluginService.getpropertybyid(agentAndServer, id);
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public Map<String, Object> upload(String agentAndServer,Plugin plugin, MultipartFile[] file,String[] className,String auto_plugin_id)
			throws Exception {
		return pluginService.upload(agentAndServer,plugin, file,className,auto_plugin_id);
	}
	@RequestMapping(value = "/uploadedit", method = RequestMethod.POST)
	public Map<String, Object> uploadedit(String agentAndServer,Plugin plugin, MultipartFile[] file,String[] className)
			throws Exception {
		return pluginService.uploadedit(agentAndServer,plugin, file,className);
	}

	public static boolean uploadIStream(String rootPath, String fileName,InputStream inputStream) {
		boolean flag = false;
		if (StringUtils.isAnyBlank(rootPath, fileName)) {
			return flag;
		}
		if (inputStream == null) {
			return flag;
		}
		File dir = new File(rootPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		FileOutputStream os = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			os = new FileOutputStream(new File(dir, fileName));
			bos = new BufferedOutputStream(os);
			bis = new BufferedInputStream(inputStream);
			int b = -1;
			while ((b = bis.read()) != -1) {
				bos.write(b);
			}
			flag = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public void test(Plugin plugin, MultipartFile[] file, String[] className)
			throws Exception {
		log.info("Pluginid:" + plugin.getPluginid());
		log.info("PluginName:" + plugin.getPluginName());
		log.info("PluginMemo:" + plugin.getPluginMemo());
		log.info("EntryClass:" + plugin.getEntryClass());
		log.info("Error:" + plugin.getError());
		log.info("Version:" + plugin.getVersion());
		log.info("PluginPath:" + plugin.getPluginPath());
		log.info("FileList:" + plugin.getFileList());
		log.info("--------------------");
		log.info("length:" + file.length);
		log.info("Bytes:" + file[0].getBytes());
		log.info("Class:" + file[0].getClass());
		log.info("ContentType:" + file[0].getContentType());
		log.info("InputStream:" + file[0].getInputStream());
		log.info("Name:" + file[0].getName());
		log.info("OriginalFilename:" + file[0].getOriginalFilename());
		log.info(""+ file[0].getOriginalFilename().substring((file[0].getOriginalFilename().lastIndexOf(".") + 1)));
		log.info(""+ file[1].getOriginalFilename().substring((file[1].getOriginalFilename().lastIndexOf(".") + 1)));
		log.info("Size:" + file[0].getSize());
		log.info("className:" + className[0]);
		log.info("className:" + className[1]);
		uploadIStream("F:/test", file[0].getOriginalFilename(),file[0].getInputStream());
		uploadIStream("F:/test", file[1].getOriginalFilename(),file[1].getInputStream());
	}

}
