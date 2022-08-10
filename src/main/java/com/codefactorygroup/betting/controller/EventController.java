package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.service.EventService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{eventId}")
    public EventDTO getEvent(@PathVariable(name = "eventId") final Integer eventId) {
        return eventService.getEvent(eventId);
    }

    @PostMapping
    public EventDTO addEvent(@RequestBody final EventDTO event) {
        return eventService.addEvent(event);
    }

    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable(name = "eventId") final Integer eventId) {
        eventService.deleteEvent(eventId);
    }

    @PutMapping("/{eventId}")
    public EventDTO updateEvent(@RequestBody final EventDTO event, @PathVariable(name = "eventId") final Integer eventId) {
        return eventService.updateEvent(event, eventId);
    }
}
