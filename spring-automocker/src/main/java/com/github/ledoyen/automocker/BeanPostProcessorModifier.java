package com.github.ledoyen.automocker;

import org.springframework.context.ConfigurableApplicationContext;

public interface BeanPostProcessorModifier {

	Object modify(ConfigurableApplicationContext applicationContext, Object originalBeanPostProcessor);
}
