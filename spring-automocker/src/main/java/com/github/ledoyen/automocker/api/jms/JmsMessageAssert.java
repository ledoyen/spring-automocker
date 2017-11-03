package com.github.ledoyen.automocker.api.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.assertj.core.api.Assertions;

public class JmsMessageAssert {

    private Message message;

    public JmsMessageAssert(Message message) {
        this.message = message;
    }

    public void hasText(String messagePayload) {
        Assertions.assertThat(message)
                .isInstanceOf(TextMessage.class);
        try {
            Assertions.assertThat(((TextMessage) message).getText())
                    .isEqualTo(messagePayload);
        } catch (JMSException e) {
            throw new IllegalStateException(
                    "Unable to retrieve 'text' from a " + TextMessage.class.getName());
        }
    }
}
