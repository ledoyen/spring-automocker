package com.github.ledoyen.automocker.internal.parser;

import java.util.Arrays;

import org.springframework.util.ClassUtils;

import com.github.ledoyen.automocker.ModifyBeanDefinition;
import com.github.ledoyen.automocker.configuration.AnnotationParser;
import com.github.ledoyen.automocker.configuration.AutomockerConfiguration;

public class ModifyBeanDefinitionParser implements AnnotationParser<ModifyBeanDefinition> {

	private static final ClassLoader CLASS_LOADER = ModifyBeanDefinitionParser.class.getClassLoader();

	@Override
	public void parse(ModifyBeanDefinition annotation, AutomockerConfiguration configuration) {
		Arrays.asList(annotation.targetClass())
				.forEach(targetClass -> configuration.addBeanModification(targetClass,
						annotation.beanDefinitionModifier()));
		Arrays.asList(annotation.targetClassName())
				.forEach(targetClassName -> {
					try {
						configuration.addBeanModification(ClassUtils.forName(targetClassName, CLASS_LOADER),
								annotation.beanDefinitionModifier());
					} catch (ClassNotFoundException e) {
						// Ignore this definition as ClassPath does not contain the class that is to be mocked
					}
				});
	}
}
