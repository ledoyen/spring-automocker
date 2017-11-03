package com.github.ledoyen.automocker.api.jms;

import java.util.Optional;
import java.util.Set;

import javax.jms.ConnectionFactory;

import org.springframework.jms.config.AbstractJmsListenerContainerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ErrorHandler;

public class JmsListenerContainerFactoryConfigurer {

    JmsListenerContainerFactoryConfigurer(Set<AbstractJmsListenerContainerFactory<?>> jmsListenerContainerFactories
            , JmsMockLocator jmsMockLocator
    ) {
        jmsListenerContainerFactories.forEach(jmsListenerContainerFactory -> {
            ConnectionFactory connectionFactory = (ConnectionFactory) ReflectionTestUtils.getField(
                    jmsListenerContainerFactory, "connectionFactory");
            Optional<ErrorHandler> originalErrorHandler = Optional.ofNullable(
                    (ErrorHandler) ReflectionTestUtils.getField(jmsListenerContainerFactory, "errorHandler"));

            ErrorHandlerMock automockerErrorHandler = new ErrorHandlerMock(originalErrorHandler);
            jmsListenerContainerFactory.setErrorHandler(automockerErrorHandler);

            Optional<JmsMock> jmsMockOptional = jmsMockLocator.getJmsMockByConnectionFactory(connectionFactory);
            jmsMockOptional.ifPresent(jmsMock -> jmsMock.setErrorHandlerMock(automockerErrorHandler));
        });
    }
}
