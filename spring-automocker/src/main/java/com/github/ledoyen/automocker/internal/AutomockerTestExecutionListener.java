package com.github.ledoyen.automocker.internal;

import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.github.ledoyen.automocker.Resettable;

public class AutomockerTestExecutionListener extends AbstractTestExecutionListener {

	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		ApplicationContext applicationContext = testContext.getApplicationContext();
		for (Resettable resettable : applicationContext.getBeansOfType(Resettable.class).values()) {
			resettable.reset();
		}
	}
}
