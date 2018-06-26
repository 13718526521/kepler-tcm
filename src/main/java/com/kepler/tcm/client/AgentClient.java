package com.kepler.tcm.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.rmi.Naming;

import com.kepler.tcm.core.agent.RemoteAgent;

public class AgentClient {

	/**
	 * 连接代理服务并返回
	 * 
	 * @param server
	 *            代理服务器连接信息 eg ： 172.16.13.248:1098/tcm_agent
	 * @return
	 * @throws Exception
	 */
	public RemoteAgent getAgent(String rmi) throws Exception {

		return (RemoteAgent) Naming.lookup(rmi);
	}
	

	/**
	 * 连接代理服务并返回
	 * 
	 * @param ip
	 * @param port
	 * @param rmiName
	 * @return
	 * @throws Exception
	 */
	public RemoteAgent getAgent(String ip, String port, String rmiName)
			throws Exception {

		String rmi = "rmi://" + ip + ":" + port + "/" + rmiName;

		return getAgent(rmi);
	}


	public static void upload(RemoteAgent remoteAgent, String local,
			String remote) throws Exception {
		File f = new File(local);
		FileInputStream is = null;
		try {
			is = new FileInputStream(f);
			int length = is.available();
			byte[] bs = new byte[length];
			is.read(bs);
			remoteAgent.uploadFile(bs, remote, f.lastModified());
		} finally {
			if (is != null)
				is.close();
		}
	}

	public static void upload(RemoteAgent remoteAgent, InputStream localStream,
			String remote) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] bs = new byte[1024];
		int nRead = 0;
		while ((nRead = localStream.read(bs)) > 0)
			bos.write(bs, 0, nRead);

		remoteAgent.uploadFile(bos.toByteArray(), remote, -1L);
	}

	public static void download(RemoteAgent remoteAgent, String remote,
			String local) throws Exception {
		byte[] bs = remoteAgent.downloadFile(remote);
		FileOutputStream os = new FileOutputStream(local);
		os.write(bs);
		os.close();
	}

	public static void main(String[] args) throws Exception {
		String rmi = "rmi://127.0.0.1:1098/tcm_agent";
		RemoteAgent remoteAgent = new AgentClient().getAgent(rmi);
		String msg = remoteAgent.getConnectRMI();
		System.out.println("client:" + msg);
		// upload(remoteAgent, "c:\\1.jpg", "d:\\aagex.jpg");
	}

}
