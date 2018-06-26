package com.kepler.tcm.core.loader;

public abstract interface IClassLoadStrategy {
	public abstract ClassLoader getClassLoader(
			ClassLoadContext paramClassLoadContext);
}
