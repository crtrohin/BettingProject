package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.service.CompetitionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @PostMapping("/competitions")
    public CompetitionDTO addCompetition(@RequestBody final CompetitionDTO competition) {
        return competitionService.addCompetition(competition);
    }


    @DeleteMapping("/competitions/{competitionId}")
    public void deleteCompetition(@PathVariable(name = "competitionId") final Integer competitionId) {
        competitionService.deleteCompetition(competitionId);
    }

    @PutMapping("/competitions/{competitionId}")
    public CompetitionDTO updateCompetition(@RequestBody final CompetitionDTO competition, @PathVariable(name = "competitionId") final Integer competitionId) {
        return competitionService.updateCompetition(competition, competitionId);
    }

    @PutMapping("/competitions/{competitionId}/events")
    public CompetitionDTO addEventToCompetition(@RequestBody final EventDTO eventDTO, @PathVariable(name = "competitionId") final Integer competitionId) {
        return competitionService.addEventToCompetition(eventDTO, competitionId);
    }
}
