package com.codefactorygroup.betting.dto;

import com.codefactorygroup.betting.domain.Participant;
import lombok.Builder;

@Builder
public record ParticipantDTO(String name) {

    public static ParticipantDTO converter(final Participant participant) {
        return ParticipantDTO.builder()
                .name(participant.getName())
                .build();
    }
}
