package com.github.ledoyen.automocker.tools.rabbitmq;

import com.rabbitmq.client.AMQP;

public interface MockExchange {
    void publish(String routingKey, AMQP.BasicProperties props, byte[] body);

    void bind(MockQueue mockQueue, String routingKey);
}
