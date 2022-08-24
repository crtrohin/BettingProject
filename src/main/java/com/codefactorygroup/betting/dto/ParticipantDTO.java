package com.codefactorygroup.betting.dto;

import com.codefactorygroup.betting.domain.Participant;
import lombok.Builder;

@Builder
public record ParticipantDTO(Integer id, String name) {

    public static ParticipantDTO converter(final Participant participant) {
        return ParticipantDTO.builder()
                .id(participant.getId())
                .name(participant.getName())
                .build();
    }
}
