package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.EventDTOtoEventConverter;
import com.codefactorygroup.betting.domain.*;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.EntityIsAlreadyLinked;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.*;
import com.codefactorygroup.betting.service.EventService;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service(value = "eventService")
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final CompetitionRepository competitionRepository;

    private final SportRepository sportRepository;

    private final ParticipantRepository participantRepository;

    private final MarketRepository marketRepository;

    private final EventDTOtoEventConverter eventDTOtoEventConverter;

    static final Logger LOGGER =
            Logger.getLogger(Event.class.getName());

    public EventServiceImpl(EventRepository eventRepository, CompetitionRepository competitionRepository,
                            SportRepository sportRepository, ParticipantRepository participantRepository, MarketRepository marketRepository, EventDTOtoEventConverter eventDTOtoEventConverter) {
        this.eventRepository = eventRepository;
        this.competitionRepository = competitionRepository;
        this.sportRepository = sportRepository;
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

    @Override
    public List<Map<String, String>> getEventsShortVersion() {
        List<Map<String, String>> list = new ArrayList<>();
        List<Event> events = eventRepository.findAll();

        for (Event event : events) {
            Map<String, String> map = new HashMap<>();
            map.put("id", event.getId().toString());
            map.put("name", event.getName());
            map.put("startTime", event.getStartTime());

            List<Participant> participants = event.getParticipants();
            List<String> participantsNames = new ArrayList<>();
            for (Participant participant: participants) {
                participantsNames.add(participant.getName());
            }

            String namesConcat = participantsNames
                    .stream()
                    .reduce("", (acc, name) -> acc + "-" + name.substring(0, 3))
                    .substring(1);

            map.put("shortName", namesConcat);

            Optional<Sport> sport = sportRepository.findSportByEventId(event.getId());
            map.put("sportName", sport.get().getName());
            list.add(map);
        }

        return list;
    }

    public List<EventDTO> getEventsMarketsOrdBySelectionPricesDesc() {
        List<Event> events = eventRepository.findAll();
        for (Event event: events) {
            for (Market market: event.getMarkets()) {
                market.setSelections(market.getSelections()
                        .stream()
                        .sorted(Comparator.comparing(Selection::getOdds).reversed())
                        .collect(Collectors.toList()));
            }
        }

        return events
                .stream()
                .map(EventDTO::converter)
                .collect(Collectors.toList());
    }

    public List<EventDTO> getEventsWithNrOfMarketsGreaterThanAndNotFromFootballSport(final Integer nrOfMarkets) {
        return eventRepository.findAll()
                .stream()
                .filter(event -> sportRepository.findSportByEventId(event.getId()).get().getName() != "Football")
                .filter(event -> event.getMarkets().stream().count() > nrOfMarkets)
                .map(EventDTO::converter)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Scheduled(fixedDelay = 15000)
    public void checkIfInPlay() {

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date = new Date();

        for (Event event: eventRepository.findAll()) {
            boolean isPreMatch = dateFormat.parse(event.getStartTime()).after(date);
            boolean isAfterMatch = dateFormat.parse(event.getEndTime()).before(date);

            if (!isAfterMatch && !isPreMatch) {
                event.setInPlay(true);
                eventRepository.save(event);
            } else if (event.getInPlay() == true) {
                event.setInPlay(false);
                eventRepository.save(event);
            }
        }
    }

}
