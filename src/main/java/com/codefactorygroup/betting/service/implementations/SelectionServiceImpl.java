package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.SelectionDTOtoSelectionConverter;
import com.codefactorygroup.betting.converter.SelectionResultDTOtoSelectionResultConverter;
import com.codefactorygroup.betting.domain.Market;
import com.codefactorygroup.betting.domain.Selection;
import com.codefactorygroup.betting.dto.SelectionDTO;
import com.codefactorygroup.betting.dto.SelectionResultDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.kafka.KafkaProducerSelections;
import com.codefactorygroup.betting.repository.MarketRepository;
import com.codefactorygroup.betting.repository.SelectionRepository;
import com.codefactorygroup.betting.service.SelectionService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "selectionService")
public class SelectionServiceImpl implements SelectionService {

    private final SelectionRepository selectionRepository;

    private final MarketRepository marketRepository;

    private final SelectionDTOtoSelectionConverter selectionDTOtoSelectionConverter;

    private final SelectionResultDTOtoSelectionResultConverter selectionResultDTOtoSelectionResultConverter;

    private final KafkaProducerSelections kafkaProducerSelections;

    public SelectionServiceImpl(SelectionRepository selectionRepository, MarketRepository marketRepository,
                                SelectionDTOtoSelectionConverter selectionDTOtoSelectionConverter, SelectionResultDTOtoSelectionResultConverter selectionResultDTOtoSelectionResultConverter, KafkaProducerSelections kafkaProducerSelections) {
        this.selectionRepository = selectionRepository;
        this.marketRepository = marketRepository;
        this.selectionDTOtoSelectionConverter = selectionDTOtoSelectionConverter;
        this.selectionResultDTOtoSelectionResultConverter = selectionResultDTOtoSelectionResultConverter;
        this.kafkaProducerSelections = kafkaProducerSelections;
    }

    @Override
    public SelectionDTO getSelection(Integer selectionId) {
        return selectionRepository.findById(selectionId)
                .map(SelectionDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Selection with ID=%d doesn't exist.", selectionId)));
    }

    @Override
    public List<SelectionDTO> getAllSelections() {
        return selectionRepository.findAll()
                .stream()
                .map(SelectionDTO::converter)
                .collect(Collectors.toList());
    }

    @Override
    public List<SelectionDTO> getSelectionsByMarketId(Integer marketId) {
        Optional<Market> marketOptional = marketRepository.findById(marketId);
        if (marketOptional.isEmpty()) {
            throw new NoSuchEntityExistsException(String.format("Market with ID=%d doesn't exist.", marketId));
        }
        return selectionRepository.findSelectionsByMarketId(marketId)
                .stream()
                .map(SelectionDTO::converter)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public SelectionDTO addSelection(final Integer marketId, final SelectionDTO newSelection) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Market with ID=%d doesn't exist.", marketId)));

        boolean existsSelection = selectionRepository.existsByName(newSelection.name());
        if (existsSelection) {
            throw new EntityAlreadyExistsException(String.format("Selection with name=%s already exists.", newSelection.name()));
        } else {
            Selection selection = selectionDTOtoSelectionConverter.convert(newSelection);
            market.addSelection(selection);

            return SelectionDTO.converter(
                    selectionRepository.save(selection));
        }
    }

    @Transactional
    @Override
    public void deleteSelection(Integer selectionId) {
        boolean selectionExists = selectionRepository.existsById(selectionId);
        if (selectionExists) {
            selectionRepository.deleteById(selectionId);
        } else {
            throw new NoSuchEntityExistsException(String.format("Selection with ID=%d doesn't exist.", selectionId));
        }
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
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Selection with ID=%d doesn't exist.", selectionId)));
    }

    public Selection setResult(
            final Selection selection,
            final SelectionResultDTO selectionResultDTO) {
        selection.setSelectionResult(selectionResultDTOtoSelectionResultConverter.convert(selectionResultDTO));
        return selection;
    }

    @Override
    public void setSelectionResult(
            final Integer selectionId,
            final SelectionResultDTO selectionResultDTO) {

        selectionRepository.findById(selectionId)
                .map(selectionFromDb -> setResult(selectionFromDb, selectionResultDTO))
                .map(selectionRepository::save)
                .map(SelectionDTO::converter)
                .ifPresentOrElse(kafkaProducerSelections::sendUpdatedSelectionMessage,
                        () -> new NoSuchEntityExistsException(String.format("Selection with ID=%d doesn't exist.", selectionId)));
    }
}