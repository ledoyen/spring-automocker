package com.github.ledoyen.automocker;

import java.util.function.BiConsumer;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public interface BeanDefinitionModifier {

	void modify(Class<?> target, String beanName, AbstractBeanDefinition definition,
			BiConsumer<String, BeanDefinition> additionalDefinitionsRegistry);

	default void afterModifications(DefaultListableBeanFactory beanFactory) {
	}
}
