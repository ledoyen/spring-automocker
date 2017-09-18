package com.github.ledoyen.automocker.context;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.github.ledoyen.automocker.api.ExtendedBeanDefinitionRegistry;
import com.github.ledoyen.automocker.api.ImmutableBeanDefinitionMetadata;

class ExtendedBeanDefinitionRegistryImpl implements ExtendedBeanDefinitionRegistry {

    private final BeanDefinitionRegistry registry;

    ExtendedBeanDefinitionRegistryImpl(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Set<BeanDefinitionMetadata> getBeanDefinitionsForClass(Class<?> clazz) {
        Set<BeanDefinitionMetadata> result = new HashSet<>();

        for (String beanName : registry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
            if (beanDefinition instanceof AbstractBeanDefinition) {
                Class<?> beanClass = BeanDefinitions.extractClass((AbstractBeanDefinition) beanDefinition);
                if (clazz.isAssignableFrom(beanClass)) {
                    result.add(ImmutableBeanDefinitionMetadata.builder()
                            .name(beanName)
                            .beanClass(beanClass)
                            .beanDefinition((AbstractBeanDefinition) beanDefinition)
                            .beanDefinitionModifier(new BeanDefinitionModifier((AbstractBeanDefinition) beanDefinition))
                            .build());
                }
            }
        }

        return result;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        registry.registerBeanDefinition(beanName, beanDefinition);
    }
}
