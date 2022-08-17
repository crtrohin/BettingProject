package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.dto.MarketDTO;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events/{eventId}")
    public EventDTO getEvent(@PathVariable(name = "eventId") final Integer eventId) {
        return eventService.getEvent(eventId);
    }

    @GetMapping("/events")
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/competitions/{competitionId}/events")
    public List<EventDTO> getEventsByCompetitionId(@PathVariable(name = "competitionId") final Integer competitionId) {
        return eventService.getEventsByCompetitionId(competitionId);
    }

    @GetMapping("/participants/{participantId}/events")
    public List<EventDTO> getEventsByParticipantId(@PathVariable(name = "participantId") final Integer participantId) {
        return eventService.getEventsByParticipantId(participantId);
    }

    @GetMapping("/markets/{marketId}/events")
    public List<EventDTO> getEventsByMarketId(@PathVariable(name = "marketId") final Integer marketId) {
        return eventService.getEventsByMarketId(marketId);
    }

    @PostMapping("/events")
    public EventDTO addEvent(@RequestBody final EventDTO event) {
        return eventService.addEvent(event);
    }

    @DeleteMapping("/events/{eventId}")
    public void deleteEvent(@PathVariable(name = "eventId") final Integer eventId) {
        eventService.deleteEvent(eventId);
    }

    @PutMapping("/events/{eventId}")
    public EventDTO updateEvent(@RequestBody final EventDTO eventDTO, @PathVariable(name = "eventId") final Integer eventId) {
        return eventService.updateEvent(eventDTO, eventId);
    }

    @PutMapping("/events/{eventId}/markets")
    public EventDTO addMarketToEvent(@RequestBody final MarketDTO marketDTO, @PathVariable(name = "eventId") final Integer eventId) {
        return eventService.addMarketToEvent(marketDTO, eventId);
    }

    @PutMapping("/events/{eventId}/participants")
    public EventDTO addParticipantToEvent(@RequestBody final ParticipantDTO participantDTO, @PathVariable(name = "eventId") final Integer eventId) {
        return eventService.addParticipantToEvent(participantDTO, eventId);
    }
}
