package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.EventDTO;

import java.util.List;

public interface EventService {

    EventDTO getEvent(Integer eventId);

    EventDTO addEvent(EventDTO event);

    void deleteEvent(Integer eventId);

    EventDTO updateEvent(EventDTO event, Integer eventId);

    Event findEventByNameAndStartTimeAndEndTime(String name, String startTime, String endTime, List<Participant> participants);


    }
