package com.github.ledoyen.automocker;

import java.lang.reflect.Field;

import org.junit.runners.model.InitializationError;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.TestContextBootstrapper;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DefaultBootstrapContext;
import org.springframework.test.context.support.DefaultTestContextBootstrapper;

import com.github.ledoyen.automocker.internal.AutomockerBeanFactory;

public class SpringAutomockerJUnit4ClassRunner extends SpringJUnit4ClassRunner {

	public SpringAutomockerJUnit4ClassRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	protected TestContextManager createTestContextManager(Class<?> clazz) {
		TestContextBootstrapper testContextBootstrapper = new DefaultTestContextBootstrapper() {
			@Override
			protected Class<? extends ContextLoader> getDefaultContextLoaderClass(Class<?> testClass) {
				return AutomockerContextLoader.class;
			}

			@Override
			protected MergedContextConfiguration processMergedContextConfiguration(MergedContextConfiguration mergedConfig) {
				return new AutomockerMergedContextConfiguration(mergedConfig, new AutomockerAnnotationConfigurationReader().readFrom(clazz));
			}
		};
		testContextBootstrapper.setBootstrapContext(new DefaultBootstrapContext(clazz, new DefaultCacheAwareContextLoaderDelegate()));
		return new TestContextManager(testContextBootstrapper);
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
