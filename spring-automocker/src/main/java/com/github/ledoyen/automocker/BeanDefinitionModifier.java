package com.github.ledoyen.automocker;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

public interface BeanDefinitionModifier {

	BeanDefinition modify(Class<?> target, AbstractBeanDefinition definition);
}
