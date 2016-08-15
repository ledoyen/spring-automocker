package com.github.ledoyen.automocker;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.BeanUtils;

import com.github.ledoyen.automocker.internal.AnnotationConfigUtils;
import com.github.ledoyen.automocker.internal.AnnotationParser;

public class AutomockerAnnotationConfigurationReader {

	private final Map<Class<? extends Annotation>, Function<? extends Annotation, Class<?>>> annotationsAnsKeyExtractors = new HashMap<>();

	public AutomockerAnnotationConfigurationReader() {
		annotationsAnsKeyExtractors.put(ModifyBeanDefinition.class, (Function<ModifyBeanDefinition, Class<?>>) ModifyBeanDefinition::value);
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
			Map<Class<?>, A> collectedAnnotationsByTarget = AnnotationConfigUtils.collectAnnotations(clazz, (Class<A>) annotationType, (Function<A, Class<?>>) keyExtractor);
			collectedAnnotationsByTarget.forEach((targetClass, annotation) -> {
				parser.parse(annotation, configuration);
			});
		});
		return configuration;
	}
}
