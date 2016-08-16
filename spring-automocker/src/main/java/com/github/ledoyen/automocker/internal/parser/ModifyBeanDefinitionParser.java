package com.github.ledoyen.automocker.internal.parser;

import org.springframework.beans.BeanUtils;

import com.github.ledoyen.automocker.ModifyBeanDefinition;
import com.github.ledoyen.automocker.configuration.AutomockerConfiguration;
import com.github.ledoyen.automocker.internal.AnnotationParser;

public class ModifyBeanDefinitionParser implements AnnotationParser<ModifyBeanDefinition> {

	@Override
	public void parse(ModifyBeanDefinition annotation, AutomockerConfiguration configuration) {
		checkNeededClassesAreAvailable(annotation, annotation.beanDefinitionModifier());
		configuration.addBeanModification(annotation.targetClass(), BeanUtils.instantiate(annotation.beanDefinitionModifier()));
	}
}
