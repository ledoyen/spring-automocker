package com.github.ledoyen.automocker.tools.rabbitmq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class CyclicIterator<T> {

    private final AtomicInteger sequence = new AtomicInteger();
    private final List<T> collection = new ArrayList<>();
    
    boolean hasNext() {
        return collection.size() > 0;
    }
    
    T next() {
        return collection.get(sequence.incrementAndGet() % collection.size());
    }

    CyclicIterator<T> add(T item) {
        collection.add(item);
        return this;
    }
}
