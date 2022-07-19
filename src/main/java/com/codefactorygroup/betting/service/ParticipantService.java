package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;

//  private final ParticipantToParticipantDtoConverter participantToParticipantDtoConverter;

    public ParticipantDTO getService(final Integer participantId) throws Exception {
        return participantRepository.findById(participantId)
                .map(ParticipantDTO::converter)
                .orElseThrow(() -> new RuntimeException(String.format("No participant with ID = %d was found.", participantId)));

    }

    public ParticipantDTO addService(final Participant newParticipant) {
        return ParticipantDTO.converter(participantRepository.save(newParticipant));
    }

    private Participant update(final Participant participant, final Participant toUpdateParticipant) {
        participant.setId(toUpdateParticipant.getId());
        participant.setName(toUpdateParticipant.getName());
        return participant;
    }

    public ParticipantDTO updateService(final Participant toUpdateParticipant,
                                            final Integer participantId) {
        return participantRepository
                .findById(participantId)
                .map(participantFromDb -> update(participantFromDb, toUpdateParticipant))
                .map(participantRepository::save)
                .map(ParticipantDTO::converter)
                .orElseThrow(() -> new RuntimeException("Failed to save participant."));

    }
}
