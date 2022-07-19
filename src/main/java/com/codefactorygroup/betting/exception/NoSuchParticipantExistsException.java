package com.codefactorygroup.betting.exception;

public class NoSuchParticipantExistsException extends RuntimeException{
    private String message;

    public NoSuchParticipantExistsException() {
    }

    public NoSuchParticipantExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}
