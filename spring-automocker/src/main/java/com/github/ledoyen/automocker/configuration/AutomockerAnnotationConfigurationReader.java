package com.github.ledoyen.automocker.configuration;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.BeanUtils;

import com.github.ledoyen.automocker.ModifyBeanDefinition;
import com.github.ledoyen.automocker.ModifyBeanPostProcessor;
import com.github.ledoyen.automocker.internal.AnnotationConfigUtils;
import com.github.ledoyen.automocker.internal.AnnotationParser;

public class AutomockerAnnotationConfigurationReader {

	private final Map<Class<? extends Annotation>, Function<? extends Annotation, Object>> annotationsAnsKeyExtractors = new HashMap<>();

	public AutomockerAnnotationConfigurationReader() {
		annotationsAnsKeyExtractors.put(ModifyBeanDefinition.class, (Function<ModifyBeanDefinition, Object>) (modifyBeanDefinition) -> {
			if (Object.class.equals(modifyBeanDefinition.targetClass())) {
				return modifyBeanDefinition.targetClassName();
			} else {
				return modifyBeanDefinition.targetClass();
			}
		});
		annotationsAnsKeyExtractors.put(ModifyBeanPostProcessor.class, (Function<ModifyBeanPostProcessor, Object>) ModifyBeanPostProcessor::targetClass);
	}

	@SuppressWarnings("unchecked")
	public <A extends Annotation> AutomockerConfiguration readFrom(Class<?> clazz) {
		AutomockerConfiguration configuration = new AutomockerConfiguration();
		annotationsAnsKeyExtractors.forEach((annotationType, keyExtractor) -> {
			AssociatedParser parserDescription = annotationType.getAnnotation(AssociatedParser.class);
			if (parserDescription == null) {
				throw new IllegalArgumentException("Cannot use configuration-defined [" + annotationType.getSimpleName() + "] that is missing an @AssociatedParser");
			}
			Class<? extends AnnotationParser<?>> parserClass = parserDescription.value();
			AnnotationParser<A> parser = (AnnotationParser<A>) BeanUtils.instantiate(parserClass);
			Map<Object, A> collectedAnnotationsByTarget = AnnotationConfigUtils.collectAnnotations(clazz, (Class<A>) annotationType, (Function<A, Object>) keyExtractor);
			collectedAnnotationsByTarget.values().forEach(annotation -> {
				parser.parse(annotation, configuration);
			});
		});
		return configuration;
	}
}
