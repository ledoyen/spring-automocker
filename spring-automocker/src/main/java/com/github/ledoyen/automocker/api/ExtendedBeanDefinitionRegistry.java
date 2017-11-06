package com.github.ledoyen.automocker.api;

import java.util.Set;

import org.immutables.value.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

public interface ExtendedBeanDefinitionRegistry {

    Set<BeanDefinitionMetadata> getBeanDefinitionsForClass(Class<?> clazz);

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    default String registerBeanDefinition(Class<?> beanClass) {
        String beanName = "Automocker" + beanClass.getSimpleName();
        registerBeanDefinition(beanName, new RootBeanDefinition(beanClass));
        return beanName;
    }

    @Value.Immutable
    interface BeanDefinitionMetadata {
        String name();
        Class<?> beanClass();
        AbstractBeanDefinition beanDefinition();
        BeanDefinitionModifier beanDefinitionModifier();
    }

    class BeanDefinitionModifier {

        private final AbstractBeanDefinition beanDefinition;

        public BeanDefinitionModifier(AbstractBeanDefinition beanDefinition) {
            this.beanDefinition = beanDefinition;
        }

        public BeanDefinitionModifier reset() {
            setFactoryBeanName(null);
            setFactoryMethodName(null);
            removeConstructorArgumentValues();
            removePropertyValues();
            return this;
        }
        public BeanDefinitionModifier setFactoryBeanName(String factoryBeanName) {
            beanDefinition.setFactoryBeanName(factoryBeanName);
            return this;
        }

        public BeanDefinitionModifier setFactoryMethodName(String factoryMethodName) {
            beanDefinition.setFactoryMethodName(factoryMethodName);
            return this;
        }

        public BeanDefinitionModifier addConstructorIndexedArgumentValue(int index, Object value) {
            beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(index, value);
            return this;
        }

        public BeanDefinitionModifier setBeanClass(Class<?> beanClass) {
            beanDefinition.setBeanClass(beanClass);
            return this;
        }

        public BeanDefinitionModifier removeConstructorArgumentValues() {
            beanDefinition.setConstructorArgumentValues(null);
            return this;
        }

        public BeanDefinitionModifier removePropertyValues() {
            beanDefinition.setPropertyValues(null);
            return this;
        }

        public BeanDefinitionModifier addPropertyValue(String propertyName, Object propertyValue) {
            beanDefinition.getPropertyValues().add(propertyName, propertyValue);
            return this;
        }
    }
}
