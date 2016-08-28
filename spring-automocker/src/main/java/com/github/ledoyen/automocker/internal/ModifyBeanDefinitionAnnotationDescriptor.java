package com.github.ledoyen.automocker.internal;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Function;

import com.github.ledoyen.automocker.ModifyBeanDefinition;
import com.github.ledoyen.automocker.configuration.AutomockerAnnotationDescriptor;

public class ModifyBeanDefinitionAnnotationDescriptor implements AutomockerAnnotationDescriptor<ModifyBeanDefinition> {

	@Override
	public Class<ModifyBeanDefinition> getAnnotationType() {
		return ModifyBeanDefinition.class;
	}

	@Override
	public Function<ModifyBeanDefinition, Collection<Object>> keysExtractor() {
		return (modifyBeanDefinition) -> {
			Collection<Object> keys = new HashSet<>();
			keys.addAll(Arrays.asList(modifyBeanDefinition.targetClassName()));
			keys.addAll(Arrays.asList(modifyBeanDefinition.targetClass()));
			if (keys.isEmpty()) {
				throw new IllegalStateException("Annotation " + modifyBeanDefinition + " must have targetClass or targetClassName defined");
			}
			return keys;
		};
	}
}
