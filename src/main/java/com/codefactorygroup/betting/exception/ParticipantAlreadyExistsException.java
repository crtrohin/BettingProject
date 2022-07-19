package com.codefactorygroup.betting.exception;

public class ParticipantAlreadyExistsException extends RuntimeException{
    private String message;

    public ParticipantAlreadyExistsException() {
    }

    public ParticipantAlreadyExistsException(String msg) {
        super(msg);
        this.message = msg;
    }

}
