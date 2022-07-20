package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.service.CompetitionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/competitions")
public class CompetitionController {

    private final CompetitionService competitionService;

    public CompetitionController(@Qualifier(value = "competitionService") CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @GetMapping("/{competitionId}")
    public CompetitionDTO getCompetition(@PathVariable(name = "competitionId") final Integer competitionId) {
        return competitionService.getCompetition(competitionId);
    }

    @PostMapping
    public CompetitionDTO addCompetition(@RequestBody final CompetitionDTO competition) {
        return competitionService.addCompetition(competition);
    }

    @DeleteMapping("/{competitionId}")
    public void deleteCompetition(@PathVariable(name = "competitionId") final Integer competitionId) {
        competitionService.deleteCompetition(competitionId);
    }

    @PutMapping("/{competitionId}")
    public CompetitionDTO updateCompetition(@RequestBody final CompetitionDTO competition, @PathVariable(name = "competitionId") final Integer competitionId) {
        return competitionService.updateCompetition(competition, competitionId);
    }
}
