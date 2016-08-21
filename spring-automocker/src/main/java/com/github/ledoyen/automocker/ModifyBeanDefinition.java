package com.github.ledoyen.automocker;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.ledoyen.automocker.configuration.AssociatedParser;
import com.github.ledoyen.automocker.internal.MockitoBeanDefinitionModifier;
import com.github.ledoyen.automocker.internal.parser.ModifyBeanDefinitionParser;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ModifyBeanDefinition.ModifyBeanDefinitions.class)
@AssociatedParser(ModifyBeanDefinitionParser.class)
public @interface ModifyBeanDefinition {

	Class<?> targetClass() default Object.class;

	String targetClassName() default "";

	Class<? extends BeanDefinitionModifier> beanDefinitionModifier() default MockitoBeanDefinitionModifier.class;

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface ModifyBeanDefinitions {
		ModifyBeanDefinition[] value();
	}

}
