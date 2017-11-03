package com.github.ledoyen.automocker.api;

import java.lang.annotation.Annotation;

public interface AfterBeanRegistrationExecutable<A extends Annotation> {

    void execute(A annotation, ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry);
}
