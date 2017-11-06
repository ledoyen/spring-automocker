package com.github.ledoyen.automocker.tools.rabbitmq;

public class MockExchangeFactory {
    public static MockExchange build(String type) {
        if("topic".equals(type)) {
            return new MockTopicExchange();
        }
        throw new IllegalArgumentException("No exchange type " + type);
    }
}
