package com.codefactorygroup.betting.dto;

import com.codefactorygroup.betting.domain.Market;
import com.codefactorygroup.betting.domain.Selection;
import lombok.Builder;

@Builder
public record SelectionDTO(Integer id, String name, Integer odds) {

    public static SelectionDTO converter(final Selection selection) {
        return SelectionDTO.builder()
                .id(selection.getId())
                .name(selection.getName())
                .odds(selection.getOdds())
                .build();
    }
}
