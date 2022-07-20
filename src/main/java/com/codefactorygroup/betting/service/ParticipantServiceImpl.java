package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.converter.ParticipantDtoToParticipantConverter;
import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.exception.NoSuchParticipantExistsException;
import com.codefactorygroup.betting.exception.ParticipantAlreadyExistsException;
import com.codefactorygroup.betting.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service(value = "default")
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantDtoToParticipantConverter participantDtoToParticipantConverter;
    private final ParticipantRepository participantRepository;

    public ParticipantDTO getParticipant(final Integer participantId) {
        return participantRepository.findById(participantId)
                .map(ParticipantDTO::converter)
                .orElseThrow(() -> new NoSuchParticipantExistsException(participantId));

    }

    public ParticipantDTO addParticipant(final ParticipantDTO newParticipant) {
        boolean foundParticipant = participantRepository.existsById(newParticipant.dtoID());
        if (foundParticipant) {
            throw new ParticipantAlreadyExistsException(newParticipant.dtoID());
        }
        return ParticipantDTO.converter(participantRepository.
                save(participantDtoToParticipantConverter.convert(newParticipant)));
    }

    private Participant update(final Participant participant, final ParticipantDTO toUpdateParticipant) {
        participant.setId(toUpdateParticipant.dtoID());
        participant.setName(toUpdateParticipant.name());
        return participant;
    }

    public ParticipantDTO updateParticipant(final ParticipantDTO toUpdateParticipant,
                                            final Integer participantId) {
        return participantRepository
                .findById(participantId)
                .map(participantFromDb -> update(participantFromDb, toUpdateParticipant))
                .map(participantRepository::save)
                .map(ParticipantDTO::converter)
                .orElseThrow(() -> new NoSuchParticipantExistsException(participantId));
    }
}
