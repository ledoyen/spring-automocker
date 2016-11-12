package com.github.ledoyen.automocker.tools;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T> {

	void accept(T t) throws Exception;

	static <T> Consumer<T> silent(ThrowingConsumer<T> throwing) {
		return t -> {
			try {
				throwing.accept(t);
			} catch (Exception e) {
				throw LamdaLuggageException.wrap(e);
			}
		};
	}
}
