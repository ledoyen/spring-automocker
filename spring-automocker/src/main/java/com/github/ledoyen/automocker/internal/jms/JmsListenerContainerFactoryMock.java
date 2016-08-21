package com.github.ledoyen.automocker.internal.jms;

import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpoint;

public class JmsListenerContainerFactoryMock implements JmsListenerContainerFactory<MessageListenerContainerMock> {

	@Override
	public MessageListenerContainerMock createListenerContainer(JmsListenerEndpoint endpoint) {
		MessageListenerContainerMock mock = new MessageListenerContainerMock();
		endpoint.setupListenerContainer(mock);
		return mock;
	}

}
