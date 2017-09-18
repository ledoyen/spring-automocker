package com.github.ledoyen.automocker.context;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

class MockPropertiesProtocolResolver implements ProtocolResolver {

    @Override
    public Resource resolve(String location, ResourceLoader resourceLoader) {
        if (location.endsWith(".properties")) {
            return new ByteArrayResource(new byte[0]);
        } else {
            return null;
        }
    }
}
