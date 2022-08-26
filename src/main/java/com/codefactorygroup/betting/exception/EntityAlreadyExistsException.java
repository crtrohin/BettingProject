package com.codefactorygroup.betting.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
