package com.kepler.tcm.core.loader;

public class DefaultClassLoadStrategy implements IClassLoadStrategy {
	public ClassLoader getClassLoader(ClassLoadContext ctx) {
		if (ctx == null)
			throw new IllegalArgumentException("null input: ctx");

		ClassLoader callerLoader = ctx.getCallerClass().getClassLoader();
		ClassLoader contextLoader = Thread.currentThread()
				.getContextClassLoader();
		ClassLoader result;
		/* ClassLoader result; */
		if (isChild(contextLoader, callerLoader)) {
			result = callerLoader;
		} else {
			/* ClassLoader result; */
			if (isChild(callerLoader, contextLoader)) {
				result = contextLoader;
			} else {
				result = contextLoader;
			}
		}
		ClassLoader systemLoader = ClassLoader.getSystemClassLoader();

		if (isChild(result, systemLoader)) {
			result = systemLoader;
		}
		return result;
	}

	private static boolean isChild(ClassLoader loader1, ClassLoader loader2) {
		if (loader1 == loader2)
			return true;
		if (loader2 == null)
			return false;
		if (loader1 == null)
			return true;

		for (; loader2 != null; loader2 = loader2.getParent()) {
			if (loader2 == loader1)
				return true;
		}

		return false;
	}
}