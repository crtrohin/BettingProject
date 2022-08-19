package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.dto.SportDTO;
import com.codefactorygroup.betting.service.SportService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SportController {
    private final SportService sportService;

    public SportController(@Qualifier(value = "sportService") SportService sportService) {
        this.sportService = sportService;
    }

    @GetMapping("/sports/{sportId}")
    public SportDTO getSport(@PathVariable(name = "sportId") final Integer sportId) {
        return sportService.getSport(sportId);
    }

    @GetMapping("/sports")
    public List<SportDTO> getAllSports() {
        return sportService.getAllSports();
    }

    @PostMapping("/sports")
    public SportDTO addSport(@RequestBody final SportDTO sport) {
        return sportService.addSport(sport);
    }

    @DeleteMapping("/sports/{sportId}")
    public void deleteSport(@PathVariable(name = "sportId") final Integer sportId) {
        sportService.deleteSport(sportId);
    }

    @PutMapping("/sports/{sportId}")
    public SportDTO updateSport(@RequestBody final SportDTO sport, @PathVariable(name="sportId") final Integer sportId) {
        return sportService.updateSport(sport, sportId);
    }

}
