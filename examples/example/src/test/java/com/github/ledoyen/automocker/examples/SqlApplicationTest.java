package com.github.ledoyen.automocker.examples;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.github.ledoyen.automocker.SpringAutomocker;
import com.github.ledoyen.automocker.SpringAutomockerJUnit4ClassRunner;
import com.github.ledoyen.automocker.examples.repository.CustomerRepository;

@RunWith(SpringAutomockerJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataJpaApplication.class)
@SpringAutomocker
public class SqlApplicationTest {

	@Autowired
	private CustomerRepository repo;

	@Test
	public void fails_as_h2_is_not_in_classpath() {
		repo.count();
	}
}
