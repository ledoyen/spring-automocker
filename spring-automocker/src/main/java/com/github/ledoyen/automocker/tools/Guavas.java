package com.github.ledoyen.automocker.tools;

import java.util.function.Function;

public class Guavas {

    public static <T, R> com.google.common.base.Function<T, R> fromThrowing(ThrowingFunction<T, R> throwing) {
        return fromJava(ThrowingFunction.silent(throwing));
    }

    public static <T, R> com.google.common.base.Function<T, R> fromJava(Function<T, R> function) {
        return t -> function.apply(t);
    }
}
