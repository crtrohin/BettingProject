package com.codefactorygroup.betting.dto;

import com.codefactorygroup.betting.domain.Competition;
import com.codefactorygroup.betting.domain.Event;
import lombok.Builder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Builder
public record CompetitionDTO(Integer id, String name, List<EventDTO> events) {

    public static CompetitionDTO converter(final Competition competition) {
        List<Event> eventsList = Optional.of(competition).map(Competition::getEvents).orElseGet(Collections::emptyList);
        return CompetitionDTO.builder()
                .id(competition.getId())
                .name(competition.getName())
                .events(eventsList
                        .stream()
                        .map(EventDTO::converter)
                        .toList())
                .build();
    }
}
