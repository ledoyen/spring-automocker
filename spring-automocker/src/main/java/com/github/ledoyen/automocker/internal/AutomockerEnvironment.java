package com.github.ledoyen.automocker.internal;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * Not a {@link ConfigurableEnvironment} so that {@link org.springframework.context.annotation.PropertySource @PropertySource} will be ignored.
 */
public class AutomockerEnvironment implements Environment {

	private ConfigurableEnvironment delegate;

	public AutomockerEnvironment(ConfigurableEnvironment env) {
		this.delegate = env;
	}

	@Override
	public boolean containsProperty(String key) {
		return delegate.containsProperty(key);
	}

	@Override
	public String getProperty(String key) {
		return delegate.getProperty(key);
	}

	@Override
	public String getProperty(String key, String defaultValue) {
		return delegate.getProperty(key, defaultValue);
	}

	@Override
	public <T> T getProperty(String key, Class<T> targetType) {
		return delegate.getProperty(key, targetType);
	}

	@Override
	public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
		return delegate.getProperty(key, targetType, defaultValue);
	}

	@Override
	public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType) {
		return delegate.getPropertyAsClass(key, targetType);
	}

	@Override
	public String getRequiredProperty(String key) throws IllegalStateException {
		return delegate.getRequiredProperty(key);
	}

	@Override
	public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
		return delegate.getRequiredProperty(key, targetType);
	}

	@Override
	public String resolvePlaceholders(String text) {
		return delegate.resolvePlaceholders(text);
	}

	@Override
	public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
		return delegate.resolveRequiredPlaceholders(text);
	}

	@Override
	public String[] getActiveProfiles() {
		return delegate.getActiveProfiles();
	}

	@Override
	public String[] getDefaultProfiles() {
		return delegate.getDefaultProfiles();
	}

	@Override
	public boolean acceptsProfiles(String... profiles) {
		return delegate.acceptsProfiles(profiles);
	}
}
