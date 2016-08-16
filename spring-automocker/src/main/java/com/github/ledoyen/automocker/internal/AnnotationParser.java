package com.github.ledoyen.automocker.internal;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.ledoyen.automocker.Need;
import com.github.ledoyen.automocker.configuration.AutomockerConfiguration;

public interface AnnotationParser<T extends Annotation> {

	void parse(T annotation, AutomockerConfiguration configuration);

	default void checkNeededClassesAreAvailable(Annotation origin, Class<?> annotatedWithNeed) {
		Arrays.asList(annotatedWithNeed.getAnnotationsByType(Need.class)).forEach(need -> {
			try {
				Class.forName(need.classname());
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException("Automocker " + origin + " is missing class [" + need.classname() + "], make sure "
						+ Arrays.asList(need.jar()).stream().map(j -> j + ".jar").collect(Collectors.joining(" or ")) + " is in the test classpath", e);
			}
		});
	}
}
