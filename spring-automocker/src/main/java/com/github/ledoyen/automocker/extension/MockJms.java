package com.github.ledoyen.automocker.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.ledoyen.automocker.ModifyBeanDefinition;
import com.github.ledoyen.automocker.internal.jms.JmsListenerContainerFactoryModifier;
import com.github.ledoyen.automocker.internal.jms.JmsTemplateModifier;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
// TODO @Reject("org.springframework.boot.autoconfigure.jms.ConnectionFactory")
@ModifyBeanDefinition(targetClassName = "org.springframework.jms.config.JmsListenerContainerFactory", beanDefinitionModifier = JmsListenerContainerFactoryModifier.class)
@ModifyBeanDefinition(targetClassName = "org.springframework.jms.core.JmsTemplate", beanDefinitionModifier = JmsTemplateModifier.class)
public @interface MockJms {

}
