package com.github.ledoyen.automocker.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.TestContextBootstrapper;
import org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DefaultBootstrapContext;
import org.springframework.test.context.support.DefaultTestContextBootstrapper;

import com.github.ledoyen.automocker.configuration.AutomockerAnnotationConfigurationReader;
import com.github.ledoyen.automocker.configuration.AutomockerConfiguration;

public class TestContextManagers {

	public static TestContextBootstrapper createTestContextBootstrapper(Class<?> testClass, Class<?> automockerConfigurationClass) {
		TestContextBootstrapper testContextBootstrapper = new DefaultTestContextBootstrapper() {
			@Override
			protected Class<? extends ContextLoader> getDefaultContextLoaderClass(Class<?> testClass) {
				return AutomockerContextLoader.class;
			}

			@Override
			protected MergedContextConfiguration processMergedContextConfiguration(MergedContextConfiguration mergedConfig) {
				return new AutomockerMergedContextConfiguration(mergedConfig, new AutomockerAnnotationConfigurationReader().readFrom(automockerConfigurationClass));
			}

			@Override
			protected List<String> getDefaultTestExecutionListenerClassNames() {
				List<String> classes = new ArrayList<>();
				classes.addAll(super.getDefaultTestExecutionListenerClassNames());
				classes.add(AutomockerTestExecutionListener.class.getName());
				return Collections.unmodifiableList(classes);
			}
		};
		testContextBootstrapper.setBootstrapContext(new DefaultBootstrapContext(testClass, new DefaultCacheAwareContextLoaderDelegate()));
		return testContextBootstrapper;
	}

	private static final class AutomockerContextLoader extends AnnotationConfigContextLoader {
		@Override
		public void prepareContext(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
			try {
				Field beanFactoryField = GenericApplicationContext.class.getDeclaredField("beanFactory");
				beanFactoryField.setAccessible(true);
				BeanFactory beanFactory = new AutomockerBeanFactory(((AutomockerMergedContextConfiguration) mergedConfig).configuration, context);
				beanFactoryField.set(context, beanFactory);
			} catch (NoSuchFieldException | IllegalAccessException e) {
				throw new RuntimeException("Unable to perform bean factory hack", e);
			}
			super.prepareContext(context, mergedConfig);
		}
	}

	@SuppressWarnings("serial")
	private static final class AutomockerMergedContextConfiguration extends MergedContextConfiguration {

		private final AutomockerConfiguration configuration;

		public AutomockerMergedContextConfiguration(MergedContextConfiguration mergedConfig, AutomockerConfiguration configuration) {
			super(mergedConfig);
			this.configuration = configuration;
		}
	}
}
