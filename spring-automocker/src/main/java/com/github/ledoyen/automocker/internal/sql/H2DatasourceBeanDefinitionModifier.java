package com.github.ledoyen.automocker.internal.sql;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

import com.github.ledoyen.automocker.BeanDefinitionModifier;
import com.github.ledoyen.automocker.Need;

@Need(classname = "org.h2.jdbcx.JdbcConnectionPool", jar = "h2")
public class H2DatasourceBeanDefinitionModifier implements BeanDefinitionModifier {

	@Override
	public void modify(Class<?> target, String beanName, AbstractBeanDefinition definition) {
		definition.setBeanClass(JdbcDataSource.class);
		definition.setFactoryBeanName(null);
		definition.setFactoryMethodName(null);
		MutablePropertyValues mpv = new MutablePropertyValues();
		mpv.add("url", "jdbc:h2:mem:" + beanName + ";DB_CLOSE_DELAY=-1");
		definition.setPropertyValues(mpv);
		definition.setConstructorArgumentValues(null);
	}
}
