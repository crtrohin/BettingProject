package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.MarketDTOtoMarketConverter;
import com.codefactorygroup.betting.converter.SelectionDTOtoSelectionConverter;
import com.codefactorygroup.betting.domain.Market;
import com.codefactorygroup.betting.domain.Selection;
import com.codefactorygroup.betting.dto.MarketDTO;
import com.codefactorygroup.betting.dto.SelectionDTO;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.MarketRepository;
import com.codefactorygroup.betting.service.MarketService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "marketService")
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;

    private final MarketDTOtoMarketConverter marketDTOtoMarketConverter;

    private final SelectionDTOtoSelectionConverter selectionDTOtoSelectionConverter;


    public MarketServiceImpl(MarketRepository marketRepository, MarketDTOtoMarketConverter marketDTOtoMarketConverter, SelectionDTOtoSelectionConverter selectionDTOtoSelectionConverter) {
        this.marketRepository = marketRepository;
        this.marketDTOtoMarketConverter = marketDTOtoMarketConverter;
        this.selectionDTOtoSelectionConverter = selectionDTOtoSelectionConverter;
    }

    @Override
    public MarketDTO getMarket(Integer marketId) {
        return marketRepository.findById(marketId)
                .map(MarketDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Market with ID=%d doesn't exist.", marketId)));
    }

    @Override
    public List<MarketDTO> getAllMarkets() {
        return marketRepository.findAll()
                .stream()
                .map(MarketDTO::converter)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketDTO> getMarketsByEventId(Integer eventId) {
        return marketRepository.findMarketsByEventId(eventId)
                .stream()
                .map(MarketDTO::converter)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MarketDTO addMarket(MarketDTO market) {
        return MarketDTO.converter(
                marketRepository.save(marketDTOtoMarketConverter.convert(market)));
    }

    @Transactional
    @Override
    public MarketDTO addSelectionToMarket(SelectionDTO selectionDTO, Integer marketId) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Market with ID=%d doesn't exist.", marketId)));

        Selection selection = selectionDTOtoSelectionConverter.convert(selectionDTO);
        market.getSelections().add(selection);

        return MarketDTO.converter(marketRepository.save(market));
    }

    @Transactional
    @Override
    public void deleteMarket(Integer marketId) {
        marketRepository.deleteById(marketId);
    }

    private Market update(final Market market, final MarketDTO toUpdateMarket) {
        List<SelectionDTO> selectionDTOS = Optional.of(toUpdateMarket).map(MarketDTO::selections).orElseGet(Collections::emptyList);
        market.setId(toUpdateMarket.id());
        market.setName(toUpdateMarket.name());
        market.setSelections(selectionDTOS
                .stream()
                .map(selectionDTOtoSelectionConverter::convert)
                .toList()
        );
        return market;
    }


    @Transactional
    @Override
    public MarketDTO updateMarket(final MarketDTO newMarket, final Integer marketId) {
        return marketRepository.findById(marketId)
                .map(marketFromDb -> update(marketFromDb, newMarket))
                .map(marketRepository::save)
                .map(MarketDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Market with ID=%d doesn't exist.", marketId)));
    }
}