package com.github.ledoyen.automocker.configuration;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.core.annotation.AnnotationUtils;

import com.github.ledoyen.automocker.BeanDefinitionModifier;
import com.github.ledoyen.automocker.BeanPostProcessorModifier;
import com.github.ledoyen.automocker.tools.Tuple;

public class AutomockerConfiguration {

	private Map<Class<?>, BeanDefinitionModifier> beanModifications = new HashMap<>();

	private Map<Class<?>, BeanPostProcessorModifier> beanPostProcessorModifications = new HashMap<>();

	public void addBeanModification(Class<?> target, BeanDefinitionModifier modifier) {
		beanModifications.put(target, modifier);
	}

	public Optional<Tuple<Class<?>, BeanDefinitionModifier>> getModifier(Class<?> target) {
		return beanModifications.entrySet().stream().filter(e -> match(e, target)).findFirst().map(e -> Tuple.of(target, e.getValue()));
	}

	@SuppressWarnings("unchecked")
	private static boolean match(Entry<Class<?>, BeanDefinitionModifier> modifier, Class<?> target) {
		final boolean match;
		if (modifier.getKey().isAnnotation()) {
			match = AnnotationUtils.findAnnotation(target, (Class<? extends Annotation>) modifier.getKey()) != null;
		} else {
			match = modifier.getKey().isAssignableFrom(target);
		}
		return match;
	}

	public void addPostProcessorModification(Class<?> targetClass, BeanPostProcessorModifier instantiate) {
		beanPostProcessorModifications.put(targetClass, instantiate);
	}
}
