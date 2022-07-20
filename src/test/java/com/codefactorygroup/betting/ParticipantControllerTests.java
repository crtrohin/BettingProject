package com.codefactorygroup.betting;

import com.codefactorygroup.betting.controller.ParticipantController;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.exception.NoSuchParticipantExistsException;
import com.codefactorygroup.betting.exception.ParticipantAlreadyExistsException;
import com.codefactorygroup.betting.service.ParticipantService;
import com.codefactorygroup.betting.service.ParticipantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParticipantControllerTests {

    private ParticipantService participantService = mock(ParticipantServiceImpl.class);
    private ParticipantController participantController = new ParticipantController(participantService);

    @Test
    void getParticipantShouldReturnParticipant() {
        ParticipantDTO participantDTO = new ParticipantDTO(10, "Arsenal");
        when(participantService.getParticipant(10)).thenReturn(participantDTO);

        var response = participantController.getParticipant(10);

        assertEquals(participantDTO, response);
    }

    @Test
    void getParticipantShouldReturnException() {
        when(participantService.getParticipant(10)).thenThrow(new NoSuchParticipantExistsException(10));

        NoSuchParticipantExistsException noSuchParticipantExistsException = assertThrows(NoSuchParticipantExistsException.class,
                () -> {participantController.getParticipant(10);
        });

        assertEquals("No participant with ID = 10 was found.",noSuchParticipantExistsException.getMessage());
    }

    @Test
    void addParticipantShouldReturnParticipant() {
        ParticipantDTO participantDTO = new ParticipantDTO(1, "Real Madrid");
        when(participantService.addParticipant(participantDTO)).thenReturn(participantDTO);

        var response = participantController.addParticipant(participantDTO);

        assertEquals(participantDTO, response);
    }

    @Test
    void addParticipantShoudlReturnException() {
        ParticipantDTO participantDTO = new ParticipantDTO(2, "Atletico Bilbao");
        when(participantService.addParticipant(participantDTO)).thenThrow(new ParticipantAlreadyExistsException(2));

        ParticipantAlreadyExistsException participantAlreadyExistsException = assertThrows(ParticipantAlreadyExistsException.class,
                () -> participantController.addParticipant(participantDTO));

        assertEquals("Participant with ID = 2 already exists.", participantAlreadyExistsException.getMessage());
    }

    @Test
    void updateParticipantShouldReturnParticipant() {
        ParticipantDTO participantDTO = new ParticipantDTO(3, "FC Shakhtar Donetsk");
        when(participantService.updateParticipant(participantDTO, 3)).thenReturn(participantDTO);

        ParticipantDTO response = participantController.updateParticipant(participantDTO, 3);

        assertEquals(participantDTO, response);
    }

    @Test
    void updateParticipantShouldReturnException() {
        ParticipantDTO participantDTO = new ParticipantDTO(4, "Tottenham");
        when(participantService.updateParticipant(participantDTO, 4)).thenThrow(new NoSuchParticipantExistsException(4));

        NoSuchParticipantExistsException noSuchParticipantExistsException = assertThrows(NoSuchParticipantExistsException.class,
                () -> participantController.updateParticipant(participantDTO, 4));

        assertEquals("No participant with ID = 4 was found.", noSuchParticipantExistsException.getMessage());
    }
}
