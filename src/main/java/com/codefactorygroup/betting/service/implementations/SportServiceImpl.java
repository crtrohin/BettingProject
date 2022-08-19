package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.SportDTOtoSportConverter;
import com.codefactorygroup.betting.domain.Sport;
import com.codefactorygroup.betting.dto.SportDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.SportRepository;
import com.codefactorygroup.betting.service.SportService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "sportService")
public class SportServiceImpl implements SportService {

    private final SportRepository sportRepository;

    private final SportDTOtoSportConverter sportDTOtoSportConverter;


    public SportServiceImpl(SportRepository sportRepository, SportDTOtoSportConverter sportDTOtoSportConverter) {
        this.sportRepository = sportRepository;
        this.sportDTOtoSportConverter = sportDTOtoSportConverter;
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
    public SportDTO addSport(SportDTO newSport) {
        boolean existsSport = sportRepository.existsByName(newSport.name());
        if (existsSport) {
            throw new EntityAlreadyExistsException(String.format("Sport with name=%s already exists.", newSport.name()));
        } else {
            Sport sport = sportDTOtoSportConverter.convert(newSport);
            return SportDTO.converter(
                    sportRepository.save(sport));
        }
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
        sport.setName(toUpdateSport.name());
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
