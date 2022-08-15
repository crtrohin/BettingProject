package com.codefactorygroup.betting.controllerTests;

import com.codefactorygroup.betting.controller.CompetitionController;
import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.service.CompetitionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompetitionControllerTests {

    private final CompetitionService competitionService = mock(CompetitionService.class);
    private final CompetitionController competitionController = new CompetitionController(competitionService);


    @Test
    void getCompetitionShouldReturnCompetition() {
        List<EventDTO> events = new ArrayList<>() {{
            add(new EventDTO(1, "Arsenal vs Barcelona", null, null, null, null));
        }};
        CompetitionDTO competitionDTO = new CompetitionDTO(10, "Champions League", events);
        when(competitionService.getCompetition(10)).thenReturn(competitionDTO);

        var response = competitionController.getCompetition(10);

        assertEquals(competitionDTO, response);
    }

    @Test
    void getCompetitionShouldReturnException() {
        when(competitionService.getCompetition(10)).thenThrow(new NoSuchEntityExistsException(String.format("No competition with ID = %d was found.", 10)));

        NoSuchEntityExistsException noSuchEntityExistsException = assertThrows(NoSuchEntityExistsException.class,
                () -> {competitionController.getCompetition(10);
                });

        assertEquals("No competition with ID = 10 was found.", noSuchEntityExistsException.getMessage());
    }

    @Test
    void addCompetitionShouldReturnCompetition() {
        List<EventDTO> events = new ArrayList<>() {{
            add(new EventDTO(1, "Arsenal vs Barcelona", null, null, null, null));
        }};
        CompetitionDTO competitionDTO = new CompetitionDTO(10, "Champions League", events);
        when(competitionService.addCompetition(competitionDTO)).thenReturn(competitionDTO);

        var response = competitionController.addCompetition(competitionDTO);

        assertEquals(competitionDTO, response);
    }

    @Test
    void addCompetitionShoudlReturnException() {
        List<EventDTO> events = new ArrayList<>() {{
            add(new EventDTO(1, "Arsenal vs Barcelona", null, null, null, null));
        }};
        CompetitionDTO competitionDTO = new CompetitionDTO(10, "Champions League", events);
        when(competitionService.addCompetition(competitionDTO)).thenThrow(new EntityAlreadyExistsException(String.format("Competition with ID = %d already exists.", 10)));

        EntityAlreadyExistsException entityAlreadyExistsException = assertThrows(EntityAlreadyExistsException.class,
                () -> competitionController.addCompetition(competitionDTO));

        assertEquals("Competition with ID = 10 already exists.", entityAlreadyExistsException.getMessage());
    }

    @Test
    void updateCompetitionShouldReturnCompetition() {
        List<EventDTO> events = List.of(new EventDTO(1, "Arsenal vs Barcelona", null, null, null, null));
        CompetitionDTO competitionDTO = new CompetitionDTO(10, "Champions League", events);
        when(competitionService.updateCompetition(competitionDTO, 10)).thenReturn(competitionDTO);

        CompetitionDTO response = competitionController.updateCompetition(competitionDTO, 10);

        assertEquals(competitionDTO, response);
    }

    @Test
    void updateCompetitionShouldReturnException() {
        List<EventDTO> events = new ArrayList<>() {{
            add(new EventDTO(1, "Arsenal vs Barcelona", null, null, null, null));
        }};
        CompetitionDTO competitionDTO = new CompetitionDTO(10, "Champions League", events);
        when(competitionService.updateCompetition(competitionDTO, 10)).thenThrow(new NoSuchEntityExistsException(String.format("No participant with ID = %d was found.", 10)));

        NoSuchEntityExistsException noSuchEntityExistsException = assertThrows(NoSuchEntityExistsException.class,
                () -> competitionController.updateCompetition(competitionDTO, 10));

        assertEquals("No participant with ID = 10 was found.", noSuchEntityExistsException.getMessage());
    }

}
