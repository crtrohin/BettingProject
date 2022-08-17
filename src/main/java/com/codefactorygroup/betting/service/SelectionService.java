package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.dto.SelectionDTO;

import java.util.List;

public interface SelectionService {
    SelectionDTO getSelection(Integer selectionId);

    List<SelectionDTO> getAllSelections();

    List<SelectionDTO> getSelectionsByMarketId(Integer marketId);

    SelectionDTO addSelection(SelectionDTO selection);

    void deleteSelection(Integer selectionId);

    SelectionDTO updateSelection(SelectionDTO selection, Integer selectionId);
}
