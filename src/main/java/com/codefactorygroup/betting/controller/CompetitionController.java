package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.service.CompetitionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompetitionController {

    private final CompetitionService competitionService;

    public CompetitionController(@Qualifier(value = "competitionService") CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @GetMapping("/competitions/{competitionId}")
    public CompetitionDTO getCompetition(@PathVariable(name = "competitionId") final Integer competitionId) {
        return competitionService.getCompetition(competitionId);
    }

    @GetMapping("/competitions")
    public List<CompetitionDTO> getAllCompetitions() {
        return competitionService.getAllCompetitions();
    }

    @GetMapping("/sports/{sportId}/competitions")
    public List<CompetitionDTO> getCompetitionsBySportId(@PathVariable(name = "sportId") final Integer sportId) {
        return competitionService.getCompetitionsBySportId(sportId);
    }

    @PostMapping("sports/{sportId}/competitions")
    public CompetitionDTO addCompetition(@PathVariable(name = "sportId") final Integer sportId,
                                         @RequestBody final CompetitionDTO competition) {
        return competitionService.addCompetition(sportId, competition);
    }

    @DeleteMapping("/competitions/{competitionId}")
    public void deleteCompetition(@PathVariable(name = "competitionId") final Integer competitionId) {
        competitionService.deleteCompetition(competitionId);
    }

    @PutMapping("/competitions/{competitionId}")
    public CompetitionDTO updateCompetition(@PathVariable(name = "competitionId") Integer competitionId,
                                @RequestBody final CompetitionDTO competitionDTO) {
        return competitionService.updateCompetition(competitionDTO, competitionId);
    }

}
