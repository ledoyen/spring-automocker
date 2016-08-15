package com.github.ledoyen.automocker;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.github.ledoyen.automocker.tools.Tuple;

public class AutomockerConfiguration {

	private Map<Class<?>, BeanDefinitionModifier> beanModifications = new HashMap<>();

	public void addBeanModification(Class<?> target, BeanDefinitionModifier modifier) {
		beanModifications.put(target, modifier);
	}

	public Optional<Tuple<Class<?>, BeanDefinitionModifier>> getModifier(Class<?> target) {
		return beanModifications.entrySet().stream().filter(e -> e.getKey().isAssignableFrom(target)).findFirst().map(e -> Tuple.of(target, e.getValue()));
	}
}
