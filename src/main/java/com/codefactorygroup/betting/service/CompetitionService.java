package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.dto.EventDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CompetitionService {

    CompetitionDTO getCompetition(Integer competitionId);

    List<CompetitionDTO> getAllCompetitions();

    List<CompetitionDTO> getCompetitionsBySportId(Integer sportId);

    CompetitionDTO addCompetition(CompetitionDTO competition);

    void deleteCompetition(Integer competitionId);

    CompetitionDTO updateCompetition(CompetitionDTO competition, Integer competitionId);

    CompetitionDTO addEventToCompetition(EventDTO eventDTO, Integer competitionId);
}
