package com.github.ledoyen.automocker.examples.property;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.ledoyen.automocker.base.MockPropertySources;

@MockPropertySources
@ContextConfiguration(classes = SimpleApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = {"text.literal = literal Test Text",
        "text.optional=optionalText"})
public class SimpleApplicationTest {

    @Autowired
    private TextService service;

    @Value("${missing.key:}")
    private Optional<String> emptyOptional;

    @Test
    public void test() {
        assertThat(service.getLiteralText()).isEqualTo("literal Test Text");
        assertThat(service.getOptionalText()).isEqualTo("optionalText");
        assertThat(emptyOptional).isEmpty();
    }
}
