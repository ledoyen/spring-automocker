package com.github.ledoyen.automocker.tools;

import java.util.Properties;

public abstract class Props {
    private Props() {}

    public static Properties of(String key, String value) {
        Properties properties = new Properties();
        properties.setProperty(key, value);
        return properties;
    }
}
