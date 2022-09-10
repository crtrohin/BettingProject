package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.dto.EventDTO;

import java.util.List;
import java.util.Map;

public interface EventService {

    EventDTO getEvent(Integer eventId);

    List<EventDTO> getAllEvents();

    List<EventDTO> getEventsByCompetitionId(Integer competitionId);

    List<EventDTO> getEventsByParticipantId(Integer participantId);

    List<EventDTO> getEventsByMarketId(Integer marketId);

    EventDTO addEvent(Integer competitionId, EventDTO event);

    void deleteEvent(Integer eventId);

    EventDTO updateEvent(EventDTO event, Integer eventId);

    EventDTO addParticipantToEvent(Integer participantId, Integer eventId);

    List<Map<String, String>> getEventsShortVersion();

    List<EventDTO> getEventsMarketsOrdBySelectionPricesDesc();
}
