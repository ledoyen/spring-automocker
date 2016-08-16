package com.github.ledoyen.automocker.internal;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import com.github.ledoyen.automocker.BeanDefinitionModifier;
import com.github.ledoyen.automocker.configuration.AutomockerConfiguration;

public class AutomockerBeanFactory extends DefaultListableBeanFactory {

	private final AutomockerConfiguration configuration;
	private ConfigurableApplicationContext applicationContext;

	private boolean freezeStarted = false;

	private final Set<BeanDefinitionModifier> modifiers = new HashSet<>();

	public AutomockerBeanFactory(AutomockerConfiguration configuration, ConfigurableApplicationContext context) {
		this.configuration = configuration;
		this.applicationContext = context;
	}

	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
		if (!freezeStarted) {
			Optional.ofNullable(BeanDefinitions.extractClass(beanDefinition)).flatMap(definitionClass -> configuration.getModifier(definitionClass)).ifPresent(modifier -> {
				modifiers.add(modifier._2());
				modifier._2().modify(modifier._1(), (AbstractBeanDefinition) beanDefinition);
			});
		}
		super.registerBeanDefinition(beanName, beanDefinition);
	}

	@Override
	public void registerSingleton(String beanName, Object singletonObject) throws IllegalStateException {
		if (ConfigurableEnvironment.class.isAssignableFrom(singletonObject.getClass())) {
			super.registerSingleton(beanName, new AutomockerEnvironment((ConfigurableEnvironment) singletonObject));
		} else {
			super.registerSingleton(beanName, singletonObject);
		}
	}

	@Override
	public void freezeConfiguration() {
		freezeStarted = true;
		modifiers.forEach(modifier -> modifier.afterModifications(this));

		super.freezeConfiguration();
	}

	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
		// TODO change by configuration and @ReplaceBeanPostProcessor
		try {
			Class<?> clazz = Class.forName("org.springframework.context.support.ApplicationContextAwareProcessor");
			// if (clazz.isAssignableFrom(beanPostProcessor.getClass())) {
			// super.addBeanPostProcessor(new AutomockerApplicationContextAwareProcessor(applicationContext));
			// } else {
			super.addBeanPostProcessor(beanPostProcessor);
			// }
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
