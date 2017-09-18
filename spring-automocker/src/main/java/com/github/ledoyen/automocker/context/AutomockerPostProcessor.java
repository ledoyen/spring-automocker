package com.github.ledoyen.automocker.context;

import java.util.List;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import com.github.ledoyen.automocker.api.AfterBeanRegistration;
import com.github.ledoyen.automocker.api.BeforeBeanRegistration;
import com.github.ledoyen.automocker.tools.Annotations;
import com.github.ledoyen.automocker.tools.Classes;
import com.github.ledoyen.automocker.tools.ImmutableVersion;
import com.github.ledoyen.automocker.tools.Version;
import com.google.common.base.Splitter;
import com.google.common.primitives.Ints;

class AutomockerPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    static final String BEAN_NAME = AutomockerPostProcessor.class.getName();
    static final Version SPRING_MIN_VERSION = ImmutableVersion.builder().major(4).minor(3).build();

    private final Class<?> testClass;

    AutomockerPostProcessor(Class<?> testClass, ConfigurableApplicationContext context) {
        this.testClass = testClass;
        checkSpringVersion();
        invokeBeforeBeanRegistrationHooks(context);
    }

    private void checkSpringVersion() {
        String springVersion = ApplicationContext.class.getPackage().getImplementationVersion();
        List<String> tokenizedSpringVersion = Splitter.on('.').splitToList(springVersion);
        int major = Ints.tryParse(tokenizedSpringVersion.get(0));
        int minor = Ints.tryParse(tokenizedSpringVersion.get(1));

        if (major < SPRING_MIN_VERSION.major() || major == SPRING_MIN_VERSION.major() && minor < SPRING_MIN_VERSION.minor()) {
            throw new IllegalStateException("Automocker needs Spring in version >= " + SPRING_MIN_VERSION.representation() + ", found " + springVersion);
        }
    }

    private void invokeBeforeBeanRegistrationHooks(ConfigurableApplicationContext context) {
        Set<Annotations.AnnotatedAnnotation<BeforeBeanRegistration>> beforeBeanRegistrationAnnotations
                = Annotations.getAnnotationsAnnotatedWith(testClass, BeforeBeanRegistration.class);
        beforeBeanRegistrationAnnotations
                .forEach(annotatedAnnotation -> Classes
                        .instanciate(annotatedAnnotation.parentAnnotation().value())
                        .execute(annotatedAnnotation.annotation(), context)
                );
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
            throws BeansException {

        ExtendedBeanDefinitionRegistryImpl extendedBeanDefinitionRegistry = new ExtendedBeanDefinitionRegistryImpl(registry);

        Set<Annotations.AnnotatedAnnotation<AfterBeanRegistration>> beforeBeanRegistrationAnnotations
                = Annotations.getAnnotationsAnnotatedWith(testClass, AfterBeanRegistration.class);
        beforeBeanRegistrationAnnotations.forEach(annotatedAnnotation -> Classes
                .instanciate(annotatedAnnotation.parentAnnotation().value())
                .execute(annotatedAnnotation.annotation(), extendedBeanDefinitionRegistry)
        );
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
