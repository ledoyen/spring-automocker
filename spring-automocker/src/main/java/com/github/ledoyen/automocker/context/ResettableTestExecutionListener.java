package com.github.ledoyen.automocker.context;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.github.ledoyen.automocker.api.ResetMocks;
import com.github.ledoyen.automocker.api.Resettable;

public class ResettableTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        if (AnnotationUtils.getAnnotation(testContext.getTestClass(), ResetMocks.class) != null) {
            ApplicationContext applicationContext = testContext.getApplicationContext();
            for (Resettable resettable : applicationContext.getBeansOfType(Resettable.class)
                    .values()) {
                resettable.reset();
            }
        }
    }
}
