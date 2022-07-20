package com.codefactorygroup.betting.exception;

public class ParticipantAlreadyExistsException extends RuntimeException{

    public ParticipantAlreadyExistsException(Integer participantId) {
        super(String.format("Participant with ID = %d already exists.", participantId));
    }
}
