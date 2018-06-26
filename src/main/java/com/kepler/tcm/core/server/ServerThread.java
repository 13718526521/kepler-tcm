package com.kepler.tcm.core.server;

public class ServerThread extends Thread {
	public static long minMem = 0L;

	public static long maxMem = 0L;

	private final long HOUR = 3600000L;
	
	private long lastTime = 0L;

	public void run() {
		while (true) {
			long nFreeMemory = Runtime.getRuntime().freeMemory();
			long nTotalMemory = Runtime.getRuntime().totalMemory();
			long nUseMemory = nTotalMemory - nFreeMemory;
			try {
				long now = System.currentTimeMillis();
				if (now - this.lastTime > 7200000L) {
					this.lastTime = now;
					minMem = nUseMemory;
				} else {
					if (nUseMemory < minMem)
						minMem = nUseMemory;
					if (nUseMemory > maxMem)
						maxMem = nUseMemory;
				}

				Thread.sleep(3000L);
			} catch (Exception localException) {
			}
		}
	}
}