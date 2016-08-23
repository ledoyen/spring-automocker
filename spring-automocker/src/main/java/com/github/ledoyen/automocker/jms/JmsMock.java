package com.github.ledoyen.automocker.jms;

import javax.jms.ConnectionFactory;

import org.springframework.jms.config.AbstractJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.ErrorHandler;

import com.github.ledoyen.automocker.internal.jms.ErrorHandlerMock;
import com.github.ledoyen.automocker.jms.assertion.JmsQueueAssert;

public final class JmsMock {

	private final JmsTemplate jmsTemplate;

	private ErrorHandlerMock errorHandlerMock;

	public JmsMock(ConnectionFactory connectionFactory) {
		this.jmsTemplate = new JmsTemplate(connectionFactory);
	}

	public void sendText(String queueName, String textPayload) {
		sendText(queueName, JmsMessageBuilder.newTextMessage(textPayload));
	}

	public void sendText(String queueName, String textPayload, Object... properties) {
		sendText(queueName, JmsMessageBuilder.newTextMessage(textPayload).addProperties(properties));
	}

	public void sendText(String queueName, JmsMessageBuilder messageBuilder) {
		jmsTemplate.send(queueName, messageBuilder::toMessage);
	}

	public JmsQueueAssert assertThatQueue(String queueName) {
		return new JmsQueueAssert(jmsTemplate, queueName);
	}

	public ErrorHandlerMock containerErrorHandler() {
		if (errorHandlerMock == null) {
			throw new IllegalStateException("No " + ErrorHandler.class.getSimpleName() + " available, make sure @MockJms is present and an "
					+ AbstractJmsListenerContainerFactory.class.getSimpleName() + " is configured in your Spring configuration");
		}
		return errorHandlerMock;
	}
}
