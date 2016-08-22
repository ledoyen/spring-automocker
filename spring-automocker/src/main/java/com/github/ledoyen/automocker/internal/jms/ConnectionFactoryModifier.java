package com.github.ledoyen.automocker.internal.jms;

import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.github.ledoyen.automocker.BeanDefinitionModifier;
import com.github.ledoyen.automocker.Need;
import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockConnectionFactory;

@Need(classname = "com.mockrunner.mock.jms.MockConnectionFactory", jar = "mockrunner-jms")
public class ConnectionFactoryModifier implements BeanDefinitionModifier {

	private static final String DESTINATION_MANAGER_BEAN_NAME = "destinationManager";
	private static final String CONFIGURATION_MANAGER_BEAN_NAME = "configurationManager";

	@Override
	public void modify(Class<?> target, String beanName, AbstractBeanDefinition definition) {
		definition.setBeanClass(MockConnectionFactory.class);
		definition.setFactoryMethodName(null);
		definition.setFactoryBeanName(null);
		definition.setPropertyValues(null);
		ConstructorArgumentValues cav = new ConstructorArgumentValues();
		cav.addIndexedArgumentValue(0, new RuntimeBeanReference(DESTINATION_MANAGER_BEAN_NAME));
		cav.addIndexedArgumentValue(1, new RuntimeBeanReference(CONFIGURATION_MANAGER_BEAN_NAME));
		definition.setConstructorArgumentValues(cav);
	}

	public void afterModifications(DefaultListableBeanFactory beanFactory) {
		beanFactory.registerBeanDefinition(DESTINATION_MANAGER_BEAN_NAME, new RootBeanDefinition(DestinationManager.class));
		beanFactory.registerBeanDefinition(CONFIGURATION_MANAGER_BEAN_NAME, new RootBeanDefinition(ConfigurationManager.class));
	}
}
