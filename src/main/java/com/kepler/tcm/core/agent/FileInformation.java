package com.kepler.tcm.core.agent;

import java.io.Serializable;

public abstract interface FileInformation extends Serializable {
	public abstract String getName();

	public abstract byte[] getContent();

	public abstract void setInformation(String paramString, byte[] paramArrayOfByte);
}