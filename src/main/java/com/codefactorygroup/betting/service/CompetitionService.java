package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.dto.CompetitionDTO;

public interface CompetitionService {

    CompetitionDTO getCompetition(Integer competitionId);

    CompetitionDTO addCompetition(CompetitionDTO competition);

    void deleteCompetition(Integer competitionId);

    CompetitionDTO updateCompetition(CompetitionDTO competition, Integer competitionId);

}
