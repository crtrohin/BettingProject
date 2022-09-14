package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/competitions/{competitionId}/events")
    public EventDTO addEvent(@PathVariable(name = "competitionId") final Integer competitionId,  @RequestBody final EventDTO event) {
        return eventService.addEvent(competitionId, event);
    }

    @DeleteMapping("/events/{eventId}")
    public void deleteEvent(@PathVariable(name = "eventId") final Integer eventId) {
        eventService.deleteEvent(eventId);
    }

    @PutMapping("/events/{eventId}")
    public EventDTO updateEvent(@RequestBody final EventDTO eventDTO, @PathVariable(name = "eventId") final Integer eventId) {
        return eventService.updateEvent(eventDTO, eventId);
    }


    @PutMapping("/events/{eventId}/participants/{participantId}")
    public EventDTO addParticipantToEvent(@PathVariable final Integer participantId, @PathVariable(name = "eventId") final Integer eventId) {
        return eventService.addParticipantToEvent(participantId, eventId);
    }

    @GetMapping("/events/short")
    public List<Map<String, String>> getEventsShortVersion() {
        return eventService.getEventsShortVersion();
    }

    @GetMapping("/events/marketsSorted")
    public List<EventDTO> getEventsMarketsOrdBySelectionPricesDesc() {
        return eventService.getEventsMarketsOrdBySelectionPricesDesc();
    }

    @GetMapping("/events/notFootball/{nrOfMarkets}")
    public List<EventDTO> getEventsWithNrOfMarketsGreaterThanAndNotFromFootballSport(@PathVariable Integer nrOfMarkets) {
        return eventService.getEventsWithNrOfMarketsGreaterThanAndNotFromFootballSport(nrOfMarkets);
    }
}
