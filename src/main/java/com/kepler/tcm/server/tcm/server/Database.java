package com.kepler.tcm.server.tcm.server;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
	private String strDatabaseName = "";
	private String strDatabaseDriver = "";
	private String strDatabaseUrl = "";
	private String strDatabaseUser = "";
	private String strDatabasePass = "";

	public Database(String strName, String strDriver, String strUrl, String strUser, String strPass) {
		this.strDatabaseName = strName;
		this.strDatabaseDriver = strDriver;
		this.strDatabaseUrl = strUrl;
		this.strDatabaseUser = strUser;
		this.strDatabasePass = strPass;
	}

	public String getDatabaseDriver() {
		return this.strDatabaseDriver;
	}

	public String getDatabaseName() {
		return this.strDatabaseName;
	}

	public String getDatabasePass() {
		return this.strDatabasePass;
	}

	public String getDatabaseUrl() {
		return this.strDatabaseUrl;
	}

	public String getDatabaseUser() {
		return this.strDatabaseUser;
	}

	public void setDatabaseDriver(String string) {
		this.strDatabaseDriver = string;
	}

	public void setDatabaseName(String string) {
		this.strDatabaseName = string;
	}

	public void setDatabasePass(String string) {
		this.strDatabasePass = string;
	}

	public void setDatabaseUrl(String string) {
		this.strDatabaseUrl = string;
	}

	public void setDatabaseUser(String string) {
		this.strDatabaseUser = string;
	}

	public Connection getConnection() throws Exception {
		Class.forName(this.strDatabaseDriver);
		return DriverManager.getConnection(this.strDatabaseUrl, this.strDatabaseUser, this.strDatabasePass);
	}
}