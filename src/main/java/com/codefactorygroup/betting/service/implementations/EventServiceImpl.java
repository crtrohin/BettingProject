package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.EventDTOtoEventConverter;
import com.codefactorygroup.betting.converter.MarketDTOtoMarketConverter;
import com.codefactorygroup.betting.converter.ParticipantDtoToParticipantConverter;
import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.dto.MarketDTO;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.EventRepository;
import com.codefactorygroup.betting.service.EventService;
import com.codefactorygroup.betting.service.ParticipantService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Component(value = "eventService")
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final EventDTOtoEventConverter eventDTOtoEventConverter;

    private final ParticipantDtoToParticipantConverter participantDtoToParticipantConverter;

    private final MarketDTOtoMarketConverter marketDTOtoMarketConverter;

    private final ParticipantService participantService;

    public EventServiceImpl(EventRepository eventRepository,
                            EventDTOtoEventConverter eventDTOtoEventConverter,
                            ParticipantDtoToParticipantConverter participantDtoToParticipantConverter,
                            MarketDTOtoMarketConverter marketDTOtoMarketConverter,
                            @Lazy ParticipantService participantService) {

        this.eventRepository = eventRepository;
        this.eventDTOtoEventConverter = eventDTOtoEventConverter;
        this.participantDtoToParticipantConverter = participantDtoToParticipantConverter;
        this.marketDTOtoMarketConverter = marketDTOtoMarketConverter;
        this.participantService = participantService;
    }

    @Transactional
    @Override
    public EventDTO getEvent(Integer eventId) {
        return eventRepository.findById(eventId)
                .map(EventDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Event with ID = %d doesn't exist.", eventId)));
    }

    @Transactional
    @Override
    public EventDTO addEvent(EventDTO newEvent) {
        Event event = eventDTOtoEventConverter.convert(newEvent);
        List<Participant> participants =
                findEventByNameAndStartTimeAndEndTime(event.getName(), event.getStartTime(),
                                event.getEndTime(), event.getParticipants())
                .getParticipants()
                .stream()
                .map(participant -> participantService.findParticipantByName(participant.getName()))
                .collect(Collectors.toList());

        event.setParticipants(participants);

        return EventDTO.converter(
                eventRepository.save(event));
    }

    @Transactional
    @Override
    public void deleteEvent(Integer eventId) {
        eventRepository.deleteById(eventId);
    }


    private Event update(final Event event, final EventDTO toUpdateEvent) {
        List <ParticipantDTO> participantDTOS = Optional.of(toUpdateEvent).map(EventDTO::participants).orElseGet(Collections::emptyList);
        List <MarketDTO> marketDTOS = Optional.of(toUpdateEvent).map(EventDTO::markets).orElseGet(Collections::emptyList);
        event.setName(toUpdateEvent.name());
        event.setStartTime(toUpdateEvent.startTime());
        event.setEndTime(toUpdateEvent.endTime());
        event.setParticipants(participantDTOS
                .stream()
                .map(participantDtoToParticipantConverter::convert)
                .collect(Collectors.toList()));
        event.setMarkets(marketDTOS
                .stream()
                .map(marketDTOtoMarketConverter::convert)
                .collect(Collectors.toList()));
        return event;
    }

    @Transactional
    @Override
    public EventDTO updateEvent(final EventDTO newEvent, final Integer eventId) {
        return eventRepository.findById(eventId)
                .map(eventFromDb -> update(eventFromDb, newEvent))
                .map(eventRepository::save)
                .map(EventDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Event with ID = %d doesn't exist.", eventId)));
    }


    public Event findEventByNameAndStartTimeAndEndTime(String name, String startTime, String endTime, List<Participant> participants) {
        Optional <Event> eventOptional = eventRepository.findByNameAndStartTimeAndEndTime(name, startTime, endTime);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            List<Participant> participantList = event.getParticipants();
            participantList.addAll(participants);

            Set<Participant> participantSet = participantList.stream().collect(Collectors.toSet());
            List<Participant> updatedParticipants = new ArrayList<>(participantSet);

            event.setParticipants(updatedParticipants);
            return event;
        } else {
            Event event = new Event();
            event.setName(name);
            event.setStartTime(startTime);
            event.setEndTime(endTime);
            event.setParticipants(participants);
            return event;
        }
    }
}
