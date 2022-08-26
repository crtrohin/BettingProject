package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.CompetitionDTOtoCompetitionConverter;
import com.codefactorygroup.betting.domain.Competition;
import com.codefactorygroup.betting.domain.Sport;
import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.CompetitionRepository;
import com.codefactorygroup.betting.repository.SportRepository;
import com.codefactorygroup.betting.service.CompetitionService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "competitionService")
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final SportRepository sportRepository;
    private final CompetitionDTOtoCompetitionConverter competitionDTOtoCompetitionConverter;

    public CompetitionServiceImpl(CompetitionRepository competitionRepository,
                                  SportRepository sportRepository,
                                  CompetitionDTOtoCompetitionConverter competitionDTOtoCompetitionConverter) {
        this.competitionRepository = competitionRepository;
        this.sportRepository = sportRepository;
        this.competitionDTOtoCompetitionConverter = competitionDTOtoCompetitionConverter;
    }

    @Override
    public CompetitionDTO getCompetition(final Integer competitionId) {
        return competitionRepository.findById(competitionId)
                .map(CompetitionDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Competition with ID=%d doesn't exist.", competitionId)));
    }

    @Override
    public List<CompetitionDTO> getAllCompetitions() {
        return competitionRepository.findAll()
                .stream()
                .map(CompetitionDTO::converter)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompetitionDTO> getCompetitionsBySportId(final Integer sportId) {
        Optional<Sport> sportOptional = sportRepository.findById(sportId);
        if (sportOptional.isEmpty()) {
            throw new NoSuchEntityExistsException(String.format("Sport with ID=%d doesn't exist.", sportId));
        }
        return competitionRepository.findCompetitionsBySportId(sportId)
                .stream()
                .map(CompetitionDTO::converter)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CompetitionDTO addCompetition(final Integer sportId, final CompetitionDTO newCompetition) {
        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Sport with ID=%d doesn't exist.", sportId)));

        boolean existsBySportIdAndName = competitionRepository.existsCompetitionBySportIdAndName(sportId, newCompetition.name());
        if (existsBySportIdAndName) {
            throw new EntityAlreadyExistsException(String.format("Competition with name=%s already exists.", newCompetition.name()));
        } else {
            Competition competition = competitionDTOtoCompetitionConverter.convert(newCompetition);
            sport.addCompetition(competition);

            return CompetitionDTO.converter(competitionRepository.
                    save(competition));
        }
    }


    @Transactional
    @Override
    public void deleteCompetition(final Integer competitionId) {
        boolean existsCompetition = competitionRepository.existsById(competitionId);
        if (existsCompetition) {
            competitionRepository.deleteById(competitionId);
        } else {
            throw new NoSuchEntityExistsException(String.format("Competition with ID=%d doesn't exist.", competitionId));
        }
    }

    private Competition update(final Competition competition, final CompetitionDTO toUpdateCompetition) {
        competition.setName(toUpdateCompetition.name());
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
