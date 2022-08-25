package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.EventDTOtoEventConverter;
import com.codefactorygroup.betting.domain.Competition;
import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.domain.Market;
import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.EntityIsAlreadyLinked;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.CompetitionRepository;
import com.codefactorygroup.betting.repository.EventRepository;
import com.codefactorygroup.betting.repository.MarketRepository;
import com.codefactorygroup.betting.repository.ParticipantRepository;
import com.codefactorygroup.betting.service.EventService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "eventService")
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final CompetitionRepository competitionRepository;

    private final ParticipantRepository participantRepository;

    private final MarketRepository marketRepository;

    private final EventDTOtoEventConverter eventDTOtoEventConverter;


    public EventServiceImpl(EventRepository eventRepository, CompetitionRepository competitionRepository,
                            ParticipantRepository participantRepository, MarketRepository marketRepository, EventDTOtoEventConverter eventDTOtoEventConverter) {
        this.eventRepository = eventRepository;
        this.competitionRepository = competitionRepository;
        this.participantRepository = participantRepository;
        this.marketRepository = marketRepository;
        this.eventDTOtoEventConverter = eventDTOtoEventConverter;
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
        Optional<Competition> competitionOptional = competitionRepository.findById(competitionId);
        if (competitionOptional.isEmpty()) {
            throw new NoSuchEntityExistsException(String.format("Competition with ID=%d doesn't exist.", competitionId));
        }
        return eventRepository.findEventsByCompetitionId(competitionId)
                .stream()
                .map(EventDTO::converter)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByParticipantId(Integer participantId) {
        Optional<Participant> participantOptional = participantRepository.findById(participantId);
        if (participantOptional.isEmpty()) {
            throw new NoSuchEntityExistsException(String.format("Participant with ID=%d doesn't exist.", participantId));
        }
        return eventRepository.findEventsByParticipantId(participantId)
                .stream()
                .map(EventDTO::converter)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByMarketId(Integer marketId) {
        Optional<Market> marketOptional = marketRepository.findById(marketId);
        if (marketOptional.isEmpty()) {
            throw new NoSuchEntityExistsException(String.format("Market with ID=%d doesn't exist.", marketId));
        }
        return eventRepository.findEventsByMarketId(marketId)
                .stream()
                .map(EventDTO::converter)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventDTO addEvent(final Integer competitionId, final EventDTO newEvent) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Competition with ID=%d doesn't exist.", competitionId)));

        boolean existsEvent = eventRepository.existsEventByCompetitionIdAndNameAndStartTimeAndEndTime(competitionId, newEvent.name(),
                newEvent.startTime(), newEvent.endTime());
        if (existsEvent) {
            throw new EntityAlreadyExistsException(String.format("Event with name=%s, startTime=%s, endTime=%s already exists.",
                    newEvent.name(), newEvent.startTime(), newEvent.endTime()));
        }
        Event event = eventDTOtoEventConverter.convert(newEvent);
        competition.addEvent(event);

        return EventDTO.converter(eventRepository.
                save(event));
    }

    @Transactional
    @Override
    public EventDTO addParticipantToEvent(Integer participantId, Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Event with ID=%d doesn't exist.", eventId)));

        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Participant with ID=%d doesn't exist.", participantId)));

        boolean isParticipantLinkedToEvent = participantRepository.isParticipantLinkedToEvent(participantId, eventId);
        if (isParticipantLinkedToEvent) {
            throw new EntityIsAlreadyLinked(String.format("Participant with ID=%d is already linked to event with ID=%d.", participantId,
                    eventId));
        } else {
            event.addParticipant(participant);
            return EventDTO.converter(eventRepository.save(event));
        }
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
