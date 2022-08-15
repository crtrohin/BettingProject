package com.codefactorygroup.betting.converter;

import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.dto.MarketDTO;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EventDTOtoEventConverter implements Converter<EventDTO, Event>{

    private final ParticipantDtoToParticipantConverter participantDtoToParticipantConverter;

    private final MarketDTOtoMarketConverter marketDTOtoMarketConverter;

    public EventDTOtoEventConverter(ParticipantDtoToParticipantConverter participantDtoToParticipantConverter, MarketDTOtoMarketConverter marketDTOtoMarketConverter) {
        this.participantDtoToParticipantConverter = participantDtoToParticipantConverter;
        this.marketDTOtoMarketConverter = marketDTOtoMarketConverter;
    }

    @Override
    public Event convert(EventDTO source) {
        List<ParticipantDTO> participantDTOS = Optional.of(source).map(EventDTO::participants).orElseGet(Collections::emptyList);
        List<MarketDTO> marketDTOS = Optional.of(source).map(EventDTO::markets).orElseGet(Collections::emptyList);
        return Event
                .builder()
                .id(source.id())
                .name(source.name())
                .startTime(source.startTime())
                .endTime(source.endTime())
                .participants(participantDTOS
                        .stream()
                        .map(participantDtoToParticipantConverter::convert)
                        .collect(Collectors.toList()))
                .markets(marketDTOS
                        .stream()
                        .map(marketDTOtoMarketConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}
