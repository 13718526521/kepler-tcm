package com.kepler.tcm.server.tcm.monitor;

import java.io.PrintWriter;
import java.net.Socket;

public class StopMonitor {
	protected static String strMonitorIP = "127.0.0.1";

	protected static int nStopPort = 6699;

	public static void main(String[] args) {
		try {
			if (System.getProperty("monitorIP") != null) {
				strMonitorIP = System.getProperty("monitorIP");
			}
			if (System.getProperty("stopPort") != null) {
				nStopPort = Integer.parseInt(System.getProperty("stopPort"));
			}

			Socket socket = new Socket(strMonitorIP, nStopPort);
			PrintWriter os = new PrintWriter(socket.getOutputStream(), true);
			os.println("#StopMe!");
			os.flush();
			os.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}