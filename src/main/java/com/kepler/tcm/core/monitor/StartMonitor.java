package com.kepler.tcm.core.monitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kepler.tcm.core.log.Log4jManager;

public class StartMonitor {
	  private static String STARTCMD = "./startup.sh";

	  protected static String strMonitorIP = "127.0.0.1";

	  protected static int nMonitorPort = 6688;

	  protected static int nStopPort = 6699;

	  protected static long lThreadSleep = 60000L;

	  protected static long lThreadTimeout = 600000L;

	  protected static String strExec = "";

	  public static String LOG_PATH = "../logs/monitor.log";

	  public static Log4jManager log4jManager = null;

	  public static Logger logger = null;
	  public static final String LOGGER_MONITOR = "monitor";

	  static{
	    String osname = System.getProperty("os.name").toLowerCase();
	    if (osname.indexOf("win") != -1)
	      STARTCMD = "startup.bat";
	  }

	  public static void main(String[] args){
	    try{
	      if ((System.getProperty("exec") != null) && (!"".equals(System.getProperty("exec"))))
	        strExec = System.getProperty("exec");
	      else {
	        strExec = STARTCMD;
	      }
	      if (System.getProperty("monitorIP") != null) {
	        strMonitorIP = System.getProperty("monitorIP");
	      }
	      if (System.getProperty("monitorPort") != null) {
	        nMonitorPort = Integer.parseInt(System.getProperty("monitorPort"));
	      }
	      if (System.getProperty("stopPort") != null) {
	        nStopPort = Integer.parseInt(System.getProperty("stopPort"));
	      }

	      if (!new File(LOG_PATH).getParentFile().exists()) {
	        new File(LOG_PATH).getParentFile().mkdirs();
	      }

	      log4jManager = new Log4jManager();

	      log4jManager.addLogger("monitor", LOG_PATH, "%d %p %t %l - %m%n", true);
	      log4jManager.load();
	      logger = LoggerFactory.getLogger("logger_monitor");

	      StopMonitor sm = new StopMonitor();
	      sm.setDaemon(true);
	      sm.start();

	      logger.info("TCM monitor started!(MonitorPort:" + nMonitorPort + ")");
	      System.out.println("TCM monitor started!(MonitorPort:" + nMonitorPort + ")");
	      while (true)
	      {
	        Thread thread = new Thread(new Monitoring());
	        thread.start();
	        thread.join(lThreadTimeout);

	        synchronized (thread) {
	          thread.wait(lThreadSleep);
	        }
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
}

	  class Monitoring  implements Runnable{
	    public void run(){
	      try{
	        Socket socket = new Socket(StartMonitor.strMonitorIP, StartMonitor.nMonitorPort);
	        if (!socket.isConnected()) {
	          new Restart().start();
	        }

	        PrintWriter os = new PrintWriter(socket.getOutputStream(), true);
	        os.println(System.currentTimeMillis());
	        os.flush();
	        os.close();
	        socket.close();
	      } catch (Exception e) {
	        StartMonitor.logger.error("请求" + StartMonitor.strMonitorIP + ":" + StartMonitor.nMonitorPort + "时异常." + e);
	        new Restart().start();
	      }
	    }
	    
	    
	    class Restart extends Thread {
	    	public Restart() {
	    	}
	    	
	    	public void run() {
	    		try { Process p = Runtime.getRuntime().exec(StartMonitor.strExec);
	    			StartMonitor.logger.info("TCM_Server重新启动(" + StartMonitor.strExec + ").");
	    			BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    			String strLine = "";
	    			while ((strLine = is.readLine()) != null) {
	    				StartMonitor.logger.info(p.toString() + ":" + strLine);
	    			}
	    			is.close();
	    		} catch (Exception e) {
	    			StartMonitor.logger.error("重新启动TCM_Server时异常." + e);
	    		}
	    	}
	    }
	  }
	  /**
	   * 
	   * @author wangsp
	   * @date 2018年6月15日
	   * @version V1.0
	   */
	  class StopMonitor extends Thread {
	    public void run(){
	      try{
	        ServerSocket serverSocket = getServerSocket(StartMonitor.nStopPort);
	        while (true){
	          Socket socket = null;
	          BufferedReader is = null;
	          try{
	            socket = serverSocket.accept();
	            String from = socket.getInetAddress().toString();
	            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            String request = is.readLine();
	            if (request.equals("#StopMe!")) {
	              StartMonitor.logger.info("监控进程被" + from + "停止.");
	              System.exit(0);
	            }
	          } catch (Exception e) {
	            StartMonitor.logger.info("接收StopMonitor请求时异常." + e);
	            try
	            {
	              if (is != null) is.close(); 
	            }
	            catch (Exception localException1) {
	            }
	            try { if (socket != null) socket.close();
	            }
	            catch (Exception localException2){
	            }
	          }finally{
	            try{
	              if (is != null) is.close(); 
	            }catch (Exception localException3) {
	            }
	            try { if (socket != null) socket.close();  } catch (Exception localException4) {
	            }
	          }
	        }
	      }catch (Exception e) {
	        StartMonitor.logger.error("创建StopMonitor监听时异常." + e);
	      }
	    }

	    protected ServerSocket getServerSocket(int port) throws Exception {
	      return new ServerSocket(port);
	    }
	 }