package com.github.ledoyen.automocker.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.sql.DataSource;

import com.github.ledoyen.automocker.ModifyBeanDefinition;
import com.github.ledoyen.automocker.internal.sql.H2DatasourceBeanDefinitionModifier;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
// @Inherited
@ModifyBeanDefinition(value = DataSource.class, beanDefinitionModifier = H2DatasourceBeanDefinitionModifier.class)
public @interface MockDatasource {

}
