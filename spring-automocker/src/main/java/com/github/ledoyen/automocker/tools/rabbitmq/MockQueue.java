package com.github.ledoyen.automocker.tools.rabbitmq;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;

public class MockQueue implements Iterable<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MockQueue.class);

    private final String name;
    private final CyclicIterator<ConsumerAndTag> consumersIterator = new CyclicIterator<>();
    
    private final Multimap<String, String> unacked = Multimaps.synchronizedMultimap(LinkedListMultimap.create());
    private final ConcurrentLinkedQueue<String> requeued = new ConcurrentLinkedQueue<>();

    public MockQueue(String name) {
        this.name = name;
    }

    public void publish(AMQP.BasicProperties props, byte[] body) {
        if(consumersIterator.hasNext()) {
            ConsumerAndTag nextConsumer = consumersIterator.next();
            try {
                unacked.put(nextConsumer.tag(), new String(body));
                nextConsumer.consumer().handleDelivery(nextConsumer.tag(), null, props, body);
            } catch (IOException e) {
                LOGGER.warn("Unable to deliver message to consumer [" + nextConsumer.tag() + "]");
            }
        } else {
            LOGGER.warn("No consumer for queue " + name);
        }
    }

    public void addConsumer(String definitiveConsumerTag, Consumer callback) {
        consumersIterator.add(ImmutableConsumerAndTag.of(definitiveConsumerTag, callback));
    }

    @Override
    public Iterator<String> iterator() {
        return requeued.iterator();
    }

    @Value.Immutable
    interface ConsumerAndTag {
        @Value.Parameter
        String tag();
        @Value.Parameter
        Consumer consumer();
    }
}
