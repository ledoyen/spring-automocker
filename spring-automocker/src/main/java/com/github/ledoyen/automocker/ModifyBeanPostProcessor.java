package com.github.ledoyen.automocker;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.ledoyen.automocker.configuration.AssociatedParser;
import com.github.ledoyen.automocker.internal.parser.ModifyBeanPostProcessorParser;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ModifyBeanPostProcessor.ModifyBeanPostProcessors.class)
@AssociatedParser(ModifyBeanPostProcessorParser.class)
public @interface ModifyBeanPostProcessor {

	String targetClass();

	Class<? extends BeanPostProcessorModifier> beanPostProcessorModifier();

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface ModifyBeanPostProcessors {
		ModifyBeanPostProcessor[] value();
	}
}
