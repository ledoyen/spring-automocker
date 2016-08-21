package com.github.ledoyen.automocker.internal.jms;

import org.springframework.beans.factory.support.AbstractBeanDefinition;

import com.github.ledoyen.automocker.BeanDefinitionModifier;

public class JmsListenerContainerFactoryModifier implements BeanDefinitionModifier {

	@Override
	public void modify(Class<?> target, String beanName, AbstractBeanDefinition definition) {
		definition.setBeanClass(JmsListenerContainerFactoryMock.class);
		definition.setFactoryMethodName(null);
		definition.setFactoryBeanName(null);
		definition.setPropertyValues(null);
		definition.setConstructorArgumentValues(null);
	}
}
