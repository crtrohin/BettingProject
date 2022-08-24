package com.codefactorygroup.betting.serviceTests;

import com.codefactorygroup.betting.converter.CompetitionDTOtoCompetitionConverter;
import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.domain.Competition;
import com.codefactorygroup.betting.domain.Sport;
import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.CompetitionRepository;
import com.codefactorygroup.betting.repository.SportRepository;
import com.codefactorygroup.betting.service.implementations.CompetitionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompetitionServiceTests {

    @Mock
    private CompetitionDTOtoCompetitionConverter competitionDtoToCompetitionConverter;
    @Mock
    private CompetitionRepository competitionRepository;

    @Mock
    private SportRepository sportRepository;

    @InjectMocks
    private CompetitionServiceImpl competitionService;

    @Test
    void getCompetitionShouldReturnCompetition() {
        Optional<Competition> optionalCompetition = Optional.of(Competition
                .builder()
                .id(10)
                .name("FIFA U-20 World Cup")
                .build());

        when(competitionRepository.findById(10)).thenReturn(optionalCompetition);

        CompetitionDTO competitionDTO = competitionService.getCompetition(10);
        Competition competition = optionalCompetition.get();

        assertThat(competitionDTO).isNotNull();
        assertEquals(competition.getId(), competitionDTO.id());
        assertEquals(competition.getName(), competitionDTO.name());
    }

    @Test
    void getCompetitionShouldReturnException() {
        when(competitionRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> competitionService.getCompetition(10));
    }

    @Test
    void getAllCompetitionsShouldReturnCompetitions() {
        Competition competition1 = Competition
                .builder()
                .id(1)
                .name("Bundesliga")
                .build();

        Competition competition2 = Competition
                .builder()
                .id(2)
                .name("European Championship")
                .build();

        Competition competition3 = Competition
                .builder()
                .id(3)
                .name("AFC Asian Cup")
                .build();

        List<Competition> competitions = List.of(competition1, competition2, competition3);

        when(competitionRepository.findAll()).thenReturn(competitions);

        List<CompetitionDTO> competitionDTOS = competitionService.getAllCompetitions();

        assertEquals(competitionDTOS.get(0).id(), competition1.getId());
        assertEquals(competitionDTOS.get(0).name(), competition1.getName());

        assertEquals(competitionDTOS.get(1).id(), competition2.getId());
        assertEquals(competitionDTOS.get(1).name(), competition2.getName());

        assertEquals(competitionDTOS.get(2).id(), competition3.getId());
        assertEquals(competitionDTOS.get(2).name(), competition3.getName());
    }


    @Test
    void addCompetitionShouldReturnCompetition() {
        EventDTO eventDTO = EventDTO
                .builder()
                .id(1)
                .name("Dortmund vs Bayern")
                .startTime("Aug 27, 16:30")
                .endTime("Aug 27, 18:00")
                .participants(Collections.emptyList())
                .markets(Collections.emptyList())
                .build();

        Event event = Event
                .builder().id(1)
                .name("Dortmund vs Bayern")
                .startTime("Aug 27, 16:30")
                .endTime("Aug 27, 18:00")
                .participants(Collections.emptyList())
                .markets(Collections.emptyList())
                .build();

        List<EventDTO> eventDTOS = List.of(eventDTO);
        List<Event> events = List.of(event);

        CompetitionDTO competitionDTO = CompetitionDTO
                .builder()
                .id(1)
                .name("Bundesliga")
                .events(eventDTOS)
                .build();

        Competition competition1 = Competition
                .builder()
                .id(1)
                .name("Bundesliga")
                .events(events)
                .build();

        Competition competition2 = Competition
                .builder()
                .id(2)
                .name("European Championship")
                .build();

        List<Competition> competitions = new ArrayList<>();
        competitions.add(competition2);

        Sport sport = Sport
                .builder()
                .id(1)
                .name("Football")
                .competitions(competitions)
                .build();

        when(sportRepository.findById(1)).thenReturn(Optional.of(sport));
        when(competitionRepository.existsCompetitionBySportIdAndName(1, "Bundesliga")).thenReturn(false);
        when(competitionDtoToCompetitionConverter.convert(competitionDTO)).thenReturn(competition1);
        when(competitionRepository.save(competition1)).thenReturn(competition1);

        CompetitionDTO savedCompetition = competitionService.addCompetition(1, competitionDTO);

        assertEquals(competitionDTO, savedCompetition);
    }

    @Test
    void addCompetitionShouldReturnExceptionNoSportExists() {
        EventDTO eventDTO = EventDTO
                .builder()
                .id(1)
                .name("Dortmund vs Bayern")
                .startTime("Aug 27, 16:30")
                .endTime("Aug 27, 18:00")
                .participants(Collections.emptyList())
                .markets(Collections.emptyList())
                .build();

        List<EventDTO> eventDTOS = List.of(eventDTO);

        CompetitionDTO competitionDTO = CompetitionDTO
                .builder()
                .id(1)
                .name("Bundesliga")
                .events(eventDTOS)
                .build();

        when(sportRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> competitionService.addCompetition(1, competitionDTO));
    }

    @Test
    void addCompetitionShouldReturnExceptionCompetitionAlreadyExists() {
        EventDTO eventDTO = EventDTO
                .builder()
                .id(1)
                .name("Dortmund vs Bayern")
                .startTime("Aug 27, 16:30")
                .endTime("Aug 27, 18:00")
                .participants(Collections.emptyList())
                .markets(Collections.emptyList())
                .build();

        List<EventDTO> eventDTOS = List.of(eventDTO);

        CompetitionDTO competitionDTO = CompetitionDTO
                .builder()
                .id(1)
                .name("Bundesliga")
                .events(eventDTOS)
                .build();

        Competition competition2 = Competition
                .builder()
                .id(2)
                .name("European Championship")
                .build();

        List<Competition> competitions = new ArrayList<>();
        competitions.add(competition2);

        Sport sport = Sport
                .builder()
                .id(1)
                .name("Football")
                .competitions(competitions)
                .build();

        when(sportRepository.findById(1)).thenReturn(Optional.of(sport));
        when(competitionRepository.existsCompetitionBySportIdAndName(1, "Bundesliga")).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> competitionService.addCompetition(1, competitionDTO));
    }

    @Test
    void updateCompetitionShouldReturnCompetition() {
        Competition competitionFromDb = Competition
                .builder()
                .id(1)
                .name("European Championship")
                .build();

        Event event = Event
                .builder()
                .id(1)
                .name("Dortmund vs Bayern")
                .startTime("Aug 27, 16:30")
                .endTime("Aug 27, 18:00")
                .participants(Collections.emptyList())
                .markets(Collections.emptyList())
                .build();

        List<Event> events = List.of(event);

        Competition updatedCompetition = Competition
                .builder()
                .id(1)
                .name("Bundesliga")
                .events(events)
                .build();

        EventDTO eventDTO = EventDTO
                .builder()
                .id(1)
                .name("Dortmund vs Bayern")
                .startTime("Aug 27, 16:30")
                .endTime("Aug 27, 18:00")
                .participants(Collections.emptyList())
                .markets(Collections.emptyList())
                .build();

        List<EventDTO> eventDTOS = List.of(eventDTO);

        CompetitionDTO toUpdateCompetitionDTO = CompetitionDTO
                .builder()
                .id(1)
                .name("Bundesliga")
                .events(eventDTOS)
                .build();

        when(competitionRepository.findById(1)).thenReturn(Optional.of(competitionFromDb));
        when(competitionRepository.save(updatedCompetition)).thenReturn(updatedCompetition);

        CompetitionDTO resultedCompetitionDTO = competitionService.updateCompetition(toUpdateCompetitionDTO, 1);

        assertEquals(resultedCompetitionDTO, toUpdateCompetitionDTO);
    }

    @Test
    void updateCompetitionShouldReturnException() {
        CompetitionDTO toUpdateCompetitionDTO = CompetitionDTO
                .builder()
                .id(1)
                .name("Arsenal")
                .build();

        when(competitionRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> competitionService.updateCompetition(toUpdateCompetitionDTO, 1));
    }


    @Test
    void deleteCompetition() {
        when(competitionRepository.existsById(1)).thenReturn(true);

        competitionService.deleteCompetition(1);

        // check if the method was called
        verify(competitionRepository).deleteById(1);
    }


    @Test
    void deleteCompetitionShouldReturnException() {
        when(competitionRepository.existsById(1)).thenReturn(false);

        assertThrows(NoSuchEntityExistsException.class, () -> competitionService.deleteCompetition(1));
    }

}
