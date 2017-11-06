package com.github.ledoyen.automocker.tools.rabbitmq;

import com.rabbitmq.client.AMQP;

class MockDefaultExchange implements MockExchange {
    private final MockNode node;

    MockDefaultExchange(MockNode mockNode) {
        this.node = mockNode;
    }

    @Override
    public void publish(String routingKey, AMQP.BasicProperties props, byte[] body) {
        node.getQueue(routingKey).ifPresent(q -> q.publish(props, body));
    }

    @Override
    public void bind(MockQueue mockQueue, String routingKey) {
        // nothing needed
    }
}
