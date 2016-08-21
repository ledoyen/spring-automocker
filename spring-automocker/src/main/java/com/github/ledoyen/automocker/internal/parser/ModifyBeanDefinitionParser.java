package com.github.ledoyen.automocker.internal.parser;

import org.springframework.util.ClassUtils;

import com.github.ledoyen.automocker.ModifyBeanDefinition;
import com.github.ledoyen.automocker.configuration.AutomockerConfiguration;
import com.github.ledoyen.automocker.internal.AnnotationParser;

public class ModifyBeanDefinitionParser implements AnnotationParser<ModifyBeanDefinition> {

	private static final ClassLoader CLASS_LOADER = ModifyBeanDefinitionParser.class.getClassLoader();

	@Override
	public void parse(ModifyBeanDefinition annotation, AutomockerConfiguration configuration) {
		if (!Object.class.equals(annotation.targetClass())) {
			configuration.addBeanModification(annotation.targetClass(), annotation.beanDefinitionModifier());
		} else if (!"".equals(annotation.targetClassName())) {
			try {
				configuration.addBeanModification(ClassUtils.forName(annotation.targetClassName(), CLASS_LOADER), annotation.beanDefinitionModifier());
			} catch (ClassNotFoundException e) {
				// Ignore this definition as ClassPath does not contain the class that is to be mocked
			}
		}
	}
}
