package com.kepler.tcm.server.tcm.agent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import com.kepler.tcm.server.core.util.Convert;
import com.kepler.tcm.server.core.util.LogA;
import com.kepler.tcm.server.core.util.Resource;
import com.kepler.tcm.server.tcm.server.Config;
import com.kepler.tcm.server.tcm.server.Plugin;
import com.kepler.tcm.server.tcm.server.RemoteServer;
import com.kepler.tcm.server.tcm.server.RemoteTask;
import com.kepler.tcm.server.tcm.server.Server;
import com.kepler.tcm.server.tcm.server.Shutdown;
import com.kepler.tcm.server.tcm.server.Task;

public class RemoteAgentImpl extends UnicastRemoteObject implements RemoteAgent {
	static final Logger logger = Logger.getLogger(RemoteAgentImpl.class);

	static String ENTER = "<br>\r\n";

	private long lastModify = -1L;

	private String name = "";

	private Map processMap = new HashMap();

	private String className = "(@RemoteAgentImpl)";

	private Map monitorMap = new HashMap();

	int fileHandle = 0;
	private Map fileNameMap = Collections.synchronizedMap(new HashMap());
	private Map fileMap = Collections.synchronizedMap(new HashMap());

	private synchronized long nextFileHandle() {
		return ++this.fileHandle;
	}

	public RemoteAgentImpl() throws Exception {
		Map serverMap = getServerMap();
		Object[] servers = serverMap.values().toArray();
		for (int i = 0; i < servers.length; i++) {
			ServerInfo info = (ServerInfo) servers[i];
			try {
				getServer(info.server);
				if (Config.getServerConfig(info.server, false).getStr("autoRestart", "").equals("1"))
					restartMonitor(info.server);
				else
					stopMonitor(info.server);
			} catch (Exception localException) {
			}
		}
	}

	public void startServer(String server) throws Exception {
		boolean started = false;
		try {
			getServer(server);
			started = true;
		} catch (Exception localException) {
		}

		if (started)
			throw new RemoteException("服务先前已经启动了.如果有异常，请先停止服务再启动。");
		logger.info("begin to start TCM Server ");

		String shell = "";

		boolean isWindows = System.getProperty("os.name").indexOf("Windows") >= 0;

		if (isWindows)
			shell = shell + "cmd.exe /C start  startServer.bat " + server;
		else {
			shell = shell + "./startServer.sh " + server;
		}
		
		logger.info(shell);
		Process proc = Runtime.getRuntime().exec(shell);

		StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "");

		StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "");

		errorGobbler.start();
		outputGobbler.start();
		try {
			proc.wait(5000L);
		} catch (Exception localException1) {
		}

		if (!startTest(server))
			throw new Exception("启动失败.");

		this.processMap.put(server, proc);
	}

	private boolean startTest(String server) {
		int n = 0;

		while (n++ < 10) {
			try {
				getServer(server);
				return true;
			} catch (Exception e) {
				try {
					Thread.sleep(1000L);
				} catch (Exception localException1) {
				}
			}
		}

		return false;
	}

	private boolean stopTest(String server) {
		int n = 0;
		boolean stop = true;
		try {
			while (true) {
				getServer(server);
				if (n++ >= 10) {
					stop = false;
					break;
				}
				try {
					Thread.sleep(400L);
				} catch (Exception localException) {
				}

			}

		} catch (Exception localException1) {
		}

		return stop;
	}

	public void stopServer(String server) throws Exception {
		stopServerAndMonitor(server, true);
	}

	public void stopServerAndMonitor(String server, boolean stopMonitor) throws Exception {
		logger.info("bein to stop TCM Server ");
		String msg = "";

		if (stopMonitor)
			stopMonitor(server);

		try {
			Shutdown.stopServer(server);
		} catch (Exception e) {
			logger.error(e);
			msg = msg + e;
		}

		logger.info("bein to test state ");

		if (!stopTest(server)) {
			try {
				Process proc = (Process) this.processMap.get(server);
				if (proc != null) {
					logger.info("begin to stop process");
					proc.destroy();
					this.processMap.remove(server);
				}

			} catch (Exception e) {
				logger.error(e);
			}

		}

		if (!stopTest(server)) {
			logger.error("关闭服务器失败." + msg);
			throw new RemoteException("关闭服务器失败." + msg);
		}

		logger.info("Stop successfully ");
	}

	private void stopMonitor(String server, int seconds) throws Exception {
		Thread monitorThread = (Thread) this.monitorMap.get(server);
		logger.info("begin to stop monitor.");
		if (!monitorAlive(server))
			return;

		logger.info("begin to stop monitor.");
		try {
			try {
				monitorThread.stop();
			} finally {
				this.monitorMap.remove(server);
			}
			logger.info("begin to destory monitor.");
			int n = 0;
			while (n++ < seconds) {
				if (!monitorAlive(server))
					break;
				Thread.sleep(1000L);
			}

			logger.info("stopping monitor ...");
			if (monitorAlive(server))
				monitorThread.destroy();
			logger.info("stopped monitor");
		} catch (Exception e) {
			logger.error("stopMonitor():", e);
		}
		logger.info("end stop monitor.");
	}

	public synchronized void restartServer(String server, boolean keepState) throws Exception {
		String msg = "开始进行重启服务器操作" + ENTER;
		try {
			Map map = new HashMap();
			if (keepState) {
				try {
					msg = msg + "开始连接服务器" + ENTER;
					RemoteServer remoteServer = getServer(server);

					msg = msg + "开始获取并保存任务状态" + ENTER;
					Object[] tasks = remoteServer.getTasks().values().toArray();
					for (int i = 0; i < tasks.length; i++) {
						RemoteTask task = (RemoteTask) tasks[i];
						if (task.isStarted())
							map.put(task.getTaskID(), "");
					}
					msg = msg + "成功保存任务状态" + ENTER;
				} catch (Exception e) {
					msg = "连接服务器获取任务失败，可能服务器没有启动:" + e + ENTER;
					logger.error("连接服务器获取任务失败，可能服务器没有启动", e);
				}
			}

			msg = msg + "开始停止服务器" + ENTER;
			stopServer(server);
			msg = msg + "开始启动服务器" + ENTER;
			startServer(server);

			if (keepState) {
				msg = msg + "开始连接服务器" + ENTER;
				RemoteServer remoteServer = getServer(server);
				msg = msg + "开始恢复任务状态" + ENTER;
				Object[] tasks = remoteServer.getTasks().values().toArray();
				for (int i = 0; i < tasks.length; i++) {
					RemoteTask task = (RemoteTask) tasks[i];
					if (map.get(task.getTaskID()) != null) {
						if (!task.isStarted())
							task.start();

					} else if (task.isStarted())
						task.stop();

				}

				msg = msg + "成功恢复任务状态" + ENTER;
			}
			logger.info(msg);
		} catch (Exception e) {
			logger.error(msg, e);
			throw new Exception(msg + e);
		}
	}

	public Map getServerConfig(String server, boolean original) throws Exception {
		Map map = new HashMap();
		Resource r = Config.getServerConfig(server, original);
		map.put("autoRestart", r.getString("autoRestart"));
		map.put("monitorInterval", r.getString("monitorInterval"));
		map.put("serverPort", r.getString("serverPort"));
		map.put("monitorPort", r.getString("monitorPort"));
		map.put("memo", Config.getServerMemo(server));
		return map;
	}

	public void setServerConfig(String server, Map map) throws Exception {
		Config.setServerConfig(server, map);
	}

	public void addServer(String server, Map map) throws Exception {
		String dir = "../servers/" + server;
		File f = new File(dir);
		if (f.exists())
			throw new Exception("应用服务器" + server + "已经存在，请用别的名称");
		if (!f.mkdirs())
			throw new Exception("无法创建目录" + f.getAbsolutePath());
		new File(dir + "/common/classes").mkdirs();
		new File(dir + "/common/lib").mkdirs();
		new File(dir + "/conf").mkdirs();
		new File(dir + "/logs").mkdirs();
		new File(dir + "/plugins/plugins").mkdirs();
		new File(dir + "/plugins/classes").mkdirs();
		new File(dir + "/plugins/lib").mkdirs();
		new File(dir + "/working").mkdirs();

		Config.setServerConfig(server, map);
	}

	private boolean monitorAlive(String server) {
		Thread monitorThread = (Thread) this.monitorMap.get(server);
		return (monitorThread != null) && (monitorThread.isAlive());
	}

	public void restartMonitor(String server) throws Exception {
		try {
			stopMonitor(server);
		} finally {
			startMonitor(server);
		}
	}

	public void startMonitor(String server) throws Exception {
		if (!monitorAlive(server)) {
			logger.info("Begint to start monitor");

			int port = Config.getServerConfig(server, true).getInt("monitorPort", -1);

			if (port < 0)
				return;

			Thread monitorThread = new Thread(new MonitorThread(this, server, port));

			monitorThread.start();
			this.monitorMap.put(server, monitorThread);
		}
	}

	public void stopMonitor(String server) throws Exception {
		stopMonitor(server, 5);
	}

	public RemoteServer getServer(String server) throws Exception {
		Resource r = Config.getServerConfig(server, true);

		RemoteServer rs = ServerTool.getServer(r.getInt("serverPort", -1));

		String name = rs.getServerName();
		if (!name.equals(server))
			throw new Exception("服务器尚未启动");
		return rs;
	}

	public String getServerHost(String server) throws RemoteException {
		return Agent.host;
	}

	public String getServerPort(String server) throws RemoteException {
		try {
			Resource r = Config.getServerConfig(server, true);

			String port = "1099";
			if (r != null)
				return r.getStr("serverPort", port);
			return port;
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	public String getMonitorPort(String server) throws Exception {
		return Config.getServerConfig(server, true).getStr("monitorPort", "-1");
	}

	public long openFile(String filename, boolean write) throws RemoteException, IOException {
		logger.info("open file " + filename);
		new File(filename).getParentFile().mkdirs();
		Object stream = null;

		if (write)
			stream = new FileOutputStream(filename);
		else
			stream = new FileInputStream(filename);

		long ret = nextFileHandle();
		this.fileMap.put(new Long(ret), stream);
		this.fileNameMap.put(new Long(ret), filename);
		return ret;
	}

	public void uploadFile(byte[] bs, String toFilename, long lastModify) throws Exception {
		File f = new File(toFilename);
		f.getParentFile().mkdirs();
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(f);
			os.write(bs);
		} finally {
			if (os != null)
				os.close();

		}

		if (lastModify >= 0L)
			f.setLastModified(lastModify);
	}

	public Map findPulginByClass(byte[] classBytes, boolean isJar) throws Exception {
		System.out.println("classBytes=" + classBytes);
		ClassLoaderByClass cl = new ClassLoaderByClass(classBytes, isJar, Task.class);
		Class c = cl.loadClass();

		System.out.println(c);
		Plugin plugin = new Plugin();
		plugin.setId(c.getName());
		plugin.setPluginName((String) c.getMethod("getName", null).invoke(null, null));
		plugin.setPluginMemo((String) c.getMethod("getMemo", null).invoke(null, null));
		plugin.setEntryClass(c.getName());
		plugin.setVersion((String) c.getMethod("getVersion", null).invoke(null, null));
		Map map = new HashMap();
		map.put("name", plugin.getPluginName());
		map.put("id", plugin.getId());
		map.put("memo", plugin.getPluginMemo());
		map.put("version", plugin.getVersion());
		map.put("entryClass", plugin.getEntryClass());
		return map;
	}

	public byte[] downloadFile(String filename) throws Exception {
		FileInputStream is = null;
		try {
			is = new FileInputStream(filename);
			byte[] bs = new byte[is.available()];
			is.read(bs);
			return bs;
		} finally {
			if (is != null)
				is.close();
		}
	}

	public byte[] downloadLogFile(String filename) throws Exception {
		return downloadFile(Agent.logPath + "/" + filename);
	}

	public void writeFile(long handle, byte[] bs) throws RemoteException, IOException {
		OutputStream os = (OutputStream) this.fileMap.get(new Long(handle));
		os.write(bs);
	}

	public void writeFile(long handle, byte[] bs, int off, int len) throws RemoteException, IOException {
		OutputStream os = (OutputStream) this.fileMap.get(new Long(handle));
		os.write(bs, off, len);
	}

	public void closeFile(long handle, long lastModify) throws RemoteException, IOException {
		try {
			Object o = this.fileMap.get(new Long(handle));
			try {
				o.getClass().getMethod("close", null).invoke(o, null);
			} catch (Exception localException) {
			}
			logger.info("closeFile handle : " + handle);
			if (lastModify >= 0L)
				new File((String) this.fileNameMap.get(new Long(handle))).setLastModified(lastModify);
		} finally {
			this.fileMap.remove(new Long(handle));
			this.fileNameMap.remove(new Long(handle));
		}
	}

	public byte[] readFile(long handle, int byteCount) throws RemoteException, IOException {
		InputStream is = (InputStream) this.fileMap.get(new Long(handle));
		byte[] bs = new byte[byteCount];
		int n = is.read(bs);
		if (n == -1)
			return null;
		if (n == byteCount)
			return bs;
		return Arrays.copyOf(bs, n);
	}

	public void exit() throws RemoteException {
		System.exit(0);
	}


	public void setName(String name) throws RemoteException {
		this.name = name;
	}

	public String getName() throws RemoteException {
		return this.name;
	}

	public String[] listFiles(String path) throws RemoteException {
		return new File(path).list();
	}

	public String[] listLogFiles(String path, String name) throws RemoteException {
		File[] fs = new LogA(Agent.logPath + "/" + path, name, "").getFiles();
		String[] ss = new String[fs.length];
		for (int i = 0; i < fs.length; i++)
			ss[i] = (fs[i].length() + "=" + fs[i].getName());
		return ss;
	}

	public String zipLogFile(String path, String name) throws Exception {
		File[] fs = new LogA(Agent.logPath + "/" + path, name, "").getFiles();

		File temp = new File(Convert.toString(System.getProperty("java.io.tmpdir"), "../temp"));
		if (!temp.exists())
			temp.mkdirs();

		File zipFile = new File(temp, nextFileHandle() + ".zip");
		logger.info("zip file=" + zipFile);
		zipFile.deleteOnExit();
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
		try {
			byte[] buf = new byte[1024];

			for (int i = 0; i < fs.length; i++) {
				zos.putNextEntry(new ZipEntry(fs[i].getName()));
				InputStream is = new FileInputStream(fs[i]);
				int len;
				while ((len = is.read(buf)) != -1) {
					/* int len; */
					zos.write(buf, 0, len);
					zos.flush();
				}

				is.close();
				zos.closeEntry();
			}
		} finally {
			zos.close();
		}
		return zipFile.getAbsolutePath();
	}

	public Map getServerMap() throws Exception {
		File f = new File("../servers");

		if (!f.exists())
			f.mkdir();

		File[] fs = f.listFiles();
		Map serverMap = new HashMap();
		for (int i = 0; i < fs.length; i++) {
			if (fs[i].isDirectory()) {
				ServerInfo info = new ServerInfo();
				info.server = fs[i].getName();
				info.memo = Config.getServerMemo(info.server);
				Resource r = Config.getServerConfig(info.server, false);
				info.port = r.getInt("serverPort", -1);
				info.monitorPort = r.getInt("monitorPort", -1);
				info.monitorInterval = r.getInt("monitorInterval", -1);
				if (info.port == -1) {
					System.out.println("server port in server 'servers/" + info.server
							+ "' is not correct.(Agent.getServerMap())");
				} else {
					serverMap.put(info.server, info);
				}
			}

		}

		return serverMap;
	}

	public String getTCMBase() throws Exception {
		return new File("..").getCanonicalPath();
	}

	public boolean serverStarted(String server) throws Exception {
		try {
			getServer(server);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public void deleteFile(String path) throws Exception {
		new File(path).delete();
	}

	public Date getCurrentDate() throws RemoteException {
		return new Date();
	}

	public long getServerLogSize(String server, String name) throws RemoteException {
		LogA log = new LogA(Agent.logPath + "/" + server + "/", name, Config.getCharset(Server.SERVER));
		return log.getTotalSize();
	}

	public String getServerLog(String server, String name, int pageNo, int pageSize) throws RemoteException {
		LogA log = new LogA(Agent.logPath + "/" + server + "/", name, Config.getCharset(Server.SERVER));
		StringWriter w = new StringWriter(pageSize);
		try {
			log.displayReverse(w, -1, pageNo, pageSize);
			return w.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
	
	public static void main(String[] args) throws Exception {
		
		Process process = Runtime.getRuntime().exec("cmd.exe /c start  bin/startServer.bat");
		
		//Process process = Runtime.getRuntime().exec("cmd.exe /c start  F:\\apache-tomcat-7.0.72\\bin\\startup.bat");

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String s;
		while ((s = bufferedReader.readLine()) != null) {
			System.out.println(">" + s);
		}
		System.out.println("end");
		
		
		//RemoteAgent remoteAgent = new  RemoteAgentImpl();
		
		//remoteAgent.startServer("");
	}
}