package com.codefactorygroup.betting.dto;

import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.domain.Market;
import com.codefactorygroup.betting.domain.Participant;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Builder
public record EventDTO(Integer id, String name, List<ParticipantDTO> participants, String startTime, String endTime, List<MarketDTO> markets) {

    public static EventDTO converter(final Event event) {
        List<Participant> eventParticipants = Optional.of(event).map(Event::getParticipants).orElseGet(Collections::emptyList);
        List<Market> eventMarkets = Optional.of(event).map(Event::getMarkets).orElseGet(Collections::emptyList);
        return EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .participants(eventParticipants
                        .stream()
                        .map(ParticipantDTO::converter)
                        .toList())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .markets(eventMarkets
                        .stream()
                        .map(MarketDTO::converter)
                        .toList())
                .build();
    }
}
