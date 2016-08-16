package com.github.ledoyen.automocker.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.ledoyen.automocker.ModifyBeanPostProcessor;
import com.github.ledoyen.automocker.internal.ApplicationContextAwareProcessorModifier;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ModifyBeanPostProcessor(targetClass = "org.springframework.context.support.ApplicationContextAwareProcessor", beanPostProcessorModifier = ApplicationContextAwareProcessorModifier.class)
public @interface MockPropertySource {

}
