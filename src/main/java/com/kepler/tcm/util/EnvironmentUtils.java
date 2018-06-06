package com.kepler.tcm.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

public class EnvironmentUtils {
	
	public static void addEnvironment(String name, ConfigurableEnvironment environment,
			String... pairs) {
		MutablePropertySources sources = environment.getPropertySources();
		Map<String, Object> map = getOrAdd(sources, name);
		for (String pair : pairs) {
			int index = getSeparatorIndex(pair);
			String key = pair.substring(0, index > 0 ? index : pair.length());
			String value = index > 0 ? pair.substring(index + 1) : "";
			map.put(key.trim(), value.trim());
		}
	}
	
	public static void addEnvironment(String name, ConfigurableEnvironment environment,
			Map<String, Object> mapParams) {
		MutablePropertySources sources = environment.getPropertySources();
		Map<String, Object> map = getOrAdd(sources, name);
		map.putAll(mapParams);;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> getOrAdd(MutablePropertySources sources,
			String name) {
		if (sources.contains(name)) {
			return (Map<String, Object>) sources.get(name).getSource();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		sources.addFirst(new MapPropertySource(name, map));
		return map;
	}

	private static int getSeparatorIndex(String pair) {
		int colonIndex = pair.indexOf(":");
		int equalIndex = pair.indexOf("=");
		if (colonIndex == -1) {
			return equalIndex;
		}
		if (equalIndex == -1) {
			return colonIndex;
		}
		return Math.min(colonIndex, equalIndex);
	}
}
