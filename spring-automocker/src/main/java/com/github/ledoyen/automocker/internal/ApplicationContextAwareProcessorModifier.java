package com.github.ledoyen.automocker.internal;

import org.springframework.context.ConfigurableApplicationContext;

import com.github.ledoyen.automocker.BeanPostProcessorModifier;

public class ApplicationContextAwareProcessorModifier implements BeanPostProcessorModifier {

	@Override
	public Object modify(ConfigurableApplicationContext applicationContext, Object originalBeanPostProcessor) {
		return new AutomockerApplicationContextAwareProcessor(applicationContext);
	}
}
