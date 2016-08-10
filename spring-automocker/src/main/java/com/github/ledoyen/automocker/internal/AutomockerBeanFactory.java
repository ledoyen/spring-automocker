package com.github.ledoyen.automocker.internal;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import com.github.ledoyen.automocker.AutomockerConfiguration;

public class AutomockerBeanFactory extends DefaultListableBeanFactory {

	private final AutomockerConfiguration configuration;
	private ConfigurableApplicationContext applicationContext;

	public AutomockerBeanFactory(AutomockerConfiguration configuration, ConfigurableApplicationContext context) {
		this.configuration = configuration;
		this.applicationContext = context;
	}

	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
		// System.out.println(beanName);
		super.registerBeanDefinition(beanName, beanDefinition);
	}

	@Override
	public void registerSingleton(String beanName, Object singletonObject) throws IllegalStateException {
		// System.out.println(beanName);
		if (ConfigurableEnvironment.class.isAssignableFrom(singletonObject.getClass())) {
			super.registerSingleton(beanName, new AutomockerEnvironment((ConfigurableEnvironment) singletonObject));
		} else {
			super.registerSingleton(beanName, singletonObject);
		}
	}

	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
		// TODO change by configuration and @ReplaceBeanPostProcessor
		try {
			Class<?> clazz = Class.forName("org.springframework.context.support.ApplicationContextAwareProcessor");
			if (clazz.isAssignableFrom(beanPostProcessor.getClass())) {
				super.addBeanPostProcessor(new AutomockerApplicationContextAwareProcessor(applicationContext));
			} else {
				super.addBeanPostProcessor(beanPostProcessor);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
