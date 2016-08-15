package com.github.ledoyen.automocker.internal;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.github.ledoyen.automocker.AutomockerConfiguration;
import com.github.ledoyen.automocker.ModifyBeanDefinition;
import com.github.ledoyen.automocker.Need;

public class ModifyBeanDefinitionParser implements AnnotationParser<ModifyBeanDefinition> {

	@Override
	public void parse(ModifyBeanDefinition annotation, AutomockerConfiguration configuration) {
		Arrays.asList(annotation.beanDefinitionModifier().getAnnotationsByType(Need.class)).forEach(need -> {
			try {
				Class.forName(need.classname());
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException("Automocker " + annotation + " is missing class [" + need.classname() + "], make sure "
						+ Arrays.asList(need.jar()).stream().map(j -> j + ".jar").collect(Collectors.joining(" or ")) + " is in the test classpath", e);
			}
		});

		configuration.addBeanModification(annotation.value(), BeanUtils.instantiate(annotation.beanDefinitionModifier()));
	}
}
