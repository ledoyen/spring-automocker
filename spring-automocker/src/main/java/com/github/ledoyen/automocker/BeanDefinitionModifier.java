package com.github.ledoyen.automocker;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public interface BeanDefinitionModifier {

	BeanDefinition modify(Class<?> target, String beanName, AbstractBeanDefinition definition);

	default void afterModifications(DefaultListableBeanFactory beanFactory) {
	}
}
