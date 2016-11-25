package com.github.ledoyen.automocker.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.ledoyen.automocker.ModifyBeanDefinition;
import com.github.ledoyen.automocker.internal.batch.BatchBeanDefinitionModifier;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ModifyBeanDefinition(targetClassName = "org.springframework.batch.core.Job", beanDefinitionModifier = BatchBeanDefinitionModifier.class)
public @interface MockBatch {

}
