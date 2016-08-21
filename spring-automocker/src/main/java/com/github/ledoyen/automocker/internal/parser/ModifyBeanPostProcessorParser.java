package com.github.ledoyen.automocker.internal.parser;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;

import com.github.ledoyen.automocker.ModifyBeanPostProcessor;
import com.github.ledoyen.automocker.configuration.AutomockerConfiguration;
import com.github.ledoyen.automocker.internal.AnnotationParser;

public class ModifyBeanPostProcessorParser implements AnnotationParser<ModifyBeanPostProcessor> {

	@Override
	public void parse(ModifyBeanPostProcessor annotation, AutomockerConfiguration configuration) {
		try {
			configuration.addPostProcessorModification(Class.forName(annotation.targetClass()), BeanUtils.instantiate(annotation.beanPostProcessorModifier()));
		} catch (BeanInstantiationException | ClassNotFoundException e) {
			throw new IllegalArgumentException("Unable to load class [" + annotation.targetClass() + "], make sure it is in the test classpath", e);
		}
	}
}
