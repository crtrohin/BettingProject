package com.codefactorygroup.betting.serviceTests;

import com.codefactorygroup.betting.converter.ParticipantDtoToParticipantConverter;
import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.EventRepository;
import com.codefactorygroup.betting.repository.ParticipantRepository;
import com.codefactorygroup.betting.service.implementations.ParticipantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTests {

    @Mock
    private ParticipantDtoToParticipantConverter participantDtoToParticipantConverter;
    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private ParticipantServiceImpl participantService;

    @Test
    void getParticipantShouldReturnParticipant() {
        Optional<Participant> optionalParticipant = Optional.of(Participant
                .builder()
                .id(10)
                .name("Arsenal")
                .build());

        when(participantRepository.findById(10)).thenReturn(optionalParticipant);

        ParticipantDTO participantDTO = participantService.getParticipant(10);
        Participant participant = optionalParticipant.get();

        assertThat(participantDTO).isNotNull();
        assertEquals(participant.getId(), participantDTO.id());
        assertEquals(participant.getName(), participantDTO.name());
    }

    @Test
    void getParticipantShouldReturnException() {
        when(participantRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> participantService.getParticipant(10));
    }

    @Test
    void getAllParticipantsShouldReturnParticipants() {
        Participant participant1 = Participant
                .builder()
                .id(1)
                .name("Arsenal")
                .build();

        Participant participant2 = Participant
                .builder()
                .id(2)
                .name("Chelsea")
                .build();

        Participant participant3 = Participant
                .builder()
                .id(3)
                .name("Real Madrid")
                .build();

        List<Participant> participants = List.of(participant1, participant2, participant3);

        when(participantRepository.findAll()).thenReturn(participants);

        List<ParticipantDTO> participantDTOS = participantService.getAllParticipants();

        assertEquals(participantDTOS.get(0).id(), participant1.getId());
        assertEquals(participantDTOS.get(0).name(), participant1.getName());

        assertEquals(participantDTOS.get(1).id(), participant2.getId());
        assertEquals(participantDTOS.get(1).name(), participant2.getName());

        assertEquals(participantDTOS.get(2).id(), participant3.getId());
        assertEquals(participantDTOS.get(2).name(), participant3.getName());
    }


    @Test
    void addParticipantShouldReturnParticipant() {
        ParticipantDTO participantDTO = ParticipantDTO
                .builder()
                .id(1)
                .name("Real Madrid")
                .build();

        Participant participant1 = Participant
                .builder()
                .id(1)
                .name("Real Madrid")
                .build();

        Participant participant2 = Participant
                .builder()
                .id(2)
                .name("Sheriff")
                .build();

        List<Participant> participants = new ArrayList<>();
        participants.add(participant2);

        Event event = Event
                .builder()
                .id(1)
                .name("Real Madrid vs Sheriff")
                .participants(participants)
                .build();

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(participantRepository.existsParticipantByName("Real Madrid")).thenReturn(false);
        when(participantDtoToParticipantConverter.convert(participantDTO)).thenReturn(participant1);
        when(participantRepository.save(participant1)).thenReturn(participant1);

        ParticipantDTO savedParticipant = participantService.addParticipant(1, participantDTO);

        assertEquals(savedParticipant, participantDTO);
    }

    @Test
    void addParticipantShouldReturnExceptionNoEventExists() {
        ParticipantDTO participantDTO = ParticipantDTO
                .builder()
                .id(1)
                .name("Real Madrid")
                .build();

        when(eventRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> participantService.addParticipant(1, participantDTO));
    }

    @Test
    void addParticipantShouldReturnExceptionParticipantAlreadyExists() {
        ParticipantDTO participantDTO = ParticipantDTO
                .builder()
                .id(1)
                .name("Real Madrid")
                .build();

        Participant participant2 = Participant
                .builder()
                .id(2)
                .name("Sheriff")
                .build();

        List<Participant> participants = new ArrayList<>();
        participants.add(participant2);

        Event event = Event
                .builder()
                .id(1)
                .name("Real Madrid vs Sheriff")
                .participants(participants)
                .build();

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(participantRepository.existsParticipantByName("Real Madrid")).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> participantService.addParticipant(1, participantDTO));
    }

    @Test
    void updateParticipantShouldReturnParticipant() {
        Participant participantFromDb = Participant
                .builder()
                .id(1)
                .name("Juventus")
                .build();

        Participant updatedParticipant = Participant
                .builder()
                .id(1)
                .name("Arsenal")
                .build();

        ParticipantDTO toUpdateParticipantDTO = ParticipantDTO
                .builder()
                .id(1)
                .name("Arsenal")
                .build();

        when(participantRepository.findById(1)).thenReturn(Optional.of(participantFromDb));
        when(participantRepository.save(updatedParticipant)).thenReturn(updatedParticipant);

        ParticipantDTO resultedParticipantDTO = participantService.updateParticipant(toUpdateParticipantDTO, 1);

        assertEquals(resultedParticipantDTO, toUpdateParticipantDTO);
    }

    @Test
    void updateParticipantShouldReturnException() {
        ParticipantDTO toUpdateParticipantDTO = ParticipantDTO
                .builder()
                .id(1)
                .name("Arsenal")
                .build();

        when(participantRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> participantService.updateParticipant(toUpdateParticipantDTO, 1));
    }


    @Test
    void deleteParticipant() {
        when(participantRepository.existsById(1)).thenReturn(true);

        participantService.deleteParticipant(1);

        // check if the method was called
        verify(participantRepository).deleteById(1);
    }


    @Test
    void deleteParticipantShouldReturnException() {
        when(participantRepository.existsById(1)).thenReturn(false);

        assertThrows(NoSuchEntityExistsException.class, () -> participantService.deleteParticipant(1));
    }

}
