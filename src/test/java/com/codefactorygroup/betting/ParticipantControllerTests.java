package com.codefactorygroup.betting;

import com.codefactorygroup.betting.controller.ParticipantController;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.service.ParticipantService;
import com.codefactorygroup.betting.service.implementations.ParticipantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantControllerTests {

    private final ParticipantService participantService = mock(ParticipantServiceImpl.class);
    private final ParticipantController participantController = new ParticipantController(participantService);


    @Test
    void getParticipantShouldReturnParticipant() {
        ParticipantDTO participantDTO = new ParticipantDTO( "Arsenal");
        when(participantService.getParticipant(10)).thenReturn(participantDTO);

        var response = participantController.getParticipant(10);

        assertEquals(participantDTO, response);
    }

    @Test
    void getParticipantShouldReturnException() {
        when(participantService.getParticipant(10)).thenThrow(new NoSuchEntityExistsException(String.format("No participant with ID = %d was found.", 10)));

        NoSuchEntityExistsException noSuchEntityExistsException = assertThrows(NoSuchEntityExistsException.class,
                () -> {participantController.getParticipant(10);
        });

        assertEquals("No participant with ID = 10 was found.", noSuchEntityExistsException.getMessage());
    }

    @Test
    void addParticipantShouldReturnParticipant() {
        ParticipantDTO participantDTO = new ParticipantDTO("Real Madrid");
        when(participantService.addParticipant(1, participantDTO)).thenReturn(participantDTO);

        var response = participantController.addParticipant(1, participantDTO);

        assertEquals(participantDTO, response);
    }

    @Test
    void addParticipantShoudlReturnException() {
        ParticipantDTO participantDTO = new ParticipantDTO("Atletico Bilbao");
        when(participantService.addParticipant(1, participantDTO)).thenThrow(new EntityAlreadyExistsException(String.format("Participant with ID = %d already exists.", 2)));

        EntityAlreadyExistsException entityAlreadyExistsException = assertThrows(EntityAlreadyExistsException.class,
                () -> participantController.addParticipant(1, participantDTO));

        assertEquals("Participant with ID = 2 already exists.", entityAlreadyExistsException.getMessage());
    }

    @Test
    void updateParticipantShouldReturnParticipant() {
        ParticipantDTO participantDTO = new ParticipantDTO("FC Shakhtar Donetsk");
        when(participantService.updateParticipant(participantDTO, 3)).thenReturn(participantDTO);

        ParticipantDTO response = participantController.updateParticipant(participantDTO, 3);

        assertEquals(participantDTO, response);
    }

    @Test
    void updateParticipantShouldReturnException() {
        ParticipantDTO participantDTO = new ParticipantDTO( "Tottenham");
        when(participantService.updateParticipant(participantDTO, 4)).thenThrow(new NoSuchEntityExistsException(String.format("No participant with ID = %d was found.", 4)));

        NoSuchEntityExistsException noSuchEntityExistsException = assertThrows(NoSuchEntityExistsException.class,
                () -> participantController.updateParticipant(participantDTO, 4));

        assertEquals("No participant with ID = 4 was found.", noSuchEntityExistsException.getMessage());
    }
}
