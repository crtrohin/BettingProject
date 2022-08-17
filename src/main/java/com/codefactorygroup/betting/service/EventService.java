package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.dto.MarketDTO;
import com.codefactorygroup.betting.dto.ParticipantDTO;

import java.util.List;

public interface EventService {

    EventDTO getEvent(Integer eventId);

    List<EventDTO> getAllEvents();

    List<EventDTO> getEventsByCompetitionId(Integer competitionId);

    List<EventDTO> getEventsByParticipantId(Integer participantId);

    List<EventDTO> getEventsByMarketId(Integer marketId);

    EventDTO addEvent(EventDTO event);

    void deleteEvent(Integer eventId);

    EventDTO updateEvent(EventDTO event, Integer eventId);

    EventDTO addMarketToEvent(MarketDTO marketDTO, Integer eventId);

    EventDTO addParticipantToEvent(ParticipantDTO participantDTO, Integer eventId);
}
