package com.github.ledoyen.automocker;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public interface BeanDefinitionModifier {

	void modify(Class<?> target, String beanName, AbstractBeanDefinition definition);

	default void afterModifications(DefaultListableBeanFactory beanFactory) {
	}
}
