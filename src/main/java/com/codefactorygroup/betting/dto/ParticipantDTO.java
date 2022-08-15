package com.codefactorygroup.betting.dto;

import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.domain.Participant;
import lombok.Builder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
public record ParticipantDTO(Integer id, String name, List<EventDTO> events) {

    public static ParticipantDTO converter(final Participant participant) {
        List<Event> events = Optional.of(participant).map(Participant::getEvents).orElseGet(Collections::emptyList);
        return ParticipantDTO.builder()
                .id(participant.getId())
                .name(participant.getName())
                .events(events
                        .stream()
                        .map(EventDTO::converter)
                        .collect(Collectors.toList()))
                .build();
    }
}
