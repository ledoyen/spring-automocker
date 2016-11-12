package com.github.ledoyen.automocker.tools;

import java.util.function.Function;

@FunctionalInterface
public interface ThrowingFunction<T, R> {

	R apply(T t) throws Exception;

	static <T, R> Function<T, R> silent(ThrowingFunction<T, R> throwing) {
		return silent(throwing, e -> {
			throw LamdaLuggageException.wrap(e);
		});
	}

	static <T, R> Function<T, R> silent(ThrowingFunction<T, R> throwing,
			Function<Exception, R> errorHandler) {
		return t -> {
			try {
				return throwing.apply(t);
			} catch (Exception e) {
				return errorHandler.apply(e);
			}
		};
	}
}
