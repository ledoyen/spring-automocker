package com.github.ledoyen.automocker.configuration;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;

import org.springframework.beans.BeanUtils;

import com.github.ledoyen.automocker.internal.AnnotationConfigUtils;

public class AutomockerAnnotationConfigurationReader {

	private final Map<Class<? extends Annotation>, Function<? extends Annotation, Collection<Object>>> annotationsAnsKeyExtractors = new HashMap<>();

	@SuppressWarnings("unchecked")
	public AutomockerAnnotationConfigurationReader() {
		ServiceLoader.load(AutomockerAnnotationDescriptor.class)
				.forEach(descriptor -> {
					annotationsAnsKeyExtractors.put(descriptor.getAnnotationType(),
							descriptor.keysExtractor());
				});
	}

	@SuppressWarnings("unchecked")
	public <A extends Annotation> AutomockerConfiguration readFrom(Class<?> clazz) {
		AutomockerConfiguration configuration = new AutomockerConfiguration();
		annotationsAnsKeyExtractors.forEach((annotationType, keyExtractor) -> {
			AssociatedParser parserDescription = annotationType.getAnnotation(AssociatedParser.class);
			if (parserDescription == null) {
				throw new IllegalArgumentException("Cannot use configuration-defined ["
						+ annotationType.getSimpleName() + "] that is missing an @AssociatedParser");
			}
			Class<? extends AnnotationParser<?>> parserClass = parserDescription.value();
			AnnotationParser<A> parser = (AnnotationParser<A>) BeanUtils.instantiate(parserClass);
			Map<Object, A> collectedAnnotationsByTarget = AnnotationConfigUtils.collectAnnotations(clazz,
					(Class<A>) annotationType, (Function<A, Collection<Object>>) keyExtractor);
			collectedAnnotationsByTarget.values()
					.forEach(annotation -> {
						parser.parse(annotation, configuration);
					});
		});
		return configuration;
	}
}
