package com.kepler.tcm.server.tcm.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;

public class ServerListener extends Thread {
	private Logger serverLogger = null;
	private int port = 0;
	ServerSocket serverSocket;

	public ServerListener(int port, boolean log) {
		this.port = port;
		if (log)
			this.serverLogger = Logger.getLogger("logger_server." + getClass());

		try {
			this.serverSocket = getServerSocket(port);
			info("Monitor listening on " + port);
		} catch (Exception e) {
			error("创建端口监听时出现异常!" + e);
		}
	}

	private void info(String s) {
		if (this.serverLogger == null)
			System.out.println(s);
		else
			this.serverLogger.info(s);
	}

	private void error(String s) {
		if (this.serverLogger == null)
			System.err.println(s);
		else
			this.serverLogger.error(s);
	}

	public void run() {
		while (true) {
			Socket socket = null;
			BufferedReader is = null;
			try {
				socket = this.serverSocket.accept();
				String from = socket.getInetAddress().toString();
				is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String request = is.readLine();

				if ((request != null) && (request.equals("#StopMe!"))) {
					info("监控进程被" + from + "停止.");

					System.exit(0);
				}
			} catch (Exception e) {
				e.printStackTrace();

				error("处理监控请求时出现异常!" + e);
				try {
					if (is != null)
						is.close();
				} catch (Exception localException1) {
				}
				try {
					if (socket != null)
						socket.close();
				} catch (Exception localException2) {
				}
			} finally {
				try {
					if (is != null)
						is.close();
				} catch (Exception localException3) {
				}
				try {
					if (socket != null)
						socket.close();
				} catch (Exception localException4) {
				}
			}
		}
	}

	protected ServerSocket getServerSocket(int port) throws Exception {
		return new ServerSocket(port);
	}
}