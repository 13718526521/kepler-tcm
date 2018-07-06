package com.kepler.tcm.domain;

public class ProxyServer {
	private String id;
	private String agentName;
	private String memo;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String toString() {
		return "ProxyServer [" + (id != null ? "id=" + id + ", " : "")
				+ (agentName != null ? "agentName=" + agentName + ", " : "")
				+ (memo != null ? "memo=" + memo : "") + "]";
	}

}
