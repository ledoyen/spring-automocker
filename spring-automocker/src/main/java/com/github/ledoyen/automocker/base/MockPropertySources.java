package com.github.ledoyen.automocker.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.github.ledoyen.automocker.api.BeforeBeanRegistration;
import com.github.ledoyen.automocker.api.BeforeBeanRegistrationExecutable;

/**
 * Make Spring ignore {@link org.springframework.context.annotation.PropertySource} annotations.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@BeforeBeanRegistration(MockPropertySources.MockPropertySourcesExecutable.class)
public @interface MockPropertySources {

    class MockPropertySourcesExecutable implements BeforeBeanRegistrationExecutable<MockPropertySources> {
        static ProtocolResolver MOCK_PROPERTIES_PROTOCOL_RESOLVER = new MockPropertiesProtocolResolver();

        @Override
        public void execute(MockPropertySources annotation, ConfigurableApplicationContext context) {
            context.addProtocolResolver(MOCK_PROPERTIES_PROTOCOL_RESOLVER);
        }

        private static class MockPropertiesProtocolResolver implements ProtocolResolver {
            @Override
            public Resource resolve(String location, ResourceLoader resourceLoader) {
                // TODO improve to exclude only files that are detected through class scanning
                if (location.endsWith(".properties") && !location.contains("META-INF")) {
                    return new ByteArrayResource(new byte[0]);
                } else {
                    return null;
                }
            }
        }
    }
}
