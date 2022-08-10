package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.EventDTOtoEventConverter;
import com.codefactorygroup.betting.converter.MarketDTOtoMarketConverter;
import com.codefactorygroup.betting.converter.ParticipantDtoToParticipantConverter;
import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.dto.MarketDTO;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.EventRepository;
import com.codefactorygroup.betting.service.EventService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "eventService")
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final EventDTOtoEventConverter eventDTOtoEventConverter;

    private final ParticipantDtoToParticipantConverter participantDtoToParticipantConverter;

    private final MarketDTOtoMarketConverter marketDTOtoMarketConverter;

    public EventServiceImpl(EventRepository eventRepository, EventDTOtoEventConverter eventDTOtoEventConverter, ParticipantDtoToParticipantConverter participantDtoToParticipantConverter, MarketDTOtoMarketConverter marketDTOtoMarketConverter) {
        this.eventRepository = eventRepository;
        this.eventDTOtoEventConverter = eventDTOtoEventConverter;
        this.participantDtoToParticipantConverter = participantDtoToParticipantConverter;
        this.marketDTOtoMarketConverter = marketDTOtoMarketConverter;
    }

    @Transactional
    @Override
    public EventDTO getEvent(Integer eventId) {
        return eventRepository.findById(eventId)
                .map(EventDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Event with ID=%d doesn't exist.", eventId)));
    }

    @Transactional
    @Override
    public EventDTO addEvent(EventDTO newEvent) {
        boolean foundEvent = eventRepository.existsById(newEvent.id());
        if (foundEvent) {
            throw new EntityAlreadyExistsException(String.format("Event with ID=%d already exists.", newEvent.id()));
        }
        return EventDTO.converter(eventRepository.
                save(eventDTOtoEventConverter.convert(newEvent)));
    }

    @Transactional
    @Override
    public void deleteEvent(Integer eventId) {
        eventRepository.deleteById(eventId);
    }


    private Event update(final Event event, final EventDTO toUpdateEvent) {
        List <ParticipantDTO> participantDTOS = Optional.of(toUpdateEvent).map(EventDTO::eventParticipants).orElseGet(Collections::emptyList);
        List <MarketDTO> marketDTOS = Optional.of(toUpdateEvent).map(EventDTO::markets).orElseGet(Collections::emptyList);
        event.setId(toUpdateEvent.id());
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
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Event with ID=%d doesn't exist.", eventId)));
    }

}
