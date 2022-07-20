package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.ParticipantDTO;

public interface ParticipantService {

    ParticipantDTO getParticipant(Integer participantId);

    ParticipantDTO addParticipant(ParticipantDTO newParticipant);

    ParticipantDTO updateParticipant(ParticipantDTO toUpdateParticipant, Integer participantId);
}
