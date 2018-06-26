package com.kepler.tcm.core.loader;

public class ClassLoaderResolver {
	private static IClassLoadStrategy s_strategy = new DefaultClassLoadStrategy();
	private static final int CALL_CONTEXT_OFFSET = 3;
	private static final CallerResolver CALLER_RESOLVER;

	static {
		try {
			CALLER_RESOLVER = new CallerResolver(null);
		} catch (SecurityException se) {
			throw new RuntimeException(
					"ClassLoaderResolver: could not create CallerResolver: "
							+ se);
		}
	}

	public static synchronized ClassLoader getClassLoader() {
		Class caller = getCallerClass(0);
		ClassLoadContext ctx = new ClassLoadContext(caller);

		return s_strategy.getClassLoader(ctx);
	}

	public static synchronized IClassLoadStrategy getStrategy() {
		return s_strategy;
	}

	public static synchronized IClassLoadStrategy setStrategy(
			IClassLoadStrategy strategy) {
		if (strategy == null)
			throw new IllegalArgumentException("null input: strategy");

		IClassLoadStrategy old = s_strategy;
		s_strategy = strategy;

		return old;
	}

	static synchronized ClassLoader getClassLoader(int callerOffset) {
		Class caller = getCallerClass(callerOffset);
		ClassLoadContext ctx = new ClassLoadContext(caller);

		return s_strategy.getClassLoader(ctx);
	}

	private static Class getCallerClass(int callerOffset) {
		return CALLER_RESOLVER.getClassContext()[(3 + callerOffset)];
	}

	private static final class CallerResolver extends SecurityManager {
		private CallerResolver() {
		}

		protected Class[] getClassContext() {
			return super.getClassContext();
		}

		CallerResolver(CallerResolver paramCallerResolver) {
			this();
		}
	}
}
