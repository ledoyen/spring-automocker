package com.github.ledoyen.automocker.examples.datajpa;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.ledoyen.automocker.api.jdbc.DatasourceLocator;
import com.github.ledoyen.automocker.base.MockJdbc;
import com.github.ledoyen.automocker.base.MockPropertySources;

public class SqlApplicationTest {

    @Test
    public void fails_as_h2_is_not_in_classpath() throws InitializationError {
        Runner runner = new SpringJUnit4ClassRunner(SampleTest.class);
        RunNotifier notifier = new RunNotifier();
        List<Throwable> testFailures = new ArrayList<>();
        notifier.addListener(new RunListener() {
            public void testFailure(Failure failure) throws Exception {
                testFailures.add(failure.getException());
            }
        });
        runner.run(notifier);
        Assertions.assertThat(testFailures)
                .as("Test failures")
                .hasSize(1);
        Assertions.assertThat(testFailures.get(0))
                .hasStackTraceContaining(
                        "Automocker is missing class [org.h2.jdbcx.JdbcDataSource] to mock 1 bean(s) of type [javax.sql.DataSource]: dataSource")
                .hasStackTraceContaining(
                        "Make sure h2.jar is in the test classpath");
    }

    @ContextConfiguration(classes = DataJpaApplication.class)
    @MockPropertySources
    @MockJdbc
    @Ignore
    public static class SampleTest {

        @Autowired
        private DatasourceLocator locator;

        @Test
        public void dummyTest() {
            Assertions.assertThat(locator.getDataSource()).isNotNull();
        }
    }
}
