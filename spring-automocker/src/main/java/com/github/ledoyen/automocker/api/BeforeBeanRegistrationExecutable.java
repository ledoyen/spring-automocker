package com.github.ledoyen.automocker.api;

import java.lang.annotation.Annotation;

import org.springframework.context.ConfigurableApplicationContext;

public interface BeforeBeanRegistrationExecutable<A extends Annotation> {

    void execute(A annotation, ConfigurableApplicationContext context);
}
