package com.codefactorygroup.betting.converterTests;

import com.codefactorygroup.betting.converter.*;
import com.codefactorygroup.betting.domain.Competition;
import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.dto.EventDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompetitionConverterTests {

    private EventDTOtoEventConverter eventDTOtoEventConverter = mock(EventDTOtoEventConverter.class);
    private CompetitionDTOtoCompetitionConverter competitionDtoToCompetitionConverter = new CompetitionDTOtoCompetitionConverter(eventDTOtoEventConverter);

    @Test
    void checkCompetitionToCompetitionDtoConversion() {
        CompetitionDTO actualCompetition = CompetitionDTO.converter(new Competition(9, "Atletico Madrid", new ArrayList<>()));
        CompetitionDTO expectedCompetition = new CompetitionDTO(9, "Atletico Madrid", new ArrayList<>());
        assertEquals(actualCompetition, expectedCompetition);
    }

    @Test
    void checkCompetitionDtoToCompetitionConversion() {
        List<EventDTO> events = new ArrayList<>(List.of(new EventDTO(1, "Arsenal vs Barcelona", null, null, null, null)));
        CompetitionDTO competitionDTO = new CompetitionDTO(9, "Atletico Madrid", events);
        List<Event> eventList = new ArrayList<>(List.of(new Event(1, "Arsenal vs Barcelona", null, null, null, null)));

        when(eventDTOtoEventConverter.convert(any()))
                .thenReturn(new Event(1, "Arsenal vs Barcelona", null, null, null, null));
        Competition actualCompetition = competitionDtoToCompetitionConverter.convert(competitionDTO);
        Competition expectedCompetition = new Competition(9, "Atletico Madrid", eventList);
        assertEquals(expectedCompetition.getId(), actualCompetition.getId());
        assertEquals(expectedCompetition.getEvents(), actualCompetition.getEvents());
        assertEquals(expectedCompetition.getName(), actualCompetition.getName());
    }

}
