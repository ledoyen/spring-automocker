package com.github.ledoyen.automocker;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.ledoyen.automocker.extension.MockDatasource;
import com.github.ledoyen.automocker.extension.MockSpringWeb;
import com.github.ledoyen.automocker.extension.MockPropertySource;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited

@MockPropertySource
@MockDatasource
@MockSpringWeb
public @interface SpringAutomocker {

}
