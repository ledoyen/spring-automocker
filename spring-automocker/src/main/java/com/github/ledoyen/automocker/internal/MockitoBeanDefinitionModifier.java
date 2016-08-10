package com.github.ledoyen.automocker.internal;

import org.mockito.Mockito;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

import com.github.ledoyen.automocker.BeanDefinitionModifier;
import com.github.ledoyen.automocker.Need;

@Need("org.mockito.Mockito")
public class MockitoBeanDefinitionModifier implements BeanDefinitionModifier {

	@Override
	public BeanDefinition modify(Class<?> target, AbstractBeanDefinition definition) {
		definition.setBeanClass(Mockito.class);
		definition.setFactoryMethodName("mock");
		definition.setFactoryBeanName(null);
		definition.setPropertyValues(null);
		ConstructorArgumentValues cav = new ConstructorArgumentValues();
		cav.addGenericArgumentValue(target);
		definition.setConstructorArgumentValues(cav);
		return definition;
	}
}
