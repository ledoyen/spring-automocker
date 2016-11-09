package com.github.ledoyen.automocker.internal;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Function;

import com.github.ledoyen.automocker.ModifyBeanPostProcessor;
import com.github.ledoyen.automocker.configuration.AutomockerAnnotationDescriptor;

public class ModifyBeanPostProcessorAnnotationDescriptor
		implements AutomockerAnnotationDescriptor<ModifyBeanPostProcessor> {

	@Override
	public Class<ModifyBeanPostProcessor> getAnnotationType() {
		return ModifyBeanPostProcessor.class;
	}

	@Override
	public Function<ModifyBeanPostProcessor, Collection<Object>> keysExtractor() {
		return (modifyBeanPostProcessor) -> {
			Collection<Object> keys = new HashSet<>();
			keys.addAll(Arrays.asList(modifyBeanPostProcessor.targetClassName()));
			if (keys.isEmpty()) {
				throw new IllegalStateException(
						"Annotation " + modifyBeanPostProcessor + " must have targetClass defined");
			}
			return keys;
		};
	}
}
