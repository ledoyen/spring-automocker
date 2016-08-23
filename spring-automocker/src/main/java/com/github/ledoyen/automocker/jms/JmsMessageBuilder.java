package com.github.ledoyen.automocker.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import com.github.ledoyen.automocker.tools.ThrowingConsumer;

public abstract class JmsMessageBuilder {

	private String replyTo;

	private Map<String, Object> properties = new HashMap<>();

	private JmsMessageBuilder() {
	}

	public static TextMessageBuilder newTextMessage(String text) {
		return new TextMessageBuilder(text);
	}

	public JmsMessageBuilder addProperties(Object... properties) {
		if (properties.length % 2 != 0) {
			throw new IllegalArgumentException("properties array must be even (keys and values)");
		}
		String key = null;
		for (int i = 0; i < properties.length; i++) {
			if (i % 2 == 0) {
				key = String.class.cast(properties[i]);
			} else {
				this.properties.put(key, properties[i]);
			}
		}
		return this;
	}

	public JmsMessageBuilder setJMSReplyTo(String replyTo) {
		this.replyTo = replyTo;
		return this;
	}

	public static final class TextMessageBuilder extends JmsMessageBuilder {
		private final String text;

		private TextMessageBuilder(String text) {
			this.text = text;
		}

		@Override
		public Message toMessageInternal(Session session) throws JMSException {
			return session.createTextMessage(text);
		}
	}

	protected abstract Message toMessageInternal(Session session) throws JMSException;

	public Message toMessage(Session session) throws JMSException {
		Message message = toMessageInternal(session);
		if (replyTo != null)
			message.setJMSReplyTo(session.createQueue(replyTo));
		properties.entrySet().forEach(ThrowingConsumer.silent(e -> message.setObjectProperty(e.getKey(), e.getValue())));
		return message;
	}

}
