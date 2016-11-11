package com.github.ledoyen.automocker.extension.jms;

import org.assertj.core.api.Assertions;

import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockDestination;

public class JmsDestinationAssert {

	private final String destinationName;
	private final MockDestination destination;

	public JmsDestinationAssert(DestinationManager destinationManager, String destinationName) {
		this.destinationName = destinationName;
		if (destinationManager.existsQueue(destinationName)) {
			this.destination = destinationManager.getQueue(destinationName);
		} else if (destinationManager.existsTopic(destinationName)) {
			this.destination = destinationManager.getTopic(destinationName);
		} else {
			throw new IllegalArgumentException("No destination [" + destinationName + "] can be found");
		}
	}

	public JmsDestinationAssert hasSize(int expected) {
		Assertions.assertThat(destination.getReceivedMessageList()
				.size())
				.isEqualTo(expected);
		return this;
	}

	public JmsMessageAssert consumingFirstMessage() {
		if (destination.isEmpty()) {
			throw new IllegalArgumentException(
					"No message available on destination [" + destinationName + "]");
		}
		return new JmsMessageAssert(destination.getMessage());
	}

	public String toString() {
		return "Asserter on JMS destination [" + destinationName + "]";
	}
}
