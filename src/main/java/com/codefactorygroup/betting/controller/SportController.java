package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.dto.SportDTO;
import com.codefactorygroup.betting.service.SportService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/sports")
public class SportController {
    private final SportService sportService;

    public SportController(@Qualifier(value = "sportService") SportService sportService) {
        this.sportService = sportService;
    }

    @GetMapping("/{sportId}")
    public SportDTO getSport(@PathVariable(name = "sportId") final Integer sportId) {
        return sportService.getSport(sportId);
    }

    @PostMapping
    public SportDTO addSport(@RequestBody final SportDTO sport) {
        return sportService.addSport(sport);
    }

    @DeleteMapping("/{sportId}")
    public void deleteSport(@PathVariable(name = "sportId") final Integer sportId) {
        sportService.deleteSport(sportId);
    }

    @PutMapping("/{sportId}")
    public SportDTO updateSport(@RequestBody final SportDTO sport, @PathVariable(name="sportId") final Integer sportId) {
        return sportService.updateSport(sport, sportId);
    }
}
