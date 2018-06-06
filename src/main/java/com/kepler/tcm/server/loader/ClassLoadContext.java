package com.kepler.tcm.server.loader;

public class ClassLoadContext {
	private final Class m_caller;

	public final Class getCallerClass() {
		return this.m_caller;
	}

	ClassLoadContext(Class caller) {
		this.m_caller = caller;
	}
}
