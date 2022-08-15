package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.CompetitionDTOtoCompetitionConverter;
import com.codefactorygroup.betting.converter.EventDTOtoEventConverter;
import com.codefactorygroup.betting.domain.Competition;
import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.CompetitionRepository;
import com.codefactorygroup.betting.service.CompetitionService;
import com.codefactorygroup.betting.service.EventService;
import com.codefactorygroup.betting.service.ParticipantService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Component(value = "competitionService")
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final CompetitionDTOtoCompetitionConverter competitionDTOtoCompetitionConverter;
    private final EventDTOtoEventConverter eventDTOtoEventConverter;
    private final EventService eventService;
    private final ParticipantService participantService;

    public CompetitionServiceImpl(CompetitionRepository competitionRepository, EventService eventService,
                                  CompetitionDTOtoCompetitionConverter competitionDTOtoCompetitionConverter,
                                  EventDTOtoEventConverter eventDTOtoEventConverter, ParticipantService participantService) {
        this.competitionRepository = competitionRepository;
        this.eventService = eventService;
        this.competitionDTOtoCompetitionConverter = competitionDTOtoCompetitionConverter;
        this.eventDTOtoEventConverter = eventDTOtoEventConverter;
        this.participantService = participantService;
    }

    @Transactional
    @Override
    public CompetitionDTO getCompetition(Integer competitionId) {
        return competitionRepository.findById(competitionId)
                .map(CompetitionDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Competition with ID = %d doesn't exist.", competitionId)));
    }

    @Transactional
    @Override
    public CompetitionDTO addCompetition(CompetitionDTO newCompetition) {
        List<EventDTO> eventDTOS = Optional.of(newCompetition).map(CompetitionDTO::events).orElseGet(Collections::emptyList);

        List<Event> events = eventDTOS
                .stream()
                .map(event ->
                        eventService
                                .findEventByNameAndStartTimeAndEndTime(event.name(), event.startTime(),
                                        event.endTime(),
                                        Optional
                                                .of(event)
                                                .map(EventDTO::participants)
                                                .orElseGet(Collections::emptyList)
                                                .stream()
                                                .map(participant -> participantService.findParticipantByName(participant.name()))
                                                .collect(Collectors.toList())))
                .collect(Collectors.toList());

        Competition competition = competitionDTOtoCompetitionConverter.convert(newCompetition);

        competition.setEvents(events);

        return CompetitionDTO.converter(competitionRepository.
                save(competition));
    }

    @Transactional
    @Override
    public void deleteCompetition(Integer competitionId) {
        competitionRepository.deleteById(competitionId);
    }


    private Competition update(final Competition competition, final CompetitionDTO toUpdateCompetition) {
        List<EventDTO> eventDTOS = Optional.of(toUpdateCompetition).map(CompetitionDTO::events).orElseGet(Collections::emptyList);
        competition.setName(toUpdateCompetition.name());
        competition.setEvents(eventDTOS
                .stream()
                .map(eventDTOtoEventConverter::convert)
                .collect(Collectors.toList()));
        return competition;
    }

    @Transactional
    @Override
    public CompetitionDTO updateCompetition(final CompetitionDTO newCompetition, final Integer competitionId) {
        return competitionRepository.findById(competitionId)
                .map(competitionFromDb -> update(competitionFromDb, newCompetition))
                .map(competitionRepository::save)
                .map(CompetitionDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Competition with ID = %d doesn't exist.", competitionId)));
    }

    public Competition findCompetitionByName(String name, List<Event> events) {
        Optional<Competition> competitionOptional = competitionRepository.findCompetitionByName(name);
        if (competitionOptional.isPresent()) {
            Competition competition = competitionOptional.get();

            List<Event> competitionEvents = competition.getEvents();
            competitionEvents.addAll(events);

            Set<Event> eventSet = competitionEvents.stream().collect(Collectors.toSet());
            List<Event> updatedEvents = new ArrayList<>(eventSet);

            competition.setEvents(updatedEvents);
            return competition;
        } else {
            Competition competition = new Competition();
            competition.setName(name);
            competition.setEvents(events);
            return competition;
        }
    }

}
