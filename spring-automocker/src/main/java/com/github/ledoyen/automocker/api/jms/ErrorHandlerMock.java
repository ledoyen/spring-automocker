package com.github.ledoyen.automocker.api.jms;

import java.util.Optional;

import org.springframework.util.ErrorHandler;

import com.github.ledoyen.automocker.api.Resettable;

public class ErrorHandlerMock implements ErrorHandler, Resettable {

    private final Optional<ErrorHandler> delegate;
    private Throwable lastCatched = null;

    ErrorHandlerMock(Optional<ErrorHandler> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void handleError(Throwable t) {
        lastCatched = t;
        delegate.ifPresent(e -> e.handleError(t));
    }

    public Optional<Throwable> getLastCatched() {
        return Optional.ofNullable(lastCatched);
    }

    @Override
    public void reset() {
        this.lastCatched = null;
    }
}
