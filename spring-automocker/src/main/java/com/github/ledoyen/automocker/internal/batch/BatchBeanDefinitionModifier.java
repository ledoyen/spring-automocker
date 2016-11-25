package com.github.ledoyen.automocker.internal.batch;

import java.util.function.BiConsumer;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.github.ledoyen.automocker.BeanDefinitionModifier;
import com.github.ledoyen.automocker.configuration.Need;
import com.github.ledoyen.automocker.extension.batch.BatchLauncherMock;

@Need(classname = "org.springframework.batch.test.JobLauncherTestUtils", jar = "spring-batch-test")
public class BatchBeanDefinitionModifier implements BeanDefinitionModifier {

	@Override
	public void modify(Class<?> target, String beanName, AbstractBeanDefinition definition,
			BiConsumer<String, BeanDefinition> additionalDefinitionsRegistry) {
		// Nothing to to here
	}

	@Override
	public void afterModifications(DefaultListableBeanFactory beanFactory) {
		beanFactory.registerBeanDefinition("batchLauncherMock",
				new RootBeanDefinition(BatchLauncherMock.class));
	}
}
