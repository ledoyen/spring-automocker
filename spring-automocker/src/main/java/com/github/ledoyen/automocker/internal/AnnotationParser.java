package com.github.ledoyen.automocker.internal;

import java.lang.annotation.Annotation;

import com.github.ledoyen.automocker.configuration.AutomockerConfiguration;

public interface AnnotationParser<T extends Annotation> {

	void parse(T annotation, AutomockerConfiguration configuration);
}
