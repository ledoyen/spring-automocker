package com.github.ledoyen.automocker.api.jms;

import java.util.Map;
import java.util.Optional;

import javax.jms.ConnectionFactory;

import com.google.common.base.Joiner;

public class JmsMockLocator {

    private final Map<String, JmsMock> jmsMocksByName;

    JmsMockLocator(Map<String, JmsMock> jmsMocksByName) {
        this.jmsMocksByName = jmsMocksByName;
    }

    public Optional<JmsMock> getJmsMockByConnectionFactory(ConnectionFactory connectionFactory) {
        return jmsMocksByName.values().stream().filter(jmsMock -> jmsMock.connectionFactory == connectionFactory).findFirst();
    }

    public JmsMock getJmsMock() {
        if (jmsMocksByName.size() == 1) {
            return jmsMocksByName.values()
                    .iterator()
                    .next();
        } else {
            throw new IllegalArgumentException("Multiple JmsMock available: " + Joiner.on(", ")
                    .join(jmsMocksByName.keySet()));
        }
    }

    public JmsMock getJmsMock(String dsName) {
        if (!jmsMocksByName.containsKey(dsName)) {
            throw new IllegalArgumentException(
                    "No JmsMock named [" + dsName + "] (available: " + Joiner.on(", ")
                            .join(jmsMocksByName.keySet()) + ")");
        }
        return jmsMocksByName.get(dsName);
    }
}
