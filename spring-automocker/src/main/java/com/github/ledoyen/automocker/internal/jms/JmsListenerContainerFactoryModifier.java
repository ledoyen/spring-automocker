package com.github.ledoyen.automocker.internal.jms;

import java.util.function.BiConsumer;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.jms.config.AbstractJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import com.github.ledoyen.automocker.BeanDefinitionModifier;

public class JmsListenerContainerFactoryModifier implements BeanDefinitionModifier {

	@Override
	public void modify(Class<?> target, String beanName, AbstractBeanDefinition definition,
			BiConsumer<String, BeanDefinition> additionalDefinitionsRegistry) {
	}

	public void afterModifications(DefaultListableBeanFactory beanFactory) {
		JmsListenerContainerFactory<?> jmsContainerFactory = beanFactory
				.getBean(JmsListenerContainerFactory.class);
		if (jmsContainerFactory instanceof AbstractJmsListenerContainerFactory) {
			beanFactory.registerBeanDefinition("jmsErrorHandlerEnricher",
					new RootBeanDefinition(ErrorHandlerMock.class));
		}
	}
}
