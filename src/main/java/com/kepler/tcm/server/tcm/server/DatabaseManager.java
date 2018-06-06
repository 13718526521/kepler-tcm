package com.kepler.tcm.server.tcm.server;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class DatabaseManager {
	private ArrayList alDatabase = null;
	private File configFile = null;
	private Logger serverLogger = Logger.getLogger("logger_server." + getClass());

	public DatabaseManager() {
		this.configFile = new File("./conf/database.xml");
		this.alDatabase = new ArrayList();
		Collections.synchronizedList(this.alDatabase);
	}

	public void load() {
		try {
			if (!this.configFile.exists()) {
				return;
			}

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(this.configFile);
			doc.normalize();
			NodeList links = doc.getElementsByTagName("database");
			for (int i = 0; i < links.getLength(); i++) {
				Element eDatabase = (Element) links.item(i);

				String strName = eDatabase.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
				String strDriver = eDatabase.getElementsByTagName("driver").item(0).getFirstChild().getNodeValue();
				String strUrl = eDatabase.getElementsByTagName("url").item(0).getFirstChild().getNodeValue();
				String strUser = "";

				if (eDatabase.getElementsByTagName("user").item(0).getFirstChild() != null)
					strUser = eDatabase.getElementsByTagName("user").item(0).getFirstChild().getNodeValue();
				String strPass = eDatabase.getElementsByTagName("pass").item(0).getFirstChild() == null ? ""
						: eDatabase.getElementsByTagName("pass").item(0).getFirstChild().getNodeValue();

				this.alDatabase.add(new Database(strName, strDriver, strUrl, strUser, strPass));
			}
		} catch (Exception e) {
			this.serverLogger.error("加载数据库配置时出现异常!", e);
		}
	}

	public void reload() {
		this.alDatabase.clear();
		load();
	}

	public void addDatabase(HashMap propertyMap) throws Exception {
		try {
			if (exist((String) propertyMap.get("name"))) {
				throw new Exception("数据库" + (String) propertyMap.get("name") + "已经存在!");
			}

			if (this.configFile.exists()) {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(this.configFile);
				doc.normalize();

				Element eDatabase = doc.createElement("database");

				Element eName = doc.createElement("name");
				Text textseg = doc.createTextNode((String) propertyMap.get("name"));
				eName.appendChild(textseg);
				eDatabase.appendChild(eName);

				Element eDriver = doc.createElement("driver");
				textseg = doc.createTextNode((String) propertyMap.get("driver"));
				eDriver.appendChild(textseg);
				eDatabase.appendChild(eDriver);

				Element eUrl = doc.createElement("url");
				textseg = doc.createTextNode((String) propertyMap.get("url"));
				eUrl.appendChild(textseg);
				eDatabase.appendChild(eUrl);

				Element eUser = doc.createElement("user");
				textseg = doc.createTextNode((String) propertyMap.get("user"));
				eUser.appendChild(textseg);
				eDatabase.appendChild(eUser);

				Element ePass = doc.createElement("pass");
				textseg = doc.createTextNode((String) propertyMap.get("pass"));
				ePass.appendChild(textseg);
				eDatabase.appendChild(ePass);

				doc.getDocumentElement().appendChild(eDatabase);

				TransformerFactory tFactory = TransformerFactory.newInstance();
				Transformer transformer = tFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(this.configFile.getAbsolutePath());
				transformer.transform(source, result);
			} else {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.newDocument();
				doc.normalize();

				Element eConfig = doc.createElement("config");

				Element eDatabase = doc.createElement("database");

				Element eName = doc.createElement("name");
				Text textseg = doc.createTextNode((String) propertyMap.get("name"));
				eName.appendChild(textseg);
				eDatabase.appendChild(eName);

				Element eDriver = doc.createElement("driver");
				textseg = doc.createTextNode((String) propertyMap.get("driver"));
				eDriver.appendChild(textseg);
				eDatabase.appendChild(eDriver);

				Element eUrl = doc.createElement("url");
				textseg = doc.createTextNode((String) propertyMap.get("url"));
				eUrl.appendChild(textseg);
				eDatabase.appendChild(eUrl);

				Element eUser = doc.createElement("user");
				textseg = doc.createTextNode((String) propertyMap.get("user"));
				eUser.appendChild(textseg);
				eDatabase.appendChild(eUser);

				Element ePass = doc.createElement("pass");
				textseg = doc.createTextNode((String) propertyMap.get("pass"));
				ePass.appendChild(textseg);
				eDatabase.appendChild(ePass);

				eConfig.appendChild(eDatabase);

				doc.appendChild(eConfig);

				TransformerFactory tFactory = TransformerFactory.newInstance();
				Transformer transformer = tFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(this.configFile.getAbsolutePath());
				transformer.transform(source, result);
			}

			reload();
		} catch (Exception e) {
			this.serverLogger.error("添加数据库配置异常!", e);
			throw e;
		}
	}

	public void modifyDatabase(String oldName, HashMap propertyMap) throws Exception {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(this.configFile);
			doc.normalize();
			NodeList dbs = doc.getElementsByTagName("database");
			for (int i = 0; i < dbs.getLength(); i++) {
				Element database = (Element) dbs.item(i);
				if (database.getElementsByTagName("name").item(0).getFirstChild().getNodeValue().equals(oldName)) {
					database.getElementsByTagName("name").item(0).getFirstChild()
							.setNodeValue((String) propertyMap.get("name"));
					database.getElementsByTagName("driver").item(0).getFirstChild()
							.setNodeValue((String) propertyMap.get("driver"));
					database.getElementsByTagName("url").item(0).getFirstChild()
							.setNodeValue((String) propertyMap.get("url"));
					database.getElementsByTagName("user").item(0).getFirstChild()
							.setNodeValue((String) propertyMap.get("user"));
					if (database.getElementsByTagName("pass").item(0).getFirstChild() != null) {
						database.getElementsByTagName("pass").item(0).getFirstChild()
								.setNodeValue((String) propertyMap.get("pass"));
					} else {
						Element ePass = (Element) database.getElementsByTagName("pass").item(0);
						Text textseg = doc.createTextNode((String) propertyMap.get("pass"));
						ePass.appendChild(textseg);
					}
				}
			}

			HashMap taskMap = Server.taskManager.getTasks();
			Iterator iterator = taskMap.keySet().iterator();
			while (iterator.hasNext()) {
				Object taskID = iterator.next();
				File taskPropertyFile = new File("./working/" + (String) taskID + "/" + "task.properties");

				String body = "";
				char[] chars = new char[64];
				int off = 0;

				FileReader reader = new FileReader(taskPropertyFile);
				while ((off = reader.read(chars)) != -1) {
					body = body + new String(chars, 0, off);
				}
				reader.close();

				body = body.replaceAll("baseName = " + oldName, "baseName = " + (String) propertyMap.get("name"));

				FileWriter writer = new FileWriter(taskPropertyFile, false);
				writer.write(body);
				writer.close();

				((RemoteTaskImpl) taskMap.get(taskID)).readTaskProperty();
			}

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(this.configFile.getAbsolutePath());
			transformer.transform(source, result);

			reload();
		} catch (Exception e) {
			this.serverLogger.error("修改数据库配置异常!", e);
			throw e;
		}
	}

	public void removeDatabase(String strDatabaseName) throws Exception {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(this.configFile);
			doc.normalize();
			NodeList config = doc.getElementsByTagName("config");
			NodeList dbs = doc.getElementsByTagName("database");
			for (int i = 0; i < dbs.getLength(); i++) {
				Element database = (Element) dbs.item(i);
				if (database.getElementsByTagName("name").item(0).getFirstChild().getNodeValue()
						.equals(strDatabaseName)) {
					config.item(0).removeChild(database);
					break;
				}
			}

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(this.configFile.getAbsolutePath());
			transformer.transform(source, result);

			reload();
		} catch (Exception e) {
			this.serverLogger.error("删除数据库配置异常!", e);
			throw e;
		}
	}

	public String[] getDatabase() {
		String[] databaseNames = new String[this.alDatabase.size()];
		for (int i = 0; i < this.alDatabase.size(); i++) {
			databaseNames[i] = ((Database) this.alDatabase.get(i)).getDatabaseName();
		}
		return databaseNames;
	}

	public Database getDatabaseByName(String strName) {
		for (int i = 0; i < this.alDatabase.size(); i++) {
			Database database = (Database) this.alDatabase.get(i);
			if (database.getDatabaseName().equals(strName)) {
				return database;
			}
		}
		return null;
	}

	public boolean exist(String strDatabaseName) {
		for (int i = 0; i < this.alDatabase.size(); i++) {
			Database database = (Database) this.alDatabase.get(i);
			if (database.getDatabaseName().equals(strDatabaseName)) {
				return true;
			}
		}
		return false;
	}

	public HashMap getPropertyByName(String strName) {
		for (int i = 0; i < this.alDatabase.size(); i++) {
			Database database = (Database) this.alDatabase.get(i);
			if (database.getDatabaseName().equals(strName)) {
				HashMap propertyMap = new HashMap();
				propertyMap.put("name", database.getDatabaseName());
				propertyMap.put("driver", database.getDatabaseDriver());
				propertyMap.put("url", database.getDatabaseUrl());
				propertyMap.put("user", database.getDatabaseUser());
				propertyMap.put("pass", database.getDatabasePass());
				return propertyMap;
			}
		}
		return null;
	}
}