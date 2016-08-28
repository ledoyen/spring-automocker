package com.github.ledoyen.automocker.internal;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class AnnotationConfigUtils {

	public static <K, A extends Annotation> Map<K, A> collectAnnotations(Class<?> clazz, Class<A> annotationType, Function<A, Collection<K>> keysExtravtor) {
		Map<K, A> collected = new HashMap<>();
		collectAnnotations(clazz, annotationType, collected, keysExtravtor, new HashSet<>());
		return collected;
	}

	private static <K, A extends Annotation> void collectAnnotations(Class<?> clazz, Class<A> annotationType, Map<K, A> collected, Function<A, Collection<K>> keysExtractor,
			Set<Class<?>> visited) {
		if (visited.add(clazz)) {
			for (Annotation annotation : clazz.getAnnotations()) {
				Class<? extends Annotation> annClass = annotation.annotationType();
				if (!annClass.getName().startsWith("java") && !annClass.equals(annotationType)) {
					collectAnnotations(annClass, annotationType, collected, keysExtractor, visited);
				}
			}
			Arrays.asList(clazz.getAnnotationsByType(annotationType)).forEach(a -> {
				keysExtractor.apply(a).forEach(k -> collected.putIfAbsent(k, a));
			});
		}
	}
}
