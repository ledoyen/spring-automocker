package com.github.ledoyen.automocker.jms;

import javax.jms.ConnectionFactory;

import org.springframework.jms.config.AbstractJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.ErrorHandler;

import com.github.ledoyen.automocker.internal.jms.ErrorHandlerMock;
import com.github.ledoyen.automocker.jms.assertion.JmsDestinationAssert;
import com.mockrunner.jms.DestinationManager;

public final class JmsMock {

	private final JmsTemplate jmsTemplate;

	private final DestinationManager destinationManager;

	private ErrorHandlerMock errorHandlerMock;

	public JmsMock(ConnectionFactory connectionFactory, DestinationManager destinationManager) {
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
}
