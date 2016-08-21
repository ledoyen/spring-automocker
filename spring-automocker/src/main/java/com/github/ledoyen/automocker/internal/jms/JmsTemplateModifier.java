package com.github.ledoyen.automocker.internal.jms;

import org.springframework.beans.factory.support.AbstractBeanDefinition;

import com.github.ledoyen.automocker.BeanDefinitionModifier;

public class JmsTemplateModifier implements BeanDefinitionModifier {

	@Override
	public void modify(Class<?> target, String beanName, AbstractBeanDefinition definition) {
		System.out.println("toto");
		// TODO Auto-generated method stub
	}
}
