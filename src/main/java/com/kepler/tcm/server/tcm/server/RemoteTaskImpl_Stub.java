package com.kepler.tcm.server.tcm.server;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;
import java.util.HashMap;

public final class RemoteTaskImpl_Stub extends RemoteStub implements RemoteTask, Remote {
	private static final long serialVersionUID = 2L;
	private static Method $method_clearAlert_0;
	private static Method $method_forceStop_1;
	private static Method $method_getAlert_2;
	private static Method $method_getErrorClassInfo_3;
	private static Method $method_getExecuteNum_4;
	private static Method $method_getFirstRuntime_5;
	private static Method $method_getLastRuntime_6;
	private static Method $method_getTaskConfig_7;
	private static Method $method_getTaskID_8;
	private static Method $method_getTaskInfo_9;
	private static Method $method_getTaskLog_10;
	private static Method $method_getTaskLog2_11;
	private static Method $method_getTaskMessage_12;
	private static Method $method_getTaskName_13;
	private static Method $method_getTaskProperty_14;
	private static Method $method_getTotalLogSize_15;
	private static Method $method_isDisabled_16;
	private static Method $method_isStarted_17;
	private static Method $method_onAlert_18;
	private static Method $method_saveConfigProperty_19;
	private static Method $method_start_20;
	private static Method $method_stop_21;

	static {
		try {
			$method_clearAlert_0 = Remote.class.getMethod("clearAlert", new Class[0]);
			$method_forceStop_1 = Remote.class.getMethod("forceStop", new Class[0]);
			$method_getAlert_2 = Remote.class.getMethod("getAlert", new Class[0]);
			$method_getErrorClassInfo_3 = Remote.class.getMethod("getErrorClassInfo", new Class[0]);
			$method_getExecuteNum_4 = Remote.class.getMethod("getExecuteNum", new Class[0]);
			$method_getFirstRuntime_5 = Remote.class.getMethod("getFirstRuntime", new Class[0]);
			$method_getLastRuntime_6 = Remote.class.getMethod("getLastRuntime", new Class[0]);
			$method_getTaskConfig_7 = Remote.class.getMethod("getTaskConfig", new Class[0]);
			$method_getTaskID_8 = Remote.class.getMethod("getTaskID", new Class[0]);
			$method_getTaskInfo_9 = Remote.class.getMethod("getTaskInfo", new Class[0]);
			$method_getTaskLog_10 = Remote.class.getMethod("getTaskLog", new Class[0]);
			$method_getTaskLog2_11 = Remote.class.getMethod("getTaskLog2",
					new Class[] { String.class, Integer.TYPE, Integer.TYPE });
			$method_getTaskMessage_12 = Remote.class.getMethod("getTaskMessage", new Class[0]);
			$method_getTaskName_13 = Remote.class.getMethod("getTaskName", new Class[0]);
			$method_getTaskProperty_14 = Remote.class.getMethod("getTaskProperty", new Class[0]);
			$method_getTotalLogSize_15 = Remote.class.getMethod("getTotalLogSize", new Class[] { String.class });
			$method_isDisabled_16 = Remote.class.getMethod("isDisabled", new Class[0]);
			$method_isStarted_17 = Remote.class.getMethod("isStarted", new Class[0]);
			$method_onAlert_18 = Remote.class.getMethod("onAlert", new Class[0]);
			$method_saveConfigProperty_19 = Remote.class.getMethod("saveConfigProperty", new Class[] { HashMap.class });
			$method_start_20 = Remote.class.getMethod("start", new Class[0]);
			$method_stop_21 = Remote.class.getMethod("stop", new Class[0]);
		} catch (NoSuchMethodException localNoSuchMethodException) {
			throw new NoSuchMethodError("stub class initialization failed");
		}
	}

	public RemoteTaskImpl_Stub(RemoteRef paramRemoteRef) {
		super(paramRemoteRef);
	}

	public void clearAlert() throws RemoteException {
		try {
			this.ref.invoke(this, $method_clearAlert_0, null, -7977121821399758261L);
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public void forceStop() throws Exception {
		this.ref.invoke(this, $method_forceStop_1, null, 3898279908201319795L);
	}

	public String getAlert() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getAlert_2, null, -8787226571790432169L);
			return (String) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public String getErrorClassInfo() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getErrorClassInfo_3, null, 3868207357873192780L);
			return (String) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public long getExecuteNum() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getExecuteNum_4, null, -8567089318970514630L);
			return ((Long) localObject).longValue();
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public String getFirstRuntime() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getFirstRuntime_5, null, 1391689121473016596L);
			return (String) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public String getLastRuntime() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getLastRuntime_6, null, -8269967908504689985L);
			return (String) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public String[][] getTaskConfig() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getTaskConfig_7, null, -1909829488210121625L);
			return (String[][]) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public String getTaskID() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getTaskID_8, null, 3995362375840528697L);
			return (String) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public String[][] getTaskInfo() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getTaskInfo_9, null, -8938133787428788684L);
			return (String[][]) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public String[] getTaskLog() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getTaskLog_10, null, -3448146279278211558L);
			return (String[]) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public String getTaskLog2(String paramString, int paramInt1, int paramInt2) throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getTaskLog2_11,
					new Object[] { paramString, new Integer(paramInt1), new Integer(paramInt2) },
					-2493514054950725279L);
			return (String) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public TaskMessage getTaskMessage() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getTaskMessage_12, null, -4781108715084428772L);
			return (TaskMessage) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public String getTaskName() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getTaskName_13, null, 7524383870929212659L);
			return (String) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public HashMap getTaskProperty() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getTaskProperty_14, null, 4590121392113161798L);
			return (HashMap) localObject;
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public long getTotalLogSize(String paramString) throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_getTotalLogSize_15, new Object[] { paramString },
					111808213656039405L);
			return ((Long) localObject).longValue();
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public boolean isDisabled() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_isDisabled_16, null, 662154894340550194L);
			return ((Boolean) localObject).booleanValue();
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public boolean isStarted() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_isStarted_17, null, 5627842141695884950L);
			return ((Boolean) localObject).booleanValue();
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public boolean onAlert() throws RemoteException {
		try {
			Object localObject = this.ref.invoke(this, $method_onAlert_18, null, 1618475655058968448L);
			return ((Boolean) localObject).booleanValue();
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public void saveConfigProperty(HashMap paramHashMap) throws RemoteException {
		try {
			this.ref.invoke(this, $method_saveConfigProperty_19, new Object[] { paramHashMap }, -3575076419056908912L);
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}

	public void start() throws Exception, RemoteException {
		this.ref.invoke(this, $method_start_20, null, -8025343665958530775L);
	}

	public void stop() throws RemoteException {
		try {
			this.ref.invoke(this, $method_stop_21, null, -2856118408655404442L);
		} catch (RuntimeException localRuntimeException) {
			throw localRuntimeException;
		} catch (RemoteException localRemoteException) {
			throw localRemoteException;
		} catch (Exception localException) {
			throw new UnexpectedException("undeclared checked exception", localException);
		}
	}
}