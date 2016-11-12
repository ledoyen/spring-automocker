package com.github.ledoyen.automocker.tools;

@FunctionalInterface
public interface ThrowingRunnable {

	void run() throws Exception;

	static Runnable silent(ThrowingRunnable throwing) {
		return () -> {
			try {
				throwing.run();
			} catch (Exception e) {
				throw LamdaLuggageException.wrap(e);
			}
		};
	}
}
