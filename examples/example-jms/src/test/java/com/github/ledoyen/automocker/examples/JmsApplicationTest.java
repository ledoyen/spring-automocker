package com.github.ledoyen.automocker.examples;

import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.adapter.ListenerExecutionFailedException;
import org.springframework.jms.listener.adapter.ReplyFailureException;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.test.context.ContextConfiguration;

import com.github.ledoyen.automocker.SpringAutomocker;
import com.github.ledoyen.automocker.SpringAutomockerJUnit4ClassRunner;
import com.github.ledoyen.automocker.jms.JmsMessageBuilder;
import com.github.ledoyen.automocker.jms.JmsMock;

@RunWith(SpringAutomockerJUnit4ClassRunner.class)
@ContextConfiguration(classes = JmsApplication.class)
@SpringAutomocker
public class JmsApplicationTest {

	@Autowired
	private JmsMock jmsMock;

	@Test
	public void hardcore_missing_header_throws_an_exception_catched_by_error_handler() {
		jmsMock.sendText("hardcore-echo-service", "test message");

		Assertions.assertThat(jmsMock.containerErrorHandler().getLastCatched()).isPresent()
				.hasValueSatisfying(t -> Assertions.assertThat(t).isExactlyInstanceOf(ListenerExecutionFailedException.class)
						.hasCauseExactlyInstanceOf(MessageHandlingException.class).hasStackTraceContaining("Missing header 'reply-to'"));
	}

	@Test
	public void hardcore_correct_message_is_sent_back_to_reply_to_queue() {
		String messagePayload = "test message";
		String responseQueueName = "response-queue";
		jmsMock.sendText("hardcore-echo-service", messagePayload, "reply-to", responseQueueName);
		jmsMock.assertThatQueue(responseQueueName).hasSize(1).extractingFirstMessage().hasText(messagePayload);
	}

	@Test
	public void simple_missing_header_throws_an_exception_catched_by_error_handler() {
		jmsMock.sendText("simple-echo-service", "test message");

		Assertions.assertThat(jmsMock.containerErrorHandler().getLastCatched()).isPresent()
				.hasValueSatisfying(t -> Assertions.assertThat(t).isExactlyInstanceOf(ReplyFailureException.class).hasCauseExactlyInstanceOf(InvalidDestinationException.class)
						.hasStackTraceContaining("Request message does not contain reply-to destination"));
	}

	@Test
	public void simple_correct_message_is_sent_back_to_reply_to_queue() throws JMSException {
		String messagePayload = "test message";
		String responseQueueName = "response-queue";

		jmsMock.sendText("simple-echo-service", JmsMessageBuilder.newTextMessage(messagePayload).setJMSReplyTo(responseQueueName));
		jmsMock.assertThatQueue(responseQueueName).hasSize(1).extractingFirstMessage().hasText(messagePayload);
	}
}