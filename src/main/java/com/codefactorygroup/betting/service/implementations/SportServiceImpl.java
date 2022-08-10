package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.CompetitionDTOtoCompetitionConverter;
import com.codefactorygroup.betting.converter.SportDTOtoSportConverter;
import com.codefactorygroup.betting.domain.Sport;
import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.dto.SportDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
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

    @Transactional
    @Override
    public SportDTO getSport(Integer sportId) {
        return sportRepository.findById(sportId)
                .map(SportDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Sport with ID=%d doesn't exists.", sportId)));
    }


    @Transactional
    @Override
    public SportDTO addSport(SportDTO sport) {
        boolean foundSport = sportRepository.existsById(sport.id());
        if (foundSport) {
            throw new EntityAlreadyExistsException(String.format("Sport with ID=%d already exists.", sport.id()));
        }
        return SportDTO.converter(sportRepository.save(sportDTOtoSportConverter.convert(sport)));
    }


    @Transactional
    @Override
    public void deleteSport(Integer sportId) {
        sportRepository.deleteById(sportId);
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
