//package com.codefactorygroup.betting.converterTests;
//
//import com.codefactorygroup.betting.converter.ParticipantDtoToParticipantConverter;
//import com.codefactorygroup.betting.domain.Participant;
//import com.codefactorygroup.betting.dto.ParticipantDTO;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ParticipantConverterTests {
//
//    private final ParticipantDtoToParticipantConverter participantDtoToParticipantConverter = new ParticipantDtoToParticipantConverter(eventDTOtoEventConverter);
//
//    @Test
//    void checkParticipantToParticipantDtoConversion() {
//        ParticipantDTO actualParticipant = ParticipantDTO.converter(new Participant(9, "Atletico Madrid", new ArrayList<>()));
//        ParticipantDTO expectedParticipant = new ParticipantDTO(9, "Atletico Madrid");
//        assertEquals(actualParticipant, expectedParticipant);
//    }
//
//    @Test
//    void checkParticipantDtoToParticipantConversion() {
//        Participant actualParticipant = participantDtoToParticipantConverter.convert(new ParticipantDTO(9, "Atletico Madrid"));
//        Participant expectedParticipant = new Participant(9, "Atletico Madrid", new ArrayList<>());
//        assertEquals(actualParticipant, expectedParticipant);
//    }
//
//}
