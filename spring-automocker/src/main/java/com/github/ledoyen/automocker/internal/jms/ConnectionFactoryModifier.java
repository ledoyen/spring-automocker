package com.github.ledoyen.automocker.internal.jms;

import java.util.function.BiConsumer;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.github.ledoyen.automocker.BeanDefinitionModifier;
import com.github.ledoyen.automocker.Need;
import com.github.ledoyen.automocker.jms.JmsMock;
import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockConnectionFactory;

@Need(classname = "com.mockrunner.mock.jms.MockConnectionFactory", jar = "mockrunner-jms")
public class ConnectionFactoryModifier implements BeanDefinitionModifier {

	private static final String DESTINATION_MANAGER_BEAN_NAME = "destinationManager";
	private static final String CONFIGURATION_MANAGER_BEAN_NAME = "configurationManager";

	private String modifiedBeanName = null;

	@Override
	public void modify(Class<?> target, String beanName, AbstractBeanDefinition definition, BiConsumer<String, BeanDefinition> additionalDefinitionsRegistry) {
		if (modifiedBeanName == null || beanName.equals(modifiedBeanName)) {
			modifiedBeanName = beanName;
		} else {
			throw new IllegalStateException("Multiple " + ConnectionFactory.class.getName() + " defined, only one is supported for now");
		}
		definition.setBeanClass(MockConnectionFactory.class);
		definition.setFactoryMethodName(null);
		definition.setFactoryBeanName(null);
		definition.setPropertyValues(null);
		ConstructorArgumentValues cav = new ConstructorArgumentValues();
		cav.addIndexedArgumentValue(0, new RuntimeBeanReference(DESTINATION_MANAGER_BEAN_NAME));
		cav.addIndexedArgumentValue(1, new RuntimeBeanReference(CONFIGURATION_MANAGER_BEAN_NAME));
		definition.setConstructorArgumentValues(cav);

		additionalDefinitionsRegistry.accept(DESTINATION_MANAGER_BEAN_NAME, new RootBeanDefinition(DestinationManager.class));
		additionalDefinitionsRegistry.accept(CONFIGURATION_MANAGER_BEAN_NAME, new RootBeanDefinition(ConfigurationManager.class));
	}

	public void afterModifications(DefaultListableBeanFactory beanFactory) {
		AbstractBeanDefinition jmsMockBeanDefinition = new RootBeanDefinition(JmsMock.class);
		ConstructorArgumentValues cav = new ConstructorArgumentValues();
		cav.addGenericArgumentValue(new RuntimeBeanReference(modifiedBeanName));
		cav.addGenericArgumentValue(new RuntimeBeanReference(DESTINATION_MANAGER_BEAN_NAME));
		jmsMockBeanDefinition.setConstructorArgumentValues(cav);
		beanFactory.registerBeanDefinition("jmsMock", jmsMockBeanDefinition);

		beanFactory.registerBeanDefinition("destinationManagerLocator", new RootBeanDefinition(DestinationManagerResetter.class));
	}
}
