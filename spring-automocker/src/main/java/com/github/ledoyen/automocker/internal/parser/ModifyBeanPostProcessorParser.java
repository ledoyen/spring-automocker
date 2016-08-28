package com.github.ledoyen.automocker.internal.parser;

import java.util.Arrays;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;

import com.github.ledoyen.automocker.ModifyBeanPostProcessor;
import com.github.ledoyen.automocker.configuration.AnnotationParser;
import com.github.ledoyen.automocker.configuration.AutomockerConfiguration;

public class ModifyBeanPostProcessorParser implements AnnotationParser<ModifyBeanPostProcessor> {

	@Override
	public void parse(ModifyBeanPostProcessor annotation, AutomockerConfiguration configuration) {
		Arrays.asList(annotation.targetClassName()).forEach(targetClassName -> {
			try {
				configuration.addPostProcessorModification(Class.forName(targetClassName), BeanUtils.instantiate(annotation.beanPostProcessorModifier()));
			} catch (BeanInstantiationException | ClassNotFoundException e) {
				throw new IllegalArgumentException("Unable to load class [" + annotation.targetClassName() + "], make sure it is in the test classpath", e);
			}
		});
	}
}
