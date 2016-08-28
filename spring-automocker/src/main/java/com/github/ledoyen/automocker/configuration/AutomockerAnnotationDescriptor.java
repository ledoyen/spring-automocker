package com.github.ledoyen.automocker.configuration;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.Function;

/**
 * Descriptor for <b>Automocker Annotation</b>, discovered through {@link java.util.ServiceLoader} mechanism.
 * @param <T> type of the <b>Automocker Annotation</b>
 */
public interface AutomockerAnnotationDescriptor<T extends Annotation> {

	Class<T> getAnnotationType();

	Function<T, Collection<Object>> keysExtractor();
}
