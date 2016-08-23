package com.github.ledoyen.automocker.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.ledoyen.automocker.ModifyBeanDefinition;
import com.github.ledoyen.automocker.internal.jms.ConnectionFactoryModifier;
import com.github.ledoyen.automocker.internal.jms.JmsListenerContainerFactoryModifier;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ModifyBeanDefinition(targetClassName = "javax.jms.ConnectionFactory", beanDefinitionModifier = ConnectionFactoryModifier.class)
@ModifyBeanDefinition(targetClassName = "org.springframework.jms.config.JmsListenerContainerFactory", beanDefinitionModifier = JmsListenerContainerFactoryModifier.class)
public @interface MockJms {
	// TODO see org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration and related auto classes

}
