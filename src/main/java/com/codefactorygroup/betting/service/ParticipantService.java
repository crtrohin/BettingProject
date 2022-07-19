package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.ParticipantDTO;

public interface ParticipantService {

    ParticipantDTO getParticipant(Integer participantId);

    ParticipantDTO addParticipant(Participant newParticipant);

    ParticipantDTO updateParticipant(Participant toUpdateParticipant, Integer participantId);
}
