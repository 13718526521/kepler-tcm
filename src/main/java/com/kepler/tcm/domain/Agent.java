package com.kepler.tcm.domain;

public class Agent {
	private String id;
	private String agentName;
	private String memo;
	private String state_message;
	private String state_code;
	
	public String getState_message() {
		return state_message;
	}

	public void setState_message(String state_message) {
		this.state_message = state_message;
	}

	public String getState_code() {
		return state_code;
	}

	public void setState_code(String state_code) {
		this.state_code = state_code;
	}

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
