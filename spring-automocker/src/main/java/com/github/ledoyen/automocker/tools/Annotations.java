package com.github.ledoyen.automocker.tools;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.immutables.value.Value;

public final class Annotations {
    private Annotations() {
    }

    /**
     * @return all {@link Annotation Annotations} found on given {@link AnnotatedElement} that have a meta-annotation of given type {@link A}
     */
    // TODO make this recursive on superclass and interfaces
    public static <A extends Annotation> Set<AnnotatedAnnotation<A>> getAnnotationsAnnotatedWith(AnnotatedElement annotatedElement, Class<A> annotationType) {
        return getAnnotationsAnnotatedWith(annotatedElement, annotationType, new LinkedHashSet<>());
    }

    private static <A extends Annotation> Set<AnnotatedAnnotation<A>> getAnnotationsAnnotatedWith(AnnotatedElement annotatedElement, Class<A> annotationType, Set<AnnotatedElement> visited) {
        Set<AnnotatedAnnotation<A>> result = new HashSet<>();

        if (visited.add(annotatedElement)) {
            Arrays.stream(annotatedElement.getDeclaredAnnotations()).forEach(annotation -> {
                Annotation superAnnotation = annotation.annotationType().getAnnotation(annotationType);
                if (superAnnotation != null) {
                    result.add(ImmutableAnnotatedAnnotation.of((A) superAnnotation, annotation));
                } else {
                    result.addAll(getAnnotationsAnnotatedWith(annotation.annotationType(), annotationType, visited));
                }
            });
        }

        return result;
    }

    /**
     * Describe an annotation annotated by another annotation.
     *
     * @param <A> type of the meta-annotation
     */
    @Value.Immutable
    public interface AnnotatedAnnotation<A extends Annotation> {

        @Value.Parameter
        A parentAnnotation();

        @Value.Parameter
        Annotation annotation();
    }
}
