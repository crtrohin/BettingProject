package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.dto.RandomParticipantsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ParticipantService {

    ParticipantDTO getParticipant(Integer participantId);

    ParticipantDTO addParticipant(ParticipantDTO newParticipant);

    void deleteParticipant(Integer participantId);

    List<ParticipantDTO> getAllParticipants();

    ParticipantDTO updateParticipant(ParticipantDTO toUpdateParticipant, Integer participantId);

    RandomParticipantsDTO getRandomParticipants();

    List<ParticipantDTO> findPaginated(int pageNo, int pageSize);

    public Participant findParticipantByName(String name);

    }
