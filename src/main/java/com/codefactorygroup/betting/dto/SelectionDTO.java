package com.codefactorygroup.betting.dto;

import com.codefactorygroup.betting.domain.Selection;
import com.codefactorygroup.betting.domain.SelectionResult;
import lombok.Builder;

@Builder
public record SelectionDTO(Integer id, String name, Integer odds, SelectionResult selectionResult) {

    public static SelectionDTO converter(final Selection selection) {
        return SelectionDTO.builder()
                .id(selection.getId())
                .name(selection.getName())
                .odds(selection.getOdds())
                .selectionResult(selection.getSelectionResult())
                .build();
    }
}
