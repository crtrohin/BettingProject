package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.dto.EventDTO;

public interface EventService {

    EventDTO getEvent(Integer eventId);

    EventDTO addEvent(EventDTO event);

    void deleteEvent(Integer eventId);

    EventDTO updateEvent(EventDTO event, Integer eventId);

}
