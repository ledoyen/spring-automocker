package com.github.ledoyen.automocker.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.ledoyen.automocker.ModifyBeanPostProcessor;
import com.github.ledoyen.automocker.internal.ApplicationContextAwareProcessorModifier;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ModifyBeanPostProcessor(classNames = "org.springframework.context.support.ApplicationContextAwareProcessor", beanDefinitionModifier = ApplicationContextAwareProcessorModifier.class)
public @interface MockPropertySource {

}
