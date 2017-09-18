package com.github.ledoyen.automocker.examples;

import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
public class JmsErrorHandler implements ErrorHandler {

    private int errorCount = 0;

    @Override
    public void handleError(Throwable t) {
        errorCount++;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void reset() {
        errorCount = 0;
    }
}
