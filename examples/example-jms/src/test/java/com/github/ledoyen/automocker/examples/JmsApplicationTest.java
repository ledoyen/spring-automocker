package com.github.ledoyen.automocker.examples;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;

import com.github.ledoyen.automocker.SpringAutomocker;
import com.github.ledoyen.automocker.SpringAutomockerJUnit4ClassRunner;

@RunWith(SpringAutomockerJUnit4ClassRunner.class)
@ContextConfiguration(classes = JmsApplication.class)
@SpringAutomocker
public class JmsApplicationTest {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Test
	public void test() {
		System.out.println("toto");
	}
}
