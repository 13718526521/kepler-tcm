package com.kepler.tcm.server.tcm.demo;

import com.kepler.tcm.server.tcm.server.DebugTask;
import com.kepler.tcm.server.tcm.server.Task;

public class Demo1 extends Task {
	final String[][] params = { { "param1", "this is param1" }, { "param2", "about param2" } };

	public void work() {
		info("第" + getExecuteNum() + "次运行任务");
		try {
			info("This is a demo . ");
			info(getConnection());

			info("Sleep 2 seconds. ");

			Thread.sleep(2000L);

			error("this is an error");
		} catch (Exception e) {
			error("error" + e);
		}
	}

	public static String getVersion() {
		return "2.0";
	}

	public static String getName() {
		return "Demo1";
	}

	public static String getMemo() {
		return "This is Demo1";
	}

	public static void main(String[] args) throws Exception {
		DebugTask task = new DebugTask();

		task.run(-1);
	}
}