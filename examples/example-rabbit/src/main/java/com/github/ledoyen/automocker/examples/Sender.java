package com.github.ledoyen.automocker.examples;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {

    private final RabbitTemplate rabbitTemplate;

    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send() throws InterruptedException {
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend("", RabbitApplication.queueName, "Hello from RabbitMQ!");
    }
}
