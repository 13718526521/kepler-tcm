package com.kepler.tcm.server.loader;

public abstract interface IClassLoadStrategy {
	public abstract ClassLoader getClassLoader(ClassLoadContext paramClassLoadContext);
}
