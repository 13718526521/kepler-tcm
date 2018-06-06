package com.kepler.tcm.server.tcm.demo;

import com.kepler.tcm.server.tcm.server.DebugTask;
import com.kepler.tcm.server.tcm.server.Task;

public class Demo2 extends Task {
	final String[] params = { "ftp", "user", "password", "port" };

	public void work() {
		String function = getClass().getName() + ".work()";

		info("第" + getExecuteNum() + "次运行任务");
		try {
			info("this is a debug");
			info("renew class");
			info("Ftp host is " + getConfigValue("ftp"));
			this.message.incSucc();

			Demo2Tool.tool1();
			getCounter("abc");
		} catch (Exception e) {
			error("Exception from " + function, e);
			this.message.incFail();
		}
		info("执行结束");
	}

	public static void main(String[] args) throws Exception {
		DebugTask task = new DebugTask();
		task.setConfig("config1");

		task.run(-1);
	}
}