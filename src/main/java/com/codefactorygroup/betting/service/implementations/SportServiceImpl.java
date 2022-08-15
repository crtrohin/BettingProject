package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.CompetitionDTOtoCompetitionConverter;
import com.codefactorygroup.betting.converter.SportDTOtoSportConverter;
import com.codefactorygroup.betting.domain.Competition;
import com.codefactorygroup.betting.domain.Sport;
import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.dto.SportDTO;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.SportRepository;
import com.codefactorygroup.betting.service.CompetitionService;
import com.codefactorygroup.betting.service.EventService;
import com.codefactorygroup.betting.service.ParticipantService;
import com.codefactorygroup.betting.service.SportService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component(value = "sportService")
public class SportServiceImpl implements SportService {

    private final SportRepository sportRepository;

    private final SportDTOtoSportConverter sportDTOtoSportConverter;

    private final CompetitionDTOtoCompetitionConverter competitionDTOtoCompetitionConverter;

    private final CompetitionService competitionService;

    private final EventService eventService;

    private final ParticipantService participantService;

    public SportServiceImpl(SportRepository sportRepository, SportDTOtoSportConverter sportDTOtoSportConverter,
                            CompetitionDTOtoCompetitionConverter competitionDTOtoCompetitionConverter,
                            CompetitionService competitionService, EventService eventService, ParticipantService participantService) {
        this.sportRepository = sportRepository;
        this.sportDTOtoSportConverter = sportDTOtoSportConverter;
        this.competitionDTOtoCompetitionConverter = competitionDTOtoCompetitionConverter;
        this.competitionService = competitionService;
        this.eventService = eventService;
        this.participantService = participantService;
    }

    @Transactional
    @Override
    public SportDTO getSport(Integer sportId) {
        return sportRepository.findById(sportId)
                .map(SportDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Sport with ID = %d doesn't exist.", sportId)));
    }


    @Transactional
    @Override
    public SportDTO addSport(SportDTO newSport) {
        List<CompetitionDTO> competitionDTO = Optional
                .of(newSport)
                .map(SportDTO::competitions)
                .orElseGet(Collections::emptyList);

        List<Competition> competitions = competitionDTO
                .stream()
                .map(competition -> competitionService.findCompetitionByName(competition.name(),
                        Optional
                                .of(competition)
                                .map(CompetitionDTO::events)
                                .orElseGet(Collections::emptyList)
                                .stream()
                                .map(event -> eventService.findEventByNameAndStartTimeAndEndTime(event.name(), event.startTime(),
                                        event.endTime(), event.participants()
                                                .stream()
                                                .map(participantDTO -> participantService.findParticipantByName(participantDTO.name()))
                                                .collect(Collectors.toList())))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());

        Sport sport = sportDTOtoSportConverter.convert(newSport);

        sport.setCompetitions(competitions);

        return SportDTO.converter(sportRepository.save(sport));
    }


    @Transactional
    @Override
    public void deleteSport(Integer sportId) {
        sportRepository.deleteById(sportId);
    }

    private Sport update(final Sport sport, final SportDTO toUpdateSport) {
        List<CompetitionDTO> competitionDTOList = Optional.of(toUpdateSport).map(SportDTO::competitions).orElseGet(Collections::emptyList);
        sport.setName(toUpdateSport.name());
        sport.setCompetitions(competitionDTOList
                .stream()
                .map(competitionDTOtoCompetitionConverter::convert)
                .collect(Collectors.toList()));
        return sport;
    }

    @Transactional
    @Override
    public SportDTO updateSport(final SportDTO newSport, final Integer sportId) {

        return sportRepository.findById(sportId)
                .map(sportFromDb -> update(sportFromDb, newSport))
                .map(sportRepository::save)
                .map(SportDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Sport with ID = %d doesn't exist.", sportId)));
    }
}
