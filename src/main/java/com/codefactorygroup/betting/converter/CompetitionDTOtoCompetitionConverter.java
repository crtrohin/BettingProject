package com.codefactorygroup.betting.converter;

import com.codefactorygroup.betting.domain.Competition;
import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.dto.EventDTO;
import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class CompetitionDTOtoCompetitionConverter implements Converter<CompetitionDTO, Competition> {

    private final EventDTOtoEventConverter eventDTOtoEventConverter;

    public CompetitionDTOtoCompetitionConverter(EventDTOtoEventConverter eventDTOtoEventConverter) {
        this.eventDTOtoEventConverter = eventDTOtoEventConverter;
    }

    @Override
    public Competition convert(CompetitionDTO source) {
        List<EventDTO> eventDTOS = Optional.of(source).map(CompetitionDTO::events).orElseGet(Collections::emptyList);

        return Competition
                .builder()
                .id(source.id())
                .name(source.name())
                .events(eventDTOS
                        .stream()
                        .map(eventDTOtoEventConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}
