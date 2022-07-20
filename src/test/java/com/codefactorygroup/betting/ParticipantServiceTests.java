package com.codefactorygroup.betting;

import com.codefactorygroup.betting.converter.ParticipantDtoToParticipantConverter;
import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.repository.ParticipantRepository;
import com.codefactorygroup.betting.service.ParticipantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParticipantServiceTests {

    @Mock
    private ParticipantRepository participantRepository;

    private ParticipantServiceImpl participantService =
            new ParticipantServiceImpl(null, participantRepository);

    @Test
    void getParticipantShouldReturnParticipant() {
        Participant participant = new Participant(10, "Arsenal");
        Optional<Participant> optionalParticipant = Optional.of(participant);
        when(participantRepository.findById(10)).thenReturn(optionalParticipant);

        var response = participantRepository.findById(10);

        assertEquals(optionalParticipant, response);
    }


    @Test
    void addParticipantShouldReturnParticipant() {
        Participant participant = new Participant(1, "Real Madrid");
        when(participantRepository.save(participant)).thenReturn(participant);

        var response = participantRepository.save(participant);

        assertEquals(participant, response);
    }


    @Test
    void updateParticipantShouldReturnParticipant() {
        Participant participant = new Participant(3, "FC Shakhtar Donetsk");
        when(participantRepository.save(participant)).thenReturn(participant);

        Participant response = participantRepository.save(participant);

        assertEquals(participant, response);
    }

}
