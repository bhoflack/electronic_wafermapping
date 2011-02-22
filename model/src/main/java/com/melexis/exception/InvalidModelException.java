package com.melexis.exception;

public class InvalidModelException extends RuntimeException {

    public InvalidModelException(final String model, final Throwable e) {
        super(String.format("Exception parsing model '%s'", model), e);
    }
}
