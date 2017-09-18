package com.github.ledoyen.automocker.api.jms;

import javax.jms.ConnectionFactory;

import org.springframework.jms.config.AbstractJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.ErrorHandler;

import com.mockrunner.jms.DestinationManager;

public final class JmsMock {

    final ConnectionFactory connectionFactory;
    private final JmsTemplate jmsTemplate;
    private final DestinationManager destinationManager;

    private ErrorHandlerMock errorHandlerMock;

    public JmsMock(ConnectionFactory connectionFactory, DestinationManager destinationManager) {
        this.connectionFactory = connectionFactory;
        this.jmsTemplate = new JmsTemplate(connectionFactory);
        this.destinationManager = destinationManager;
    }

    public void sendText(String destinationName, String textPayload) {
        sendText(destinationName, JmsMessageBuilder.newTextMessage(textPayload));
    }

    public void sendText(String destinationName, String textPayload, Object... properties) {
        sendText(destinationName, JmsMessageBuilder.newTextMessage(textPayload)
                .addProperties(properties));
    }

    public void sendText(String destinationName, JmsMessageBuilder messageBuilder) {
        jmsTemplate.send(destinationName, messageBuilder::toMessage);
    }

    public JmsDestinationAssert assertThatDestination(String destinationName) {
        return new JmsDestinationAssert(destinationManager, destinationName);
    }

    public ErrorHandlerMock containerErrorHandler() {
        if (errorHandlerMock == null) {
            throw new IllegalStateException("No " + ErrorHandler.class.getSimpleName()
                    + " available, make sure @MockJms is present and an "
                    + AbstractJmsListenerContainerFactory.class.getSimpleName()
                    + " is configured in your Spring configuration");
        }
        return errorHandlerMock;
    }

    void setErrorHandlerMock(ErrorHandlerMock errorHandlerMock) {
        this.errorHandlerMock = errorHandlerMock;
    }
}
