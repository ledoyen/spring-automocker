package com.github.ledoyen.automocker.internal;

import java.util.function.BiConsumer;

import org.mockito.Mockito;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

import com.github.ledoyen.automocker.BeanDefinitionModifier;
import com.github.ledoyen.automocker.Need;

@Need(classname = "org.mockito.Mockito", jar = { "mockito-core", "mockito-all" })
public class MockitoBeanDefinitionModifier implements BeanDefinitionModifier {

	@Override
	public void modify(Class<?> target, String beanName, AbstractBeanDefinition definition, BiConsumer<String, BeanDefinition> additionalDefinitionsRegistry) {
		definition.setBeanClass(Mockito.class);
		definition.setFactoryMethodName("mock");
		definition.setFactoryBeanName(null);
		definition.setPropertyValues(null);
		ConstructorArgumentValues cav = new ConstructorArgumentValues();
		cav.addGenericArgumentValue(target);
		definition.setConstructorArgumentValues(cav);
	}
}
