package com.github.ledoyen.automocker.examples.datajpa;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.ContextConfiguration;

import com.github.ledoyen.automocker.SpringAutomocker;
import com.github.ledoyen.automocker.SpringAutomockerJUnit4ClassRunner;

@SpringAutomocker
@ContextConfiguration(classes = DataJpaApplication.class)
public class SqlApplicationTest {

	@Test
	public void fails_as_h2_is_not_in_classpath() throws InitializationError {
		Runner runner = new SpringAutomockerJUnit4ClassRunner(this.getClass());
		RunNotifier notifier = new RunNotifier();
		List<Throwable> testFailures = new ArrayList<>();
		notifier.addListener(new RunListener() {
			public void testFailure(Failure failure) throws Exception {
				testFailures.add(failure.getException());
			}
		});
		runner.run(notifier);
		Assertions.assertThat(testFailures).as("Test failures").hasSize(1);
		Assertions.assertThat(testFailures.get(0))
				.hasStackTraceContaining("Automocker is missing class [org.h2.jdbcx.JdbcConnectionPool] to handle [DataSource], make sure h2.jar is in the test classpath");
	}
}
