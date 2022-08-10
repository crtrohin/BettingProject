package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.dto.RandomParticipantsDTO;
import com.codefactorygroup.betting.service.ParticipantService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
    private final ParticipantService participantService;

    public ParticipantController(@Qualifier(value = "participantService") ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("/{participantId}")
    public ParticipantDTO getParticipant(@PathVariable("participantId") final Integer participantId) {
        return participantService.getParticipant(participantId);
    }

    @PostMapping
    public ParticipantDTO addParticipant(@RequestBody final ParticipantDTO newParticipant) {
        return participantService.addParticipant(newParticipant);
    }

    @PutMapping("/{participantId}")
    public ParticipantDTO updateParticipant(@RequestBody final ParticipantDTO toUpdateParticipant,
                                            @PathVariable("participantId") final Integer participantId) {
        return participantService.updateParticipant(toUpdateParticipant, participantId);
    }

    @GetMapping("/random")
    public RandomParticipantsDTO getRandomParticipants() {
        return participantService.getRandomParticipants();
    }

    @GetMapping
    public List<ParticipantDTO> getAllParticipants(@RequestParam("page") Integer page,
                                   @RequestParam("perPage") Integer perPage) {
        return participantService.findPaginated(page, perPage);
    }

    @DeleteMapping("/{participantId}")
    public void deleteParticipant(@PathVariable("participantId") final Integer participantId) {
        participantService.deleteParticipant(participantId);
    }

}
