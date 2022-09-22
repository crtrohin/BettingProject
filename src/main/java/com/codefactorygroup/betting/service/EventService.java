package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.dto.EventShortDTO;

import java.util.List;

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

    List<EventShortDTO> getEventsShortVersion();

    List<EventDTO> getEventsMarketsOrdBySelectionPricesDesc();

    List<EventDTO> getEventsWithNrOfMarketsGreaterThanAndNotFromFootballSport(final Integer nrOfMarkets);

    List<EventDTO> getEventsWithDuplicatedParticipantAndDistinctYear();

}
