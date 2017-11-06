package com.github.ledoyen.automocker.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.ledoyen.automocker.api.ResetMocks;

/**
 * Collection of mocking strategies supplied by default.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@MockPropertySources
@MockJdbc
@MockSpringWeb
@MockJms
@MockBatch
@MockRabbitMq

@ResetMocks
public @interface Automocker {
}
