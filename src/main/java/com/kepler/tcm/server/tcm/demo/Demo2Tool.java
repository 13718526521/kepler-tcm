package com.kepler.tcm.server.tcm.demo;

import com.kepler.tcm.server.tcm.server.Task;

public class Demo2Tool {
	public static void tool1() {
		Task.getCurrent().info("tool1 execute");
	}

	public void tool2() {
		Task.getCurrent().info("tool2 execute");
	}
}