package com.github.ledoyen.automocker.examples;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class HardCoreJmsEchoService {

	@Autowired
	private JmsTemplate jmsTemplate;

	@JmsListener(destination = "hardcore-echo-service", containerFactory = "jmsListenerContainerFactory")
	public void receiveMessage(@Payload String message, @Header(name = "reply-to") String replyTo) {
		System.out.println("Received <" + message + ">");

		MessageCreator messageCreator = new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		};
		System.out.println("Sending a new message.");
		jmsTemplate.send(replyTo, messageCreator);
	}
}
