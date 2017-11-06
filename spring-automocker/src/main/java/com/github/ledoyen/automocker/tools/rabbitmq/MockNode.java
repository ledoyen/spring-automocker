package com.github.ledoyen.automocker.tools.rabbitmq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;

public class MockNode {
    
    private final List<MockQueue.ConsumerAndTag> consumers = new ArrayList<>();
    private final Map<String, MockExchange> exchanges = new ConcurrentHashMap<>();
    private final Map<String, MockQueue> queues = new ConcurrentHashMap<>();
    
    public MockNode() {
        exchanges.put("", new MockDefaultExchange(this));
        queues.put("unrouted", new MockQueue("unrouted"));
    }

    public void basicPublish(String exchange, String routingKey, boolean mandatory, boolean immediate, AMQP.BasicProperties props, byte[] body) {
        if(!exchanges.containsKey(exchange)) {
            throw new IllegalArgumentException("No exchange named " + exchange);
        }
        exchanges.get(exchange).publish(routingKey, props, body);
    }

    public String basicConsume(String queue, boolean autoAck, String consumerTag, boolean noLocal, boolean exclusive, Map<String, Object> arguments, Consumer callback) {
        final String definitiveConsumerTag;
        if("".equals(consumerTag)) {
            definitiveConsumerTag = "amq.ctag-test" + UUID.randomUUID();
        } else {
            definitiveConsumerTag = consumerTag;
        }
        if(!queues.containsKey(queue)) {
            throw new IllegalArgumentException("No queue named " + queue);
        }

        queues.get(queue).addConsumer(definitiveConsumerTag, callback);
        
        return definitiveConsumerTag;
    }

    public Optional<MockQueue> getQueue(String name) {
        return Optional.ofNullable(queues.get(name));
    }

    public AMQP.Exchange.DeclareOk exchangeDeclare(String exchange, String type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments) {
        exchanges.put(exchange, MockExchangeFactory.build(type));
        return null;
    }

    public AMQP.Queue.DeclareOk queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) {
        queues.putIfAbsent(queue, new MockQueue(queue));
        return null;
    }

    public AMQP.Queue.BindOk queueBind(String queue, String exchange, String routingKey, Map<String, Object> arguments) {
        if(!exchanges.containsKey(exchange)) {
            throw new IllegalArgumentException("No exchange named " + queue);
        }
        if(!queues.containsKey(queue)) {
            throw new IllegalArgumentException("No queue named " + queue);
        }
        exchanges.get(exchange).bind(queues.get(queue), routingKey);
        return null;
    }
}
