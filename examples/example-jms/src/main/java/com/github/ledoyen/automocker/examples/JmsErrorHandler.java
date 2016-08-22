package com.github.ledoyen.automocker.examples;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
public class JmsErrorHandler implements ErrorHandler {

	private Throwable lastCatched = null;

	@Override
	public void handleError(Throwable t) {
		lastCatched = t;
	}

	public Optional<Throwable> getLastCatched() {
		return Optional.ofNullable(lastCatched);
	}
}
