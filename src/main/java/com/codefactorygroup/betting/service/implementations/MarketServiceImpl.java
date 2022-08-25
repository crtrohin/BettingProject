package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.MarketDTOtoMarketConverter;
import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.domain.Market;
import com.codefactorygroup.betting.dto.MarketDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.EventRepository;
import com.codefactorygroup.betting.repository.MarketRepository;
import com.codefactorygroup.betting.service.MarketService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "marketService")
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;

    private final EventRepository eventRepository;
    private final MarketDTOtoMarketConverter marketDTOtoMarketConverter;

    public MarketServiceImpl(MarketRepository marketRepository, EventRepository eventRepository,
                             MarketDTOtoMarketConverter marketDTOtoMarketConverter) {
        this.marketRepository = marketRepository;
        this.eventRepository = eventRepository;
        this.marketDTOtoMarketConverter = marketDTOtoMarketConverter;
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
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            throw new NoSuchEntityExistsException(String.format("Event with ID=%d doesn't exist.", eventId));
        }
        return marketRepository.findMarketsByEventId(eventId)
                .stream()
                .map(MarketDTO::converter)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MarketDTO addMarket(final Integer eventId, final MarketDTO newMarket) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Event with ID=%d doesn't exist.", eventId)));

        boolean existsMarket = marketRepository.existsByEventIdAndName(eventId, newMarket.name());
        if (existsMarket) {
            throw new EntityAlreadyExistsException(String.format("Market with name=%s already exists.", newMarket.name()));
        } else {
            Market market = marketDTOtoMarketConverter.convert(newMarket);
            event.addMarket(market);

            return MarketDTO.converter(
                    marketRepository.save(market));
        }
    }


    @Transactional
    @Override
    public void deleteMarket(Integer marketId) {
        boolean marketExists = marketRepository.existsById(marketId);
        if (marketExists) {
            marketRepository.deleteById(marketId);
        } else {
            throw new NoSuchEntityExistsException(String.format("Market with ID=%d doesn't exist.", marketId));
        }
    }

    private Market update(final Market market, final MarketDTO toUpdateMarket) {
        market.setName(toUpdateMarket.name());
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