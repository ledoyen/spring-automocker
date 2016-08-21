package com.github.ledoyen.automocker.internal.mvc;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.github.ledoyen.automocker.BeanDefinitionModifier;

public class MvcControllerMocker implements BeanDefinitionModifier {

	private boolean anyControllerDeclared = false;

	@Override
	public void modify(Class<?> target, String beanName, AbstractBeanDefinition definition) {
		anyControllerDeclared = true;
	}

	public void afterModifications(DefaultListableBeanFactory beanFactory) {
		if (anyControllerDeclared) {
			beanFactory.registerBeanDefinition("mockMvc", new RootBeanDefinition(MockMvcFactory.class));
		}
	}
}
