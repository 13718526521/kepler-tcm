package com.kepler.tcm.core.task;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public abstract interface RemoteTask extends Remote {
	
	public abstract String getConnectRMI() throws Exception;
	
	public abstract void start() throws Exception, RemoteException;

	public abstract void stop() throws RemoteException;

	public abstract void forceStop() throws Exception;

	public abstract String getTaskName() throws RemoteException;

	public abstract String getTaskID() throws RemoteException;

	public abstract boolean isStarted() throws RemoteException;

	public abstract boolean isDisabled() throws RemoteException;

	public abstract TaskMessage getTaskMessage() throws RemoteException;

	public abstract String getFirstRuntime() throws RemoteException;

	public abstract String getLastRuntime() throws RemoteException;

	public abstract long getExecuteNum() throws RemoteException;

	public abstract String[][] getTaskInfo() throws RemoteException;

	public abstract String[][] getTaskConfig() throws RemoteException;

	public abstract HashMap getTaskProperty() throws RemoteException;

	public abstract String[] getTaskLog() throws RemoteException;

	public abstract long getTotalLogSize(String paramString) throws RemoteException;

	public abstract String getTaskLog2(String paramString, int paramInt1, int paramInt2) throws RemoteException;

	public abstract void saveConfigProperty(HashMap paramHashMap) throws RemoteException;

	public abstract String getAlert() throws RemoteException;

	public abstract boolean onAlert() throws RemoteException;

	public abstract void clearAlert() throws RemoteException;

	public abstract String getErrorClassInfo() throws RemoteException;
}