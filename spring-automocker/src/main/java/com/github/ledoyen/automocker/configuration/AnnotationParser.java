package com.github.ledoyen.automocker.configuration;

import java.lang.annotation.Annotation;

public interface AnnotationParser<T extends Annotation> {

	void parse(T annotation, AutomockerConfiguration configuration);
}
