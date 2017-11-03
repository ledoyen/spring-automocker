package com.github.ledoyen.automocker.tools;

import org.immutables.value.Value;

@Value.Immutable
public interface Version {
    int major();

    int minor();

    default String representation() {
        return major() + "." + minor();
    }
}
