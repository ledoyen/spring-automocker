package com.github.ledoyen.automocker;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.ledoyen.automocker.internal.TestContextManagers;

public class SpringAutomockerJUnit4ClassRunner extends SpringJUnit4ClassRunner {

	public SpringAutomockerJUnit4ClassRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	protected TestContextManager createTestContextManager(Class<?> clazz) {
		return new TestContextManager(TestContextManagers.createTestContextBootstrapper(clazz, clazz));
	}
}
