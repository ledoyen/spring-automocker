package com.github.ledoyen.automocker.examples;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    
    private final List<String> messages = new ArrayList<>();

    public void receiveMessage(String message) {
        if(message.contains("reject")) {
            throw new AmqpRejectAndDontRequeueException("reject");
        } else if(message.contains("requeue")) {
            throw new IllegalArgumentException("requeue");
        } else {
            System.out.println("Received <" + message + ">");
            this.messages.add(message);
        }
    }

    public List<String> getMessages() {
        return messages;
    }
}
