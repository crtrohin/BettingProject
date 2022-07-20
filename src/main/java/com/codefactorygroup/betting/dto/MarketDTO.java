package com.codefactorygroup.betting.dto;

import com.codefactorygroup.betting.domain.Market;
import com.codefactorygroup.betting.domain.Selection;
import lombok.Builder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Builder
public record MarketDTO(Integer id, String name, List<SelectionDTO> selections) {

    public static MarketDTO converter(final Market market) {
        List<Selection> selections = Optional.of(market).map(Market::getSelections).orElseGet(Collections::emptyList);

        return MarketDTO.builder()
                .id(market.getId())
                .name(market.getName())
                .selections(selections
                        .stream()
                        .map(SelectionDTO::converter)
                        .toList())
                .build();
    }
}
