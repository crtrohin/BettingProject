package com.codefactorygroup.betting.exception;

public class NoSuchEntityExistsException extends RuntimeException {
    public NoSuchEntityExistsException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
