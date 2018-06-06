package com.kepler.tcm.server.tcm.agent;

import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;
import java.util.Date;
import java.util.Map;

import com.kepler.tcm.server.tcm.server.RemoteServer;

public final class RemoteAgentImpl_Stub extends RemoteStub implements RemoteAgent, Remote {
	private static final long serialVersionUID = 2L;
	private static Method $method_addServer_0;
	private static Method $method_closeFile_1;
	private static Method $method_deleteFile_2;
	private static Method $method_downloadFile_3;
	private static Method $method_downloadLogFile_4;
	private static Method $method_exit_5;
	private static Method $method_findPulginByClass_6;
	private static Method $method_getCurrentDate_7;
	private static Method $method_getMonitorPort_8;
	private static Method $method_getName_9;
	private static Method $method_getServer_10;
	private static Method $method_getServerConfig_11;
	private static Method $method_getServerHost_12;
	private static Method $method_getServerLog_13;
	private static Method $method_getServerLogSize_14;
	private static Method $method_getServerMap_15;
	private static Method $method_getServerPort_16;
	private static Method $method_getTCMBase_17;
	private static Method $method_listFiles_18;
	private static Method $method_listLogFiles_19;
	private static Method $method_openFile_20;
	private static Method $method_readFile_21;
	private static Method $method_restartMonitor_22;
	private static Method $method_restartServer_23;
	private static Method $method_serverStarted_24;
	private static Method $method_setName_25;
	private static Method $method_setServerConfig_26;
	private static Method $method_startMonitor_27;
	private static Method $method_startServer_28;
	private static Method $method_stopMonitor_29;
	private static Method $method_stopServer_30;
	private static Method $method_uploadFile_31;
	private static Method $method_writeFile_32;
	private static Method $method_writeFile_33;
	private static Method $method_zipLogFile_34;

	static {
		try {
			$method_addServer_0 = Remote.class.getMethod("addServer", new Class[] { String.class, Map.class });
			$method_closeFile_1 = Remote.class.getMethod("closeFile", new Class[] { Long.TYPE, Long.TYPE });
			$method_deleteFile_2 = Remote.class.getMethod("deleteFile", new Class[] { String.class });
			$method_downloadFile_3 = Remote.class.getMethod("downloadFile", new Class[] { String.class });
			$method_downloadLogFile_4 = Remote.class.getMethod("downloadLogFile", new Class[] { String.class });
			$method_exit_5 = Remote.class.getMethod("exit", new Class[0]);
			$method_findPulginByClass_6 = Remote.class.getMethod("findPulginByClass",
					new Class[] { new byte[0].getClass(), Boolean.TYPE });
			$method_getCurrentDate_7 = Remote.class.getMethod("getCurrentDate", new Class[0]);
			$method_getMonitorPort_8 = Remote.class.getMethod("getMonitorPort", new Class[] { String.class });
			$method_getName_9 = Remote.class.getMethod("getName", new Class[0]);
			$method_getServer_10 = Remote.class.getMethod("getServer", new Class[] { String.class });
			$method_getServerConfig_11 = Remote.class.getMethod("getServerConfig",
					new Class[] { String.class, Boolean.TYPE });
			$method_getServerHost_12 = Remote.class.getMethod("getServerHost", new Class[] { String.class });
			$method_getServerLog_13 = Remote.class.getMethod("getServerLog",
					new Class[] { String.class, String.class, Integer.TYPE, Integer.TYPE });
			$method_getServerLogSize_14 = Remote.class.getMethod("getServerLogSize",
					new Class[] { String.class, String.class });
			$method_getServerMap_15 = Remote.class.getMethod("getServerMap", new Class[0]);
			$method_getServerPort_16 = Remote.class.getMethod("getServerPort", new Class[] { String.class });
			$method_getTCMBase_17 = Remote.class.getMethod("getTCMBase", new Class[0]);
			$method_listFiles_18 = Remote.class.getMethod("listFiles", new Class[] { String.class });
			$method_listLogFiles_19 = Remote.class.getMethod("listLogFiles",
					new Class[] { String.class, String.class });
			$method_openFile_20 = Remote.class.getMethod("openFile", new Class[] { String.class, Boolean.TYPE });
			$method_readFile_21 = Remote.class.getMethod("readFile", new Class[] { Long.TYPE, Integer.TYPE });
			$method_restartMonitor_22 = Remote.class.getMethod("restartMonitor", new Class[] { String.class });
			$method_restartServer_23 = Remote.class.getMethod("restartServer",
					new Class[] { String.class, Boolean.TYPE });
			$method_serverStarted_24 = Remote.class.getMethod("serverStarted", new Class[] { String.class });
			$method_setName_25 = Remote.class.getMethod("setName", new Class[] { String.class });
			$method_setServerConfig_26 = Remote.class.getMethod("setServerConfig",
					new Class[] { String.class, Map.class });
			$method_startMonitor_27 = Remote.class.getMethod("startMonitor", new Class[] { String.class });
			$method_startServer_28 = Remote.class.getMethod("startServer", new Class[] { String.class });
			$method_stopMonitor_29 = Remote.class.getMethod("stopMonitor", new Class[] { String.class });
			$method_stopServer_30 = Remote.class.getMethod("stopServer", new Class[] { String.class });
			$method_uploadFile_31 = Remote.class.getMethod("uploadFile",
					new Class[] { new byte[0].getClass(), String.class, Long.TYPE });
			$method_writeFile_32 = Remote.class.getMethod("writeFile",
					new Class[] { Long.TYPE, new byte[0].getClass() });
			$method_writeFile_33 = Remote.class.getMethod("writeFile",
					new Class[] { Long.TYPE, new byte[0].getClass(), Integer.TYPE, Integer.TYPE });
			$method_zipLogFile_34 = Remote.class.getMethod("zipLogFile", new Class[] { String.class, String.class });
		} catch (NoSuchMethodException localNoSuchMethodException) {
			throw new NoSuchMethodError("stub class initialization failed");
		}
	}

	public RemoteAgentImpl_Stub(RemoteRef paramRemoteRef) {
		super(paramRemoteRef);
	}

	public void addServer(String paramString, Map paramMap) throws Exception {
		this.ref.invoke(this, $method_addServer_0, new Object[] { paramString, paramMap }, 5350409138670980939L);
	}

	public void closeFile(long paramLong1, long paramLong2) throws IOException, RemoteException {
		try {
			this.ref.invoke(this, $method_closeFile_1, new Object[] { new Long(paramLong1), new Long(paramLong2) },
					5817349055440156624L);
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (IOException localIOException) {
			throw localIOException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public void deleteFile(String paramString) throws Exception {
		this.ref.invoke(this, $method_deleteFile_2, new Object[] { paramString }, -8487335724984782172L);
	}

	public byte[] downloadFile(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_downloadFile_3, new Object[] { paramString },
				1307062400137940292L);
		return (byte[]) localObject;
	}

	public byte[] downloadLogFile(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_downloadLogFile_4, new Object[] { paramString },
				2398680848798951661L);
		return (byte[]) localObject;
	}

	public void exit() throws RemoteException {
		try {
			this.ref.invoke(this, $method_exit_5, null, -6307240473358936408L);
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public Map findPulginByClass(byte[] paramArrayOfByte, boolean paramBoolean) throws Exception {
		Object localObject = this.ref.invoke(this, $method_findPulginByClass_6,
				new Object[] { paramArrayOfByte, paramBoolean ? Boolean.TRUE : Boolean.FALSE }, 7301442800671634801L);
		return (Map) localObject;
	}

	public Date getCurrentDate() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getCurrentDate_7, null, 8524281267519615889L);
			return (Date) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public String getMonitorPort(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_getMonitorPort_8, new Object[] { paramString },
				-2506985985135105054L);
		return (String) localObject;
	}

	public String getName() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getName_9, null, 6317137956467216454L);
			return (String) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public RemoteServer getServer(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_getServer_10, new Object[] { paramString },
				7097915215387398633L);
		return (RemoteServer) localObject;
	}

	public Map getServerConfig(String paramString, boolean paramBoolean) throws Exception {
		Object localObject = this.ref.invoke(this, $method_getServerConfig_11,
				new Object[] { paramString, paramBoolean ? Boolean.TRUE : Boolean.FALSE }, -4746013014933943076L);
		return (Map) localObject;
	}

	public String getServerHost(String paramString) throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getServerHost_12, new Object[] { paramString },
					7138243759504422567L);
			return (String) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public String getServerLog(String paramString1, String paramString2, int paramInt1, int paramInt2)
			throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getServerLog_13,
					new Object[] { paramString1, paramString2, new Integer(paramInt1), new Integer(paramInt2) },
					688497645025834102L);
			return (String) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public long getServerLogSize(String paramString1, String paramString2) throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getServerLogSize_14,
					new Object[] { paramString1, paramString2 }, -6724002822625899274L);
			return ((Long) localObject).longValue();
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public Map getServerMap() throws Exception {
		Object localObject = this.ref.invoke(this, $method_getServerMap_15, null, 520997952498713335L);
		return (Map) localObject;
	}

	public String getServerPort(String paramString) throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getServerPort_16, new Object[] { paramString },
					-3735128507609990226L);
			return (String) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public String getTCMBase() throws Exception {
		Object localObject = this.ref.invoke(this, $method_getTCMBase_17, null, -4717582158179190230L);
		return (String) localObject;
	}

	public String[] listFiles(String paramString) throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_listFiles_18, new Object[] { paramString },
					-3573926815560094645L);
			return (String[]) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public String[] listLogFiles(String paramString1, String paramString2) throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_listLogFiles_19,
					new Object[] { paramString1, paramString2 }, 7218581622923558929L);
			return (String[]) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public long openFile(String paramString, boolean paramBoolean) throws IOException, RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_openFile_20,
					new Object[] { paramString, paramBoolean ? Boolean.TRUE : Boolean.FALSE }, -3451702613849906218L);
			return ((Long) localObject).longValue();
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (IOException localIOException) {
			throw localIOException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public byte[] readFile(long paramLong, int paramInt) throws IOException, RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_readFile_21,
					new Object[] { new Long(paramLong), new Integer(paramInt) }, -9142021279456322351L);
			return (byte[]) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (IOException localIOException) {
			throw localIOException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public void restartMonitor(String paramString) throws Exception {
		this.ref.invoke(this, $method_restartMonitor_22, new Object[] { paramString }, -2116571263058790120L);
	}

	public void restartServer(String paramString, boolean paramBoolean) throws Exception {
		this.ref.invoke(this, $method_restartServer_23,
				new Object[] { paramString, paramBoolean ? Boolean.TRUE : Boolean.FALSE }, -5362147397373902180L);
	}

	public boolean serverStarted(String paramString) throws Exception {
		Object localObject = this.ref.invoke(this, $method_serverStarted_24, new Object[] { paramString },
				5521280347262413931L);
		return ((Boolean) localObject).booleanValue();
	}

	public void setName(String paramString) throws RemoteException {
		try {
			this.ref.invoke(this, $method_setName_25, new Object[] { paramString }, 1344297395548290975L);
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public void setServerConfig(String paramString, Map paramMap) throws Exception {
		this.ref.invoke(this, $method_setServerConfig_26, new Object[] { paramString, paramMap },
				-7049566181386598633L);
	}

	public void startMonitor(String paramString) throws Exception {
		this.ref.invoke(this, $method_startMonitor_27, new Object[] { paramString }, -169373492934197058L);
	}

	public void startServer(String paramString) throws Exception {
		this.ref.invoke(this, $method_startServer_28, new Object[] { paramString }, 4025584258759146969L);
	}

	public void stopMonitor(String paramString) throws Exception {
		this.ref.invoke(this, $method_stopMonitor_29, new Object[] { paramString }, 6618901415252526845L);
	}

	public void stopServer(String paramString) throws Exception {
		this.ref.invoke(this, $method_stopServer_30, new Object[] { paramString }, 6001002318804188470L);
	}

	public void uploadFile(byte[] paramArrayOfByte, String paramString, long paramLong) throws Exception {
		this.ref.invoke(this, $method_uploadFile_31,
				new Object[] { paramArrayOfByte, paramString, new Long(paramLong) }, 3674884641949260794L);
	}

	public void writeFile(long paramLong, byte[] paramArrayOfByte) throws IOException, RemoteException {
		try {
			this.ref.invoke(this, $method_writeFile_32, new Object[] { new Long(paramLong), paramArrayOfByte },
					-8031941504210772400L);
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (IOException localIOException) {
			throw localIOException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public void writeFile(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
			throws IOException, RemoteException {
		try {
			this.ref.invoke(this, $method_writeFile_33, new Object[] { new Long(paramLong), paramArrayOfByte,
					new Integer(paramInt1), new Integer(paramInt2) }, -6685262049504457425L);
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (IOException localIOException) {
			throw localIOException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public String zipLogFile(String paramString1, String paramString2) throws Exception {
		Object localObject = this.ref.invoke(this, $method_zipLogFile_34, new Object[] { paramString1, paramString2 },
				-2462476783310187266L);
		return (String) localObject;
	}
}