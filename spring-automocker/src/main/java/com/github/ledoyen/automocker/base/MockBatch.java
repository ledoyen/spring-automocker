package com.github.ledoyen.automocker.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.batch.core.Job;

import com.github.ledoyen.automocker.api.AfterBeanRegistration;
import com.github.ledoyen.automocker.api.AfterBeanRegistrationExecutable;
import com.github.ledoyen.automocker.api.ExtendedBeanDefinitionRegistry;
import com.github.ledoyen.automocker.api.batch.BatchLauncherMock;
import com.github.ledoyen.automocker.tools.Classes;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@AfterBeanRegistration(MockBatch.MockBatchExecutable.class)
public @interface MockBatch {

    class MockBatchExecutable implements AfterBeanRegistrationExecutable<MockBatch> {
        private static final String SPRING_BATCH_TEST_CLASS = "org.springframework.batch.test.JobLauncherTestUtils";

        @Override
        public void execute(MockBatch annotation, ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry) {
            if (Classes.isPresent("org.springframework.batch.core.Job")) {
                Set<ExtendedBeanDefinitionRegistry.BeanDefinitionMetadata> jobBeans = extendedBeanDefinitionRegistry.getBeanDefinitionsForClass(Job.class);
                if(jobBeans.size() > 0) {
                    if (Classes.isPresent(SPRING_BATCH_TEST_CLASS)) {
                        extendedBeanDefinitionRegistry.registerBeanDefinition(BatchLauncherMock.class);
                    } else {
                        throw new IllegalStateException("\nAutomocker is missing class [" + SPRING_BATCH_TEST_CLASS + "] to mock " + jobBeans.size() + " bean(s) of type [" + Job.class.getName() + "]: " +
                                jobBeans.stream().map(ExtendedBeanDefinitionRegistry.BeanDefinitionMetadata::name).collect(Collectors.joining(", ")) +
                                "\nMake sure spring-batch-test.jar is in the test classpath");
                    }
                }
            }
        }
    }
}
