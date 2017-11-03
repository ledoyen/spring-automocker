package com.github.ledoyen.automocker.examples;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class SimpleJmsEchoService {

    @JmsListener(destination = "simple-echo-service", containerFactory = "jmsListenerContainerFactory")
    public String receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        System.out.println("Sending back the payload.");
        return message;
    }
}
