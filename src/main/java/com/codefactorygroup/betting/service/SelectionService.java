package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.dto.SelectionDTO;
import org.springframework.stereotype.Service;

@Service
public interface SelectionService {
    SelectionDTO getSelection(Integer selectionId);

    SelectionDTO addSelection(SelectionDTO selection);

    void deleteSelection(Integer selectionId);

    SelectionDTO updateSelection(SelectionDTO selection, Integer selectionId);
}
