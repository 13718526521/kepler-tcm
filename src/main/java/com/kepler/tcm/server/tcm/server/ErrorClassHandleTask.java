package com.kepler.tcm.server.tcm.server;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorClassHandleTask extends Task {
	String error = null;
	String className = null;

	public String getError() {
		return "加载任务类" + this.className + "出错，原因：" + this.error + ". 将用ErrorClassHandleTask来接管.";
	}

	public void setError(String className, Throwable e) {
		this.className = className;
		StringWriter w = null;
		try {
			w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			this.error = w.toString();
		} finally {
			try {
				w.close();
			} catch (Exception localException) {
			}
		}
	}

	public void work() {
		alert("无法加载类" + this.className);
		alert("错误原因:" + this.error);

		info("无法加载类" + this.className);
		info("错误原因:" + this.error);
	}
}