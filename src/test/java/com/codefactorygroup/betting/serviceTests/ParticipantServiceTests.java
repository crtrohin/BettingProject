package com.codefactorygroup.betting.serviceTests;

import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.repository.ParticipantRepository;
import com.codefactorygroup.betting.service.implementations.ParticipantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTests {

    @Mock
    private ParticipantRepository participantRepository;

    private final ParticipantServiceImpl participantService =
            new ParticipantServiceImpl(null, participantRepository, null);

    @Test
    void getParticipantShouldReturnParticipant() {
        Participant participant = new Participant(10, "Arsenal", new ArrayList<>());
        Optional<Participant> optionalParticipant = Optional.of(participant);
        when(participantRepository.findById(10)).thenReturn(optionalParticipant);

        var response = participantRepository.findById(10);

        assertEquals(optionalParticipant, response);
    }


    @Test
    void addParticipantShouldReturnParticipant() {
        Participant participant = new Participant(1, "Real Madrid", new ArrayList<>());
        when(participantRepository.save(participant)).thenReturn(participant);

        var response = participantRepository.save(participant);

        assertEquals(participant, response);
    }

}
