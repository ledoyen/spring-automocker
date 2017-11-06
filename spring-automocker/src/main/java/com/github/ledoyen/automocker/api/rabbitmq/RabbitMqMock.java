package com.github.ledoyen.automocker.api.rabbitmq;

import com.github.ledoyen.automocker.tools.rabbitmq.MockNode;
import com.github.ledoyen.automocker.tools.rabbitmq.spring.MockConnectionFactory;

public class RabbitMqMock {
    
    private final MockNode mockNode;

    public RabbitMqMock(MockConnectionFactory mockConnectionFactory) {
        this.mockNode = mockConnectionFactory.getNode();
    }

    public Iterable<String> getQueue(String queueName) {
        return mockNode.getQueue(queueName).get();
    }
}
