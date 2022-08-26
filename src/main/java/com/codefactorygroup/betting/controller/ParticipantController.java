package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.service.ParticipantService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ParticipantController {
    private final ParticipantService participantService;

    public ParticipantController(@Qualifier(value = "participantService") ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("/participants/{participantId}")
    public ParticipantDTO getParticipant(@PathVariable("participantId") final Integer participantId) {
        return participantService.getParticipant(participantId);
    }

    @GetMapping("/participants")
    public List<ParticipantDTO> getAllParticipants() {
        return participantService.getAllParticipants();
    }

    @GetMapping("/events/{eventId}/participants")
    public List<ParticipantDTO> getParticipantsByEventId(@PathVariable(name = "eventId") final Integer eventId) {
        return participantService.getParticipantsByEventId(eventId);
    }

    @PostMapping("/events/{eventId}/participants")
    public ParticipantDTO addParticipant(@PathVariable(name = "eventId") final Integer eventId, @RequestBody final ParticipantDTO newParticipant) {
        return participantService.addParticipant(eventId, newParticipant);
    }

    @PutMapping("/participants/{participantId}")
    public ParticipantDTO updateParticipant(@RequestBody final ParticipantDTO toUpdateParticipant,
                                            @PathVariable("participantId") final Integer participantId) {
        return participantService.updateParticipant(toUpdateParticipant, participantId);
    }

    @GetMapping("/participants/paginated")
    public List<ParticipantDTO> getAllParticipantsByPage(@RequestParam(value = "page") Integer page,
                                                         @RequestParam("perPage") Integer perPage) {
        return participantService.findPaginated(page, perPage);
    }

    @DeleteMapping("/participants/{participantId}")
    public void deleteParticipant(@PathVariable("participantId") final Integer participantId) {
        participantService.deleteParticipant(participantId);
    }

}
