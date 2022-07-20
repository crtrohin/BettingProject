package com.codefactorygroup.betting.exception;

public class NoSuchParticipantExistsException extends RuntimeException {
    public NoSuchParticipantExistsException(Integer participantId) {
        super(String.format("No participant with ID = %d was found.", participantId));
    }
}
