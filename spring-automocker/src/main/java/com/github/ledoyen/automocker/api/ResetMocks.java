package com.github.ledoyen.automocker.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Activate a {@link org.springframework.test.context.TestExecutionListener} to
 * reset any bean implementing {@link Resettable}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResetMocks {
}
