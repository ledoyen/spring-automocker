package com.github.ledoyen.automocker.internal;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.ClassUtils;

public abstract class BeanDefinitions {

	private static final ClassLoader BEAN_CLASS_LOADER = ClassUtils.getDefaultClassLoader();

	private BeanDefinitions() {
	}

	public static Class<?> extractClass(BeanDefinition beanDefinition) {
		if (beanDefinition instanceof AbstractBeanDefinition) {
			AbstractBeanDefinition abd = (AbstractBeanDefinition) beanDefinition;
			try {
				Class<?> beanClazz = abd.resolveBeanClass(BEAN_CLASS_LOADER);
				MethodMetadata factoryMetadata = getMethodMetadata(beanDefinition);
				if (beanClazz != null) {
					return beanClazz;
				} else if (factoryMetadata != null) {
					return Class.forName(factoryMetadata.getReturnTypeName());
				} else {
					throw new IllegalStateException("Unable to resolve target class for BeanDefinition [" + beanDefinition + "]");
				}
			} catch (ClassNotFoundException ex) {
				throw new IllegalStateException("Cannot load class: " + ex.getMessage(), ex);
			}
		}
		return null;
	}

	private static MethodMetadata getMethodMetadata(BeanDefinition beanDef) {
		return AnnotatedBeanDefinition.class.isAssignableFrom(beanDef.getClass()) ? ((AnnotatedBeanDefinition) beanDef).getFactoryMethodMetadata() : null;
	}
}
