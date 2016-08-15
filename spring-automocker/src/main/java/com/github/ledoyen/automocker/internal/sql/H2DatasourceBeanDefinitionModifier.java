package com.github.ledoyen.automocker.internal.sql;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

import com.github.ledoyen.automocker.BeanDefinitionModifier;
import com.github.ledoyen.automocker.Need;

@Need(classname = "org.h2.jdbcx.JdbcConnectionPool", jar = "h2")
public class H2DatasourceBeanDefinitionModifier implements BeanDefinitionModifier {

	@Override
	public BeanDefinition modify(Class<?> target, AbstractBeanDefinition definition) {
		definition.setBeanClass(MockDataSourceFactory.class);
		definition.setFactoryBeanName(null);
		definition.setFactoryMethodName(null);
		definition.setPropertyValues(null);
		definition.setConstructorArgumentValues(null);
		return definition;
	}

}
