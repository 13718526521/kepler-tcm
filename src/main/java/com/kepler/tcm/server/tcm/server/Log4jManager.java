package com.kepler.tcm.server.tcm.server;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.xml.Log4jEntityResolver;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.InputSource;

public class Log4jManager {
	private Logger serverLogger = Logger.getLogger("logger_server." + getClass());

	public static final Pattern TASKIDPATTERN = Pattern.compile("\\d{8}");

	public synchronized void load() {
		try {
			DOMConfigurator.configure(Server.LOG4J_CONFIG_FILE.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void addTaskLogger(String taskID, Log4jProperties lp) {
		try {
			if (Server.LOG4J_CONFIG_FILE.exists()) {
				Document document = getDocument(Server.LOG4J_CONFIG_FILE);
				Element appender = null;
				Element root = document.getRootElement();

				if (exists(root, taskID)) {
					return;
				}
				document = removeNode(document, taskID);
				root = document.getRootElement();

				String[] LEVELS = { lp.getLogLevel(), lp.getLogLevel2() };
				String[] appenderNames = { "appender1_" + taskID, "appender2_" + taskID };

				String taskPath = "${log.path}/${server.name}/" + taskID + "/";
				String[] logFileNames = { taskPath + "out.log", taskPath + "error.log" };

				for (int i = 0; i < 2; i++) {
					String appenderName = appenderNames[i];

					if ("0".equals(lp.getLogType())) {
						appender = root.addElement("appender").addAttribute("name", appenderName).addAttribute("class",
								"org.apache.log4j.FileAppender");
					} else if ("1".equals(lp.getLogType())) {
						appender = root.addElement("appender").addAttribute("name", appenderName).addAttribute("class",
								"org.apache.log4j.DailyRollingFileAppender");
					} else if ("2".equals(lp.getLogType())) {
						appender = root.addElement("appender").addAttribute("name", appenderName).addAttribute("class",
								"org.apache.log4j.RollingFileAppender");
						appender.addElement("param").addAttribute("name", "MaxFileSize").addAttribute("value",
								Integer.parseInt(lp.getLogMaxSize()) * 1024 + "KB");
						appender.addElement("param").addAttribute("name", "MaxBackupIndex").addAttribute("value",
								lp.getLogBackNums());

						appender.addElement("param").addAttribute("name", "Encoding").addAttribute("value",
								Util.getCharset());
					} else {
						throw new Exception("不支持的日志类别(" + lp.getLogType() + ")!");
					}

					appender.addElement("param").addAttribute("name", "File").addAttribute("value", logFileNames[i]);

					appender.addElement("param").addAttribute("name", "Append").addAttribute("value", "true");

					appender.addElement("param").addAttribute("name", "Threshold").addAttribute("value", LEVELS[i]);

					Element layout = appender.addElement("layout").addAttribute("class",
							"org.apache.log4j.PatternLayout");

					layout.addElement("param").addAttribute("name", "ConversionPattern").addAttribute("value",
							"%d|%p|%m%n");
				}

				Element logger = root.addElement("logger").addAttribute("name", "logger_" + taskID);

				if ("1".equals(lp.getMerged()))
					logger.addAttribute("additivity", "true");
				else {
					logger.addAttribute("additivity", "false");
				}

				logger.addElement("appender-ref").addAttribute("ref", "appender1_" + taskID);
				logger.addElement("appender-ref").addAttribute("ref", "appender2_" + taskID);

				document = orderNode(document);
				write2File(document);
			} else {
				initLog4j();
				addTaskLogger(taskID, lp);
			}
			load();
		} catch (Exception e) {
			this.serverLogger.error("添加任务日志器出现异常," + e);
		}
	}

	public synchronized void addLogger(String suffix, String logFileName, String conversionPattern,
			boolean additivity) {
		try {
			if (Server.LOG4J_CONFIG_FILE.exists()) {
				Document document = getDocument(Server.LOG4J_CONFIG_FILE);
				Element root = document.getRootElement();
				if (exists(root, suffix)) {
					return;
				}
				document = removeNode(document, suffix);
				root = document.getRootElement();

				Element appender = root.addElement("appender").addAttribute("name", "appender_" + suffix)
						.addAttribute("class", "org.apache.log4j.RollingFileAppender");

				appender.addElement("param").addAttribute("name", "MaxFileSize").addAttribute("value", "2048KB");
				appender.addElement("param").addAttribute("name", "MaxBackupIndex").addAttribute("value", "20");

				appender.addElement("param").addAttribute("name", "Encoding").addAttribute("value", Util.getCharset());

				appender.addElement("param").addAttribute("name", "File").addAttribute("value", logFileName);

				appender.addElement("param").addAttribute("name", "Append").addAttribute("value", "true");

				Element layout = appender.addElement("layout").addAttribute("class", "org.apache.log4j.PatternLayout");

				layout.addElement("param").addAttribute("name", "ConversionPattern").addAttribute("value",
						conversionPattern);

				Element logger = root.addElement("logger").addAttribute("name", "logger_" + suffix);

				if (additivity)
					logger.addAttribute("additivity", "true");
				else
					logger.addAttribute("additivity", "false");

				logger.addElement("level").addAttribute("value", "DEBUG");

				logger.addElement("appender-ref").addAttribute("ref", "appender_" + suffix);

				document = orderNode(document);
				write2File(document);
			} else {
				initLog4j();
				addLogger(suffix, logFileName, conversionPattern, additivity);
			}

			load();
		} catch (Exception e) {
			this.serverLogger.error("添加日志器出现异常," + e);
		}
	}

	public synchronized void addRootLogger(String logFileName) {
		try {
			if (Server.LOG4J_CONFIG_FILE.exists()) {
				Document document = getDocument(Server.LOG4J_CONFIG_FILE);
				Element root = document.getRootElement();
				if (exists(root, "root")) {
					return;
				}
				document = removeNode(document, "root");

				root = document.getRootElement();

				Element appender = root.addElement("appender").addAttribute("name", "appender_root")
						.addAttribute("class", "org.apache.log4j.RollingFileAppender");
				appender.addElement("param").addAttribute("name", "MaxFileSize").addAttribute("value", "2048KB");
				appender.addElement("param").addAttribute("name", "MaxBackupIndex").addAttribute("value", "20");

				appender.addElement("param").addAttribute("name", "Encoding").addAttribute("value", Util.getCharset());

				appender.addElement("param").addAttribute("name", "Threshold").addAttribute("value", "WARN");

				appender.addElement("param").addAttribute("name", "File").addAttribute("value", logFileName);

				appender.addElement("param").addAttribute("name", "Append").addAttribute("value", "true");

				Element layout = appender.addElement("layout").addAttribute("class", "org.apache.log4j.PatternLayout");

				layout.addElement("param").addAttribute("name", "ConversionPattern").addAttribute("value",
						"%d %p %t - %m%n");

				/* Element appender = root.addElement("appender"); */
				appender.addAttribute("name", "console");
				appender.addAttribute("class", "org.apache.log4j.ConsoleAppender");

				appender.addElement("param").addAttribute("name", "Threshold").addAttribute("value", "INFO");
				/* Element layout = appender.addElement("layout"); */
				layout.addAttribute("class", "org.apache.log4j.PatternLayout");
				layout.addElement("param").addAttribute("name", "ConversionPattern").addAttribute("value",
						"%d{yyyy-MM-dd HH:mm:ss} %p %m (%c.java:%L) %n");

				Element logger = root.addElement("root");

				logger.addElement("level").addAttribute("value", "DEBUG");

				logger.addElement("appender-ref").addAttribute("ref", "appender_root");
				logger.addElement("appender-ref").addAttribute("ref", "console");
				document = orderNode(document);
				write2File(document);
			} else {
				initLog4j();
				addRootLogger(logFileName);
			}

			load();
		} catch (Exception e) {
			e.printStackTrace();
			this.serverLogger.error("添加根日志器出现异常," + e);
		}
	}

	public synchronized void modifyTaskLogger(String taskID, Log4jProperties lp) {
		try {
			removeTaskLogger(taskID);
			addTaskLogger(taskID, lp);
			load();
		} catch (Exception e) {
			this.serverLogger.error("修改日志器出现异常," + e);
		}
	}

	public synchronized void removeTaskLogger(String taskID) {
		try {
			if (Server.LOG4J_CONFIG_FILE.exists()) {
				Document document = getDocument(Server.LOG4J_CONFIG_FILE);
				document = removeNode(document, taskID);
				write2File(document);
			} else {
				initLog4j();
			}
			load();
		} catch (Exception e) {
			this.serverLogger.error("移除日志器出现异常," + e);
		}
	}

	private boolean exists(Element root, String suffix) {
		List list = null;
		Node node = null;
		boolean bState = false;
		if ("root".equals(suffix)) {
			list = root.selectNodes("./root");
			if ((list != null) && (list.size() == 1))
				bState = true;
		} else {
			list = root.selectNodes("./logger");
			if (list != null) {
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					node = (Node) iterator.next();
					if (node.valueOf("@name").equals("logger_" + suffix)) {
						bState = true;
						break;
					}
				}
			}
		}

		list = root.selectNodes("./appender");
		if ((bState) && (list != null))
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				node = (Node) iterator.next();
				if (node.valueOf("@name").equals("appender_" + suffix)) {
					List listLayout = node.selectNodes("./layout/param");
					if ((listLayout == null) || (listLayout.size() != 1)) {
						bState = false;
						break;
					}
					Node param = (Node) listLayout.get(0);
					if ("root".equals(suffix)) {
						if ((!"ConversionPattern".equalsIgnoreCase(param.valueOf("@name")))
								|| (param.valueOf("@value").indexOf("%t") == -1))
							break;
						bState = true;
						break;
					}

					if (TASKIDPATTERN.matcher(suffix).matches()) {
						if ((!"ConversionPattern".equalsIgnoreCase(param.valueOf("@name")))
								|| (!"%d|%p|%m%n".equals(param.valueOf("@value"))))
							break;
						bState = true;
						break;
					}

					bState = true;

					break;
				}
			}
		else {
			bState = false;
		}
		return bState;
	}

	private Document orderNode(Document document) throws Exception {
		Element root = document.getRootElement();
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(Log4jTemplate.XMLDOCTYPE);
		sBuffer.append(Log4jTemplate.XMLROOTHEADER);
		List list = root.selectNodes("./appender");
		for (int i = 0; i < list.size(); i++) {
			sBuffer.append(((Node) list.get(i)).asXML());
		}
		list = root.selectNodes("./logger");
		for (int i = 0; i < list.size(); i++) {
			sBuffer.append(((Node) list.get(i)).asXML());
		}
		list = root.selectNodes("./root");
		for (int i = 0; i < list.size(); i++) {
			sBuffer.append(((Node) list.get(i)).asXML());
		}
		sBuffer.append(Log4jTemplate.XMLROOTENDER);
		return getDocument(sBuffer.toString());
	}

	private Document removeNode(Document document, String suffix) throws Exception {
		Element root = document.getRootElement();
		Node node = null;
		String name = "";
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(Log4jTemplate.XMLDOCTYPE);
		sBuffer.append(Log4jTemplate.XMLROOTHEADER);
		List list = root.selectNodes("./appender");
		for (int i = 0; i < list.size(); i++) {
			node = (Node) list.get(i);
			name = node.valueOf("@name").trim();

			if ((!("appender_" + suffix).equalsIgnoreCase(name)) && (!("appender1_" + suffix).equalsIgnoreCase(name))
					&& (!("appender2_" + suffix).equalsIgnoreCase(name))) {
				sBuffer.append(node.asXML());
			}

		}

		list = root.selectNodes("./logger");
		for (int i = 0; i < list.size(); i++) {
			node = (Node) list.get(i);
			name = node.valueOf("@name").trim();
			if (!("logger_" + suffix).equalsIgnoreCase(name)) {
				sBuffer.append(node.asXML());
			}
		}
		if (!"root".equalsIgnoreCase(suffix)) {
			node = root.selectSingleNode("./root");
			sBuffer.append(node.asXML());
		}
		sBuffer.append(Log4jTemplate.XMLROOTENDER);
		return getDocument(sBuffer.toString());
	}

	private void initLog4j() throws Exception {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(Server.LOG4J_CONFIG_FILE);
			fileWriter.write(Log4jTemplate.getLog4jTemplate());
			fileWriter.flush();
			fileWriter.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (fileWriter != null) {
					fileWriter.close();
					fileWriter = null;
				}
			} catch (Exception localException1) {
			}
		}
	}

	private Document getDocument(File file) throws Exception {
		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		SAXReader reader = null;
		try {
			fileInputStream = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new SAXReader();
			reader.setEntityResolver(new Log4jEntityResolver());
			InputSource inputSource = new InputSource(inputStreamReader);
			inputSource.setSystemId("dummy://log4j.dtd");
			return reader.read(inputSource);
		} catch (Exception e) {
			throw e;
		} finally {
			if (fileInputStream != null)
				try {
					fileInputStream.close();
					fileInputStream = null;
				} catch (Exception localException3) {
				}
			if (inputStreamReader != null)
				try {
					inputStreamReader.close();
					inputStreamReader = null;
				} catch (Exception localException4) {
				}
			if (reader != null)
				reader = null;
		}
	}

	private Document getDocument(String xmlString) throws Exception {
		SAXReader reader = new SAXReader();
		reader.setEntityResolver(new Log4jEntityResolver());
		InputSource inputSource = new InputSource(new ByteArrayInputStream(xmlString.getBytes("UTF-8")));
		inputSource.setSystemId("dummy://log4j.dtd");
		return reader.read(inputSource);
	}

	private void write2File(Document document) throws Exception {
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new FileOutputStream(Server.LOG4J_CONFIG_FILE), format);
		writer.write(document);
		writer.flush();
	}
}