package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.dto.ParticipantDTO;

import java.util.List;

public interface ParticipantService {

    ParticipantDTO getParticipant(Integer participantId);

    List<ParticipantDTO> getAllParticipants();

    List<ParticipantDTO> getParticipantsByEventId(Integer eventId);

    ParticipantDTO addParticipant(Integer eventId, ParticipantDTO newParticipant);

    void deleteParticipant(Integer participantId);

    ParticipantDTO updateParticipant(ParticipantDTO toUpdateParticipant, Integer participantId);

    List<ParticipantDTO> findPaginated(int pageNo, int pageSize);
}
