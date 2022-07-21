package com.codefactorygroup.betting;

import com.codefactorygroup.betting.converter.ParticipantDtoToParticipantConverter;
import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipantConverterTests {

    private ParticipantDtoToParticipantConverter participantDtoToParticipantConverter = new ParticipantDtoToParticipantConverter();

    @Test
    void checkParticipantToParticipantDtoConversion() {
        ParticipantDTO actualParticipant = ParticipantDTO.converter(new Participant(9, "Atletico Madrid"));
        ParticipantDTO expectedParticipant = new ParticipantDTO(9, "Atletico Madrid");
        assertEquals(actualParticipant, expectedParticipant);
    }

    @Test
    void checkParticipantDtoToParticipantConversion() {
        Participant actualParticipant = participantDtoToParticipantConverter.convert(new ParticipantDTO(9, "Atletico Madrid"));
        Participant expectedParticipant = new Participant(9, "Atletico Madrid");
        assertEquals(actualParticipant, expectedParticipant);
    }

}
