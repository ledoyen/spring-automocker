package com.github.ledoyen.automocker.tools.rabbitmq.spring;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;

import com.github.ledoyen.automocker.tools.rabbitmq.MockNode;

public class MockConnectionFactory implements ConnectionFactory {
    
    private final MockNode node = new MockNode();
    private final Set<ConnectionListener> connectionListeners = new CopyOnWriteArraySet<>();

    @Override
    public Connection createConnection() throws AmqpException {
        Connection connection = new MockConnection(node, connectionListeners);
        connectionListeners.forEach(connectionListener -> connectionListener.onCreate(connection));
        return connection;
    }

    @Override
    public String getHost() {
        return "localtest";
    }

    @Override
    public int getPort() {
        return -1;
    }

    @Override
    public String getVirtualHost() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public void addConnectionListener(ConnectionListener listener) {
        connectionListeners.add(listener);
    }

    @Override
    public boolean removeConnectionListener(ConnectionListener listener) {
        return connectionListeners.remove(listener);
    }

    @Override
    public void clearConnectionListeners() {
        connectionListeners.clear();
    }

    public MockNode getNode() {
        return node;
    }
}
