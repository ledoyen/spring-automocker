package com.github.ledoyen.automocker.examples;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.adapter.ListenerExecutionFailedException;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.test.context.ContextConfiguration;

import com.github.ledoyen.automocker.SpringAutomocker;
import com.github.ledoyen.automocker.SpringAutomockerJUnit4ClassRunner;
import com.mockrunner.jms.DestinationManager;

@RunWith(SpringAutomockerJUnit4ClassRunner.class)
@ContextConfiguration(classes = JmsApplication.class)
@SpringAutomocker
public class JmsApplicationTest {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private JmsErrorHandler errorHandler;

	@Autowired
	private DestinationManager mockDestinationManager;

	@Test
	public void missing_header_throws_an_exception_cathed_by_error_handler() {
		String messagePayload = "test message";
		jmsTemplate.send("echo-service", new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(messagePayload);
			}
		});
		Assertions.assertThat(errorHandler.getLastCatched()).isPresent()
				.hasValueSatisfying(t -> Assertions.assertThat(t).isExactlyInstanceOf(ListenerExecutionFailedException.class)
						.hasCauseExactlyInstanceOf(MessageHandlingException.class).hasStackTraceContaining("Missing header 'reply-to'"));
	}

	@Test
	public void correct_message_is_sent_back_toreply_to_queue() throws JMSException {
		String messagePayload = "test message";
		String responseQueueName = "response-queue";
		jmsTemplate.send("echo-service", new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(messagePayload);
				message.setStringProperty("reply-to", responseQueueName);
				return message;
			}
		});

		Queue responseQueue = mockDestinationManager.getQueue(responseQueueName);

		TextMessage message = (TextMessage) jmsTemplate.receive(responseQueue);

		Assertions.assertThat(message.getText()).isEqualTo(messagePayload);
	}
}
