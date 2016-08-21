package com.github.ledoyen.automocker.internal.jms;

import javax.jms.JMSException;

import org.springframework.jms.listener.AbstractMessageListenerContainer;

public class MessageListenerContainerMock extends AbstractMessageListenerContainer {

	@Override
	public void setConcurrency(String concurrency) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean sharedConnectionEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void doInitialize() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doShutdown() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterPropertiesSet() {
		// nothing is set, do no check anything in parent classes
	};
}
