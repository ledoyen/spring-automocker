package com.github.ledoyen.automocker.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.github.ledoyen.automocker.api.AfterBeanRegistration;
import com.github.ledoyen.automocker.api.AfterBeanRegistrationExecutable;
import com.github.ledoyen.automocker.api.ExtendedBeanDefinitionRegistry;
import com.github.ledoyen.automocker.api.rabbitmq.RabbitMqMock;
import com.github.ledoyen.automocker.tools.Classes;
import com.github.ledoyen.automocker.tools.rabbitmq.spring.MockConnectionFactory;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@AfterBeanRegistration(MockRabbitMq.MockRabbitMqExecutable.class)
public @interface MockRabbitMq {
    // ConnectionFactory

    class MockRabbitMqExecutable implements AfterBeanRegistrationExecutable<MockRabbitMq> {

        @Override
        public void execute(MockRabbitMq annotation, ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry) {
            if (Classes.isPresent("org.springframework.amqp.rabbit.connection.ConnectionFactory")) {
                Set<ExtendedBeanDefinitionRegistry.BeanDefinitionMetadata> connectionFactoryBeans = extendedBeanDefinitionRegistry
                        .getBeanDefinitionsForClass(ConnectionFactory.class);
                if (connectionFactoryBeans.size() > 0) {
                    connectionFactoryBeans.forEach(bdm -> {
                                bdm.beanDefinitionModifier()
                                        .reset()
                                        .setBeanClass(MockConnectionFactory.class);
                                RootBeanDefinition rabbitMqMockBeanDefinition = new RootBeanDefinition(RabbitMqMock.class);
                                rabbitMqMockBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, new RuntimeBeanReference(bdm.name()));
                                extendedBeanDefinitionRegistry.registerBeanDefinition("Automocker" + bdm.name() + "RabbitMqMock", rabbitMqMockBeanDefinition);
                            }
                    );
                }
            }
        }
    }
}