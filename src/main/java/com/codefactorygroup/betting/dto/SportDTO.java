package com.codefactorygroup.betting.dto;

import com.codefactorygroup.betting.domain.Competition;
import com.codefactorygroup.betting.domain.Sport;
import lombok.Builder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Builder
public record SportDTO(Integer id, String name, List<CompetitionDTO> competitions) {

    public static SportDTO converter(final Sport sport) {
        List <Competition> competitionList = Optional.of(sport).map(Sport::getCompetitions).orElseGet(Collections::emptyList);
        return SportDTO.builder()
                .id(sport.getId())
                .name(sport.getName())
                .competitions(competitionList
                        .stream()
                        .map(CompetitionDTO::converter)
                        .toList())
                .build();
    }
}
