package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.CompetitionDTOtoCompetitionConverter;
import com.codefactorygroup.betting.converter.EventDTOtoEventConverter;
import com.codefactorygroup.betting.domain.Competition;
import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.CompetitionRepository;
import com.codefactorygroup.betting.service.CompetitionService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "competitionService")
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final CompetitionDTOtoCompetitionConverter competitionDTOtoCompetitionConverter;

    private final EventDTOtoEventConverter eventDTOtoEventConverter;

    public CompetitionServiceImpl(CompetitionRepository competitionRepository, CompetitionDTOtoCompetitionConverter competitionDTOtoCompetitionConverter, EventDTOtoEventConverter eventDTOtoEventConverter) {
        this.competitionRepository = competitionRepository;
        this.competitionDTOtoCompetitionConverter = competitionDTOtoCompetitionConverter;
        this.eventDTOtoEventConverter = eventDTOtoEventConverter;
    }

    @Transactional
    @Override
    public CompetitionDTO getCompetition(Integer competitionId) {
        return competitionRepository.findById(competitionId)
                .map(CompetitionDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Competition with ID=%d doesn't exists.", competitionId)));
    }

    @Transactional
    @Override
    public CompetitionDTO addCompetition(CompetitionDTO newCompetition) {
        boolean foundCompetition = competitionRepository.existsById(newCompetition.id());
        if (foundCompetition) {
            throw new EntityAlreadyExistsException(String.format("Competition with ID=%d already exists.", newCompetition.id()));
        }
        return CompetitionDTO.converter(competitionRepository.
                save(competitionDTOtoCompetitionConverter.convert(newCompetition)));
    }

    @Transactional
    @Override
    public void deleteCompetition(Integer competitionId) {
        competitionRepository.deleteById(competitionId);
    }


    private Competition update(final Competition competition, final CompetitionDTO toUpdateCompetition) {
        List<EventDTO> eventDTOS = Optional.of(toUpdateCompetition).map(CompetitionDTO::events).orElseGet(Collections::emptyList);
        competition.setId(toUpdateCompetition.id());
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
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Competition with ID=%d doesn't exist.", competitionId)));
    }

}
