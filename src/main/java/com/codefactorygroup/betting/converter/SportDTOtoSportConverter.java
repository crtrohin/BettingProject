package com.codefactorygroup.betting.converter;

import com.codefactorygroup.betting.domain.Sport;
import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.dto.SportDTO;
import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class SportDTOtoSportConverter implements Converter<SportDTO, Sport> {

    private final CompetitionDTOtoCompetitionConverter competitionDTOtoCompetitionConverter;

    public SportDTOtoSportConverter(CompetitionDTOtoCompetitionConverter competitionDTOtoCompetitionConverter) {
        this.competitionDTOtoCompetitionConverter = competitionDTOtoCompetitionConverter;
    }

    @Override
    public Sport convert(SportDTO source) {
        List<CompetitionDTO> competitionList = Optional.of(source).map(SportDTO::competitions).orElseGet(Collections::emptyList);
        return Sport.builder()
                .id(source.id())
                .name(source.name())
                .competitions(competitionList
                        .stream()
                        .map(competitionDTOtoCompetitionConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}
