package com.github.ledoyen.automocker.internal.mvc;

import java.util.function.BiConsumer;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.github.ledoyen.automocker.BeanDefinitionModifier;

public class MvcControllerMocker implements BeanDefinitionModifier {

	@Override
	public void modify(Class<?> target, String beanName, AbstractBeanDefinition definition,
			BiConsumer<String, BeanDefinition> additionalDefinitionsRegistry) {
	}

	public void afterModifications(DefaultListableBeanFactory beanFactory) {
		beanFactory.registerBeanDefinition("mockMvc", new RootBeanDefinition(MockMvcFactory.class));
	}
}
