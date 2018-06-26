package com.kepler.tcm.core.agent;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;

import com.kepler.tcm.core.server.RemoteServer;

public abstract interface RemoteAgent extends Remote {
	
	public abstract String getConnectRMI() throws Exception;
	
	public abstract Map getServerMap() throws Exception;

	public abstract void startServer(String paramString) throws Exception;

	public abstract void stopServer(String paramString) throws Exception;

	/**
	 * 获取远程服务实例
	 * @param serverName  服务名
	 * @return
	 * @throws Exception
	 */
	public abstract RemoteServer getServer(String serverName) throws Exception;

	public abstract void restartServer(String paramString, boolean paramBoolean)
			throws Exception;

	public abstract boolean serverStarted(String paramString) throws Exception;

	public abstract String getServerHost(String paramString)
			throws RemoteException;

	public abstract String getServerPort(String paramString)
			throws RemoteException;

	public abstract String getMonitorPort(String paramString) throws Exception;

	public abstract void stopMonitor(String paramString) throws Exception;

	public abstract void startMonitor(String paramString) throws Exception;

	public abstract void restartMonitor(String paramString) throws Exception;

	/**
	 * 获取服务配置信息
	 * @param serverName  服务名
	 * @param original   是否从源文件获取，false
	 * @return
	 * @throws Exception
	 */
	public abstract Map getServerConfig(String serverName, boolean isOriginal)
			throws Exception;

	public abstract void setServerConfig(String paramString, Map paramMap)
			throws Exception;
	/**
	 * 添加服务，不可重名
	 * @param serverName 服务名
	 * @param paramMap  服务基本属性集
	 * @throws Exception
	 */
	public abstract void addServer(String serverName, Map paramMap)
			throws Exception;

	public abstract long openFile(String paramString, boolean paramBoolean)
			throws RemoteException, IOException;

	public abstract void writeFile(long paramLong, byte[] paramArrayOfByte)
			throws RemoteException, IOException;

	public abstract void writeFile(long paramLong, byte[] paramArrayOfByte,
			int paramInt1, int paramInt2) throws RemoteException, IOException;

	public abstract void closeFile(long paramLong1, long paramLong2)
			throws RemoteException, IOException;

	public abstract byte[] readFile(long paramLong, int paramInt)
			throws RemoteException, IOException;

	public abstract void exit() throws RemoteException;

	public abstract void setName(String paramString) throws RemoteException;

	public abstract String getName() throws RemoteException;

	public abstract void uploadFile(byte[] paramArrayOfByte,
			String paramString, long paramLong) throws Exception;

	public abstract byte[] downloadFile(String paramString) throws Exception;

	public abstract byte[] downloadLogFile(String paramString) throws Exception;

	public abstract Map findPulginByClass(byte[] paramArrayOfByte,
			boolean paramBoolean) throws Exception;

	public abstract String[] listFiles(String paramString)
			throws RemoteException;

	public abstract void deleteFile(String paramString) throws Exception;

	public abstract String getTCMBase() throws Exception;

	public abstract String[] listLogFiles(String paramString1,
			String paramString2) throws RemoteException;

	public abstract Date getCurrentDate() throws RemoteException;

	public abstract long getServerLogSize(String paramString1,
			String paramString2) throws RemoteException;

	public abstract String getServerLog(String paramString1,
			String paramString2, int paramInt1, int paramInt2)
			throws RemoteException;

	public abstract String zipLogFile(String paramString1, String paramString2)
			throws Exception;
}