package com.github.ledoyen.automocker.examples.property;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.github.ledoyen.automocker.SpringAutomocker;
import com.github.ledoyen.automocker.SpringAutomockerJUnit4ClassRunner;

@SpringAutomocker
@ContextConfiguration(classes = SimpleApplication.class)
@RunWith(SpringAutomockerJUnit4ClassRunner.class)
@TestPropertySource(properties = "text.file.content = somecontent")
public class SimpleApplicationTest {

	@Autowired
	private TextService service;

	@Test
	public void test() {
		assertThat(service.getText()).isEqualTo("somecontent");
	}
}
