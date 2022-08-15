package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.domain.Competition;
import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.dto.CompetitionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompetitionService {

    CompetitionDTO getCompetition(Integer competitionId);

    CompetitionDTO addCompetition(CompetitionDTO competition);

    void deleteCompetition(Integer competitionId);

    CompetitionDTO updateCompetition(CompetitionDTO competition, Integer competitionId);

    Competition findCompetitionByName(String name, List<Event> events);

}
