package com.kepler.tcm.core.agent;

import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kepler.tcm.core.util.Config;

public class MonitorThread implements Runnable {
	private static final Logger logger = LoggerFactory
			.getLogger(MonitorThread.class);

	private String server = null;
	RemoteAgentImpl r = null;
	int monitorPort;

	public MonitorThread(RemoteAgentImpl r, String server, int monitorPort) {
		this.server = server;
		this.r = r;
		this.monitorPort = monitorPort;
	}

	public void run() {
		int monitorInterval = 0;
		try {
			monitorInterval = Config.getServerConfig(this.server, false)
					.getInt("monitorInterval", -1);
		} catch (Exception localException1) {
		}
		if (monitorInterval < 0) {
			logger.error("Interval is not correct! Stop the thread.");
			return;
		}
		logger.info("Begin to monitor " + Agent.host + ":" + this.monitorPort
				+ "@" + this.server);
		while (true) {
			Socket socket = null;
			try {
				boolean live = false;
				try {
					socket = new Socket(Agent.host, this.monitorPort);
					live = socket.isConnected();
				} catch (Exception e) {
					logger.error("请求" + Agent.host + ":" + this.monitorPort
							+ "时异常.", e);
				}

				if (!live) {
					System.out.println("begin to restart server");

					this.r.stopServerAndMonitor(this.server, false);
					this.r.startServer(this.server);
				} else {
					PrintWriter os = new PrintWriter(socket.getOutputStream(),
							true);
					os.println(System.currentTimeMillis());
					os.flush();
					os.close();
				}

			} catch (Exception e) {
				logger.error("Monitoring Exception:", e);

				if (socket != null)
					try {
						socket.close();
					} catch (Exception localException2) {
					}
			} finally {
				if (socket != null)
					try {
						socket.close();
					} catch (Exception localException3) {
					}
			}
			try {
				try {
					monitorInterval = Config
							.getServerConfig(this.server, false).getInt(
									"monitorInterval", -1);
				} catch (Exception localException5) {
				}
				if (monitorInterval <= 0)
					monitorInterval = 10;
				Thread.sleep(monitorInterval * 1000);
			} catch (Exception localException6) {
			}
		}
	}
}