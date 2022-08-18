package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.CompetitionDTOtoCompetitionConverter;
import com.codefactorygroup.betting.converter.SportDTOtoSportConverter;
import com.codefactorygroup.betting.domain.Competition;
import com.codefactorygroup.betting.domain.Sport;
import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.dto.SportDTO;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.SportRepository;
import com.codefactorygroup.betting.service.SportService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "sportService")
public class SportServiceImpl implements SportService {

    private final SportRepository sportRepository;

    private final SportDTOtoSportConverter sportDTOtoSportConverter;

    private final CompetitionDTOtoCompetitionConverter competitionDTOtoCompetitionConverter;

    public SportServiceImpl(SportRepository sportRepository, SportDTOtoSportConverter sportDTOtoSportConverter, CompetitionDTOtoCompetitionConverter competitionDTOtoCompetitionConverter) {
        this.sportRepository = sportRepository;
        this.sportDTOtoSportConverter = sportDTOtoSportConverter;
        this.competitionDTOtoCompetitionConverter = competitionDTOtoCompetitionConverter;
    }

    @Override
    public SportDTO getSport(Integer sportId) {
        return sportRepository.findById(sportId)
                .map(SportDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Sport with ID=%d doesn't exists.", sportId)));
    }

    @Override
    public List<SportDTO> getAllSports() {
        return sportRepository.findAll()
                .stream()
                .map(SportDTO::converter)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public SportDTO addSport(SportDTO sport) {
        return SportDTO.converter(
                sportRepository.save(sportDTOtoSportConverter.convert(sport)));
    }

    @Transactional
    @Override
    public SportDTO addCompetitionToSport(CompetitionDTO competitionDTO, Integer sportId) {
        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Sport with ID=%d doesn't exist.", sportId)));

        Competition competition = competitionDTOtoCompetitionConverter.convert(competitionDTO);
        sport.addCompetition(competition);

        return SportDTO.converter(sportRepository.save(sport));
    }


    @Transactional
    @Override
    public void deleteSport(Integer sportId) {
        boolean sportExists = sportRepository.existsById(sportId);
        if (sportExists) {
            sportRepository.deleteById(sportId);
        } else {
            throw new NoSuchEntityExistsException(String.format("Sport with ID=%d doesn't exist.", sportId));
        }
    }

    private Sport update(final Sport sport, final SportDTO toUpdateSport) {
        List<CompetitionDTO> competitionDTOList = Optional.of(toUpdateSport).map(SportDTO::competitions).orElseGet(Collections::emptyList);
        sport.setId(toUpdateSport.id());
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
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Sport with ID=%d doesn't exist.", sportId)));
    }

}
