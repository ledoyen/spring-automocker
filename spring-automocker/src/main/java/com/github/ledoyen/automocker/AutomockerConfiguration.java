package com.github.ledoyen.automocker;

import java.util.Map;

import com.github.ledoyen.automocker.internal.AnnotationConfigUtils;

public class AutomockerConfiguration {

	public static AutomockerConfiguration readFrom(Class<?> clazz) {
		Map<Class<?>, ModifyBeanDefinition> anns = AnnotationConfigUtils.collectAnnotations(clazz, ModifyBeanDefinition.class, ModifyBeanDefinition::value);
		return new AutomockerConfiguration();
	}
}
