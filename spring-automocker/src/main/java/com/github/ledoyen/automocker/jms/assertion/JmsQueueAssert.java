package com.github.ledoyen.automocker.jms.assertion;

import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.springframework.jms.core.JmsTemplate;

public class JmsQueueAssert {

	private JmsTemplate jmsTemplate;
	private String destinationName;

	public JmsQueueAssert(JmsTemplate jmsTemplate, String destinationName) {
		this.jmsTemplate = jmsTemplate;
		this.destinationName = destinationName;
	}

	@SuppressWarnings("unchecked")
	public JmsQueueAssert hasSize(int expected) {
		Assertions.assertThat(jmsTemplate.browse(destinationName, (session, browser) -> {
			return Collections.list(browser.getEnumeration()).size();
		})).isEqualTo(expected);
		return this;
	}

	public JmsMessageAssert extractingFirstMessage() {
		return new JmsMessageAssert(jmsTemplate.receive(destinationName));
	}
}
