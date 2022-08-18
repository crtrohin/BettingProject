package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.EventDTOtoEventConverter;
import com.codefactorygroup.betting.converter.MarketDTOtoMarketConverter;
import com.codefactorygroup.betting.converter.ParticipantDtoToParticipantConverter;
import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.domain.Market;
import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.dto.MarketDTO;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.EventRepository;
import com.codefactorygroup.betting.service.EventService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Override
    public EventDTO getEvent(Integer eventId) {
        return eventRepository.findById(eventId)
                .map(EventDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Event with ID=%d doesn't exist.", eventId)));
    }

    @Override
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(EventDTO::converter)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByCompetitionId(Integer competitionId) {
        return eventRepository.findEventsByCompetitionId(competitionId)
                .stream()
                .map(EventDTO::converter)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByParticipantId(Integer participantId) {
        return eventRepository.findEventsByParticipantId(participantId)
                .stream()
                .map(EventDTO::converter)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByMarketId(Integer marketId) {
        return eventRepository.findEventsByMarketId(marketId)
                .stream()
                .map(EventDTO::converter)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventDTO addEvent(EventDTO newEvent) {
        Optional<Event> eventOptional = eventRepository.findByNameAndStartTimeAndEndTime(newEvent.name(),
                newEvent.startTime(), newEvent.endTime());
        if (eventOptional.isPresent()) {
            throw new EntityAlreadyExistsException(String.format("Event with name=%s already exists.", newEvent.name()));
        }
        return EventDTO.converter(eventRepository.
                save(eventDTOtoEventConverter.convert(newEvent)));
    }

    @Transactional
    @Override
    public EventDTO addMarketToEvent(final MarketDTO marketDTO, final Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Event with ID=%d doesn't exist.", eventId)));

        Market market = marketDTOtoMarketConverter.convert(marketDTO);
        event.addMarket(market);

        return EventDTO.converter(eventRepository.save(event));
    }

    @Transactional
    @Override
    public EventDTO addParticipantToEvent(ParticipantDTO participantDTO, Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Event with ID=%d doesn't exist.", eventId)));

        Participant participant = participantDtoToParticipantConverter.convert(participantDTO);
        event.addParticipant(participant);

        return EventDTO.converter(eventRepository.save(event));
    }

    @Transactional
    @Override
    public void deleteEvent(Integer eventId) {
        boolean eventExists = eventRepository.existsById(eventId);
        if (eventExists) {
            eventRepository.deleteById(eventId);
        } else {
            throw new NoSuchEntityExistsException(String.format("Event with ID=%d doesn't exist.", eventId));
        }
    }


    private Event update(final Event event, final EventDTO toUpdateEvent) {
        event.setName(toUpdateEvent.name());
        event.setStartTime(toUpdateEvent.startTime());
        event.setEndTime(toUpdateEvent.endTime());
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
