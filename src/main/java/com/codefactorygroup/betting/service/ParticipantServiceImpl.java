package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.exception.NoSuchParticipantExistsException;
import com.codefactorygroup.betting.exception.ParticipantAlreadyExistsException;
import com.codefactorygroup.betting.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;

//  private final ParticipantToParticipantDtoConverter participantToParticipantDtoConverter;

    public ParticipantDTO getParticipant(final Integer participantId) {
        return participantRepository.findById(participantId)
                .map(ParticipantDTO::converter)
                .orElseThrow(() -> new NoSuchElementException(String.format("No participant with ID = %d was found.", participantId)));

    }

    public ParticipantDTO addParticipant(final Participant newParticipant) {
        Boolean foundParticipant = participantRepository.findById(newParticipant.getId()).isPresent();
        if (foundParticipant) {
                throw new ParticipantAlreadyExistsException(String.format("Participant with ID = %d already exists.", newParticipant.getId()));
        }
        return ParticipantDTO.converter(participantRepository.save(newParticipant));
    }

    private Participant update(final Participant participant, final Participant toUpdateParticipant) {
        participant.setId(toUpdateParticipant.getId());
        participant.setName(toUpdateParticipant.getName());
        return participant;
    }

    public ParticipantDTO updateParticipant(final Participant toUpdateParticipant,
                                            final Integer participantId) {
        return participantRepository
                .findById(participantId)
                .map(participantFromDb -> update(participantFromDb, toUpdateParticipant))
                .map(participantRepository::save)
                .map(ParticipantDTO::converter)
                .orElseThrow(() -> new NoSuchParticipantExistsException("Failed to save participant."));
    }
}
