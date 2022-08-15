package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.SelectionDTOtoSelectionConverter;
import com.codefactorygroup.betting.domain.Selection;
import com.codefactorygroup.betting.dto.SelectionDTO;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.SelectionRepository;
import com.codefactorygroup.betting.service.SelectionService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component(value = "selectionService")
public class SelectionServiceImpl implements SelectionService {

    private final SelectionRepository selectionRepository;

    private final SelectionDTOtoSelectionConverter selectionDTOtoSelectionConverter;

    public SelectionServiceImpl(SelectionRepository selectionRepository, SelectionDTOtoSelectionConverter selectionDTOtoSelectionConverter) {
        this.selectionRepository = selectionRepository;
        this.selectionDTOtoSelectionConverter = selectionDTOtoSelectionConverter;
    }

    @Transactional
    @Override
    public SelectionDTO getSelection(Integer selectionId) {
        return selectionRepository.findById(selectionId)
                .map(SelectionDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Selection with ID = %d doesn't exist.", selectionId)));
    }

    @Transactional
    @Override
    public SelectionDTO addSelection(SelectionDTO selection) {
        return SelectionDTO.converter(selectionRepository.save(selectionDTOtoSelectionConverter.convert(selection)));
    }

    @Transactional
    @Override
    public void deleteSelection(Integer selectionId) {
        selectionRepository.deleteById(selectionId);
    }


    private Selection update(final Selection selection, final SelectionDTO toUpdateSelection) {
        selection.setName(toUpdateSelection.name());
        selection.setOdds(toUpdateSelection.odds());
        return selection;
    }

    @Transactional
    @Override
    public SelectionDTO updateSelection(final SelectionDTO newSelection, final Integer selectionId) {
        return selectionRepository.findById(selectionId)
                .map(selectionFromDb -> update(selectionFromDb, newSelection))
                .map(selectionRepository::save)
                .map(SelectionDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Selection with ID = %d doesn't exist.", selectionId)));
    }
}