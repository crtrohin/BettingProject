package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.service.ParticipantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// In Spring MVC, the model works a container that contains the data of the application.
// Here, a data can be in any form such as objects, strings, information from the database, etc.
// It is required to place the Model interface in the controller part of the application.

@AllArgsConstructor
@RestController
@RequestMapping("/participant")
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping("/{participantId}")
    public ParticipantDTO getParticipant(@PathVariable("participantId") final Integer participantId) {
        return participantService.getParticipant(participantId);
    }

    @PostMapping
    public ParticipantDTO addParticipant(@RequestBody final Participant newParticipant) {
        return participantService.addParticipant(newParticipant);
    }

    @PutMapping("/{participantId}")
    public ParticipantDTO updateParticipant(@RequestBody final Participant toUpdateParticipant,
                                            @PathVariable("participantId") final Integer participantId) {
        return participantService.updateParticipant(toUpdateParticipant, participantId);
    }
}
