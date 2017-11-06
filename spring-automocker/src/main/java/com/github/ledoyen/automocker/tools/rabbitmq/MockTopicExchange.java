package com.github.ledoyen.automocker.tools.rabbitmq;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.rabbitmq.client.AMQP;

public class MockTopicExchange implements MockExchange {
    private final Map<String, MockQueue> queuesByBindingKeys = new ConcurrentHashMap<>();
    
    @Override
    public void publish(String routingKey, AMQP.BasicProperties props, byte[] body) {
        queuesByBindingKeys
                .entrySet()
                .stream()
                .filter(e -> match(e.getKey(), routingKey))
                .forEach(e -> e.getValue().publish(props, body));
    }

    private boolean match(String bindingKey, String routingKey) {
        return true;
    }

    @Override
    public void bind(MockQueue mockQueue, String routingKey) {
        queuesByBindingKeys.put(routingKey, mockQueue);
    }
}
