package com.github.ledoyen.automocker;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.ledoyen.automocker.internal.AnnotationParser;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AssociatedParser {

	Class<? extends AnnotationParser<?>> value();
}
