package com.github.ledoyen.automocker.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

class AutomockerPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    static final String BEAN_NAME = AutomockerPostProcessor.class.getName();

    private final Class<?> testClass;

    AutomockerPostProcessor(Class<?> testClass, ConfigurableApplicationContext context) {
        this.testClass = testClass;
        context.addProtocolResolver(new MockPropertiesProtocolResolver());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
            throws BeansException {
        try {
            String[] names = registry.getBeanDefinitionNames();
            for (String name : names) {
                BeanDefinition definition = registry.getBeanDefinition(name);
                // TODO apply context modifications here
                System.out.println(definition);
            }
        } catch (NoSuchBeanDefinitionException ex) {
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
