package com.github.ledoyen.automocker.configuration;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.github.ledoyen.automocker.BeanDefinitionModifier;
import com.github.ledoyen.automocker.BeanPostProcessorModifier;
import com.github.ledoyen.automocker.tools.Tuple;

public class AutomockerConfiguration {

	private Map<Class<?>, Class<? extends BeanDefinitionModifier>> beanModificationDefinitions = new HashMap<>();

	private Map<Class<?>, BeanDefinitionModifier> beanModifiers = new HashMap<>();

	private Map<Class<?>, BeanPostProcessorModifier> beanPostProcessorModifications = new HashMap<>();

	public void addBeanModification(Class<?> target,
			Class<? extends BeanDefinitionModifier> beanDefinitionClass) {
		beanModificationDefinitions.put(target, beanDefinitionClass);
	}

	public Optional<Tuple<Class<?>, BeanDefinitionModifier>> getModifier(Class<?> target) {
		Optional<Tuple<Class<?>, BeanDefinitionModifier>> modifierAndTarget = getFromCache(target);
		if (!modifierAndTarget.isPresent()) {
			modifierAndTarget = getFromDefinitions(target);
		}
		return modifierAndTarget;
	}

	private Optional<Tuple<Class<?>, BeanDefinitionModifier>> getFromCache(Class<?> target) {
		return beanModifiers.entrySet()
				.stream()
				.filter(e -> match(e, target))
				.findFirst()
				.map(e -> Tuple.of(e.getKey(), e.getValue()));
	}

	private Optional<Tuple<Class<?>, BeanDefinitionModifier>> getFromDefinitions(Class<?> target) {
		return beanModificationDefinitions.entrySet()
				.stream()
				.filter(e -> match(e, target))
				.findFirst()
				.map(e -> {
					checkNeededClassesAreAvailable(target.getSimpleName(), e.getValue());
					BeanDefinitionModifier modifier = BeanUtils.instantiate(e.getValue());
					beanModifiers.put(e.getKey(), modifier);
					return Tuple.of(target, modifier);
				});
	}

	private void checkNeededClassesAreAvailable(String origin, Class<?> annotatedWithNeed) {
		Arrays.asList(annotatedWithNeed.getAnnotationsByType(Need.class))
				.forEach(need -> {
					try {
						Class.forName(need.classname());
					} catch (ClassNotFoundException e) {
						throw new IllegalStateException("Automocker is missing class [" + need.classname()
								+ "] to handle [" + origin + "], make sure " + Arrays.asList(need.jar())
										.stream()
										.map(j -> j + ".jar")
										.collect(Collectors.joining(" or "))
								+ " is in the test classpath", e);
					}
				});
	}

	@SuppressWarnings("unchecked")
	private static boolean match(Entry<Class<?>, ?> classKeyEntry, Class<?> target) {
		final boolean match;
		if (classKeyEntry.getKey()
				.isAnnotation()) {
			match = AnnotationUtils.findAnnotation(target,
					(Class<? extends Annotation>) classKeyEntry.getKey()) != null;
		} else {
			match = classKeyEntry.getKey()
					.isAssignableFrom(target);
		}
		return match;
	}

	public void addPostProcessorModification(Class<?> targetClass, BeanPostProcessorModifier instantiate) {
		beanPostProcessorModifications.put(targetClass, instantiate);
	}
}
