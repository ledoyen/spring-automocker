package com.github.ledoyen.automocker.extension.jmx;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.management.Attribute;
import javax.management.JMException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.github.ledoyen.automocker.tools.Guavas;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public final class LocalMBeans {

	private static final int CACHE_SIZE = 100;
	private static final MBeanServer MBEAN_SERVER = ManagementFactory.getPlatformMBeanServer();
	public static final LocalMBeans INSTANCE = new LocalMBeans();

	private final LoadingCache<String, ObjectName> objectNameByLiterals = CacheBuilder.newBuilder()
			.maximumSize(CACHE_SIZE)
			.build(CacheLoader.from(Guavas.fromThrowing(LocalMBeans::getObjectName)));
	private final LoadingCache<ObjectName, String[]> attributesByObjectNames = CacheBuilder.newBuilder()
			.maximumSize(CACHE_SIZE)
			.build(CacheLoader.from(Guavas.fromThrowing(LocalMBeans::getAttributeNames)));

	private LocalMBeans() {
	}

	public Set<ObjectName> listAvailableObjectNames() {
		return MBEAN_SERVER.queryNames(null, null);
	}

	public Map<String, Object> listAttributes(ObjectName name) {
		try {
			return extractAttributes(name, attributesByObjectNames.get(name));
		} catch (ExecutionException e) {
			throw new IllegalArgumentException(e.getCause());
		}
	}

	public Map<String, Object> listAttributes(String name) {
		try {
			return listAttributes(objectNameByLiterals.get(name));
		} catch (ExecutionException e) {
			throw new IllegalArgumentException(e.getCause());
		}
	}

	private static ObjectName getObjectName(String name) throws JMException {
		ObjectName on = new ObjectName(name);
		if (on.isPattern()) {
			Set<ObjectName> names = MBEAN_SERVER.queryNames(on, null);
			assertThat(names).as("Eligible MBeans")
					.hasSize(1);
			on = names.iterator()
					.next();
		}
		return on;
	}

	private static String[] getAttributeNames(ObjectName name) throws JMException {
		MBeanInfo info = MBEAN_SERVER.getMBeanInfo(name);
		return Arrays.asList(info.getAttributes())
				.stream()
				.map(a -> a.getName())
				.toArray(size -> new String[size]);
	}

	private Map<String, Object> extractAttributes(ObjectName on, String[] attributeNames) {
		try {
			return MBEAN_SERVER.getAttributes(on, attributeNames)
					.asList()
					.stream()
					.collect(Collectors.toMap(Attribute::getName, Attribute::getValue));
		} catch (JMException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
