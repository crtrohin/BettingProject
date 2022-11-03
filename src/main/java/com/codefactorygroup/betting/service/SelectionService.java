package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.domain.SelectionResult;
import com.codefactorygroup.betting.dto.SelectionDTO;
import com.codefactorygroup.betting.dto.SelectionResultDTO;

import java.util.List;

public interface SelectionService {
    SelectionDTO getSelection(Integer selectionId);

    List<SelectionDTO> getAllSelections();

    List<SelectionDTO> getSelectionsByMarketId(Integer marketId);

    SelectionDTO addSelection(Integer marketId, SelectionDTO selection);

    void deleteSelection(Integer selectionId);

    SelectionDTO updateSelection(SelectionDTO selection, Integer selectionId);

    SelectionDTO setSelectionResult(Integer selectionId, SelectionResultDTO selectionResultDTO);
}
