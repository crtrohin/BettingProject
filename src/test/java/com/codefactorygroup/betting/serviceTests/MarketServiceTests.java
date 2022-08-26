package com.codefactorygroup.betting.serviceTests;

import com.codefactorygroup.betting.converter.MarketDTOtoMarketConverter;
import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.domain.Market;
import com.codefactorygroup.betting.dto.MarketDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.MarketRepository;
import com.codefactorygroup.betting.repository.EventRepository;
import com.codefactorygroup.betting.service.implementations.MarketServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarketServiceTests {

    @Mock
    private MarketDTOtoMarketConverter marketDtoToMarketConverter;
    @Mock
    private MarketRepository marketRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private MarketServiceImpl marketService;

    @Test
    void getMarketShouldReturnMarket() {
        Optional<Market> optionalMarket = Optional.of(Market
                .builder()
                .id(10)
                .name("1x2")
                .build());

        when(marketRepository.findById(10)).thenReturn(optionalMarket);

        MarketDTO marketDTO = marketService.getMarket(10);
        Market market = optionalMarket.get();

        assertThat(marketDTO).isNotNull();
        assertEquals(market.getId(), marketDTO.id());
        assertEquals(market.getName(), marketDTO.name());
    }

    @Test
    void getMarketShouldReturnException() {
        when(marketRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> marketService.getMarket(10));
    }

    @Test
    void getAllMarketsShouldReturnMarkets() {
        Market market1 = Market
                .builder()
                .id(1)
                .name("1x2")
                .build();

        Market market2 = Market
                .builder()
                .id(2)
                .name("Top 3")
                .build();

        Market market3 = Market
                .builder()
                .id(3)
                .name("Handicap")
                .build();

        List<Market> markets = List.of(market1, market2, market3);

        when(marketRepository.findAll()).thenReturn(markets);

        List<MarketDTO> marketDTOS = marketService.getAllMarkets();

        assertEquals(marketDTOS.get(0).id(), market1.getId());
        assertEquals(marketDTOS.get(0).name(), market1.getName());

        assertEquals(marketDTOS.get(1).id(), market2.getId());
        assertEquals(marketDTOS.get(1).name(), market2.getName());

        assertEquals(marketDTOS.get(2).id(), market3.getId());
        assertEquals(marketDTOS.get(2).name(), market3.getName());
    }


    @Test
    void addMarketShouldReturnMarket() {
        MarketDTO marketDTO = MarketDTO
                .builder()
                .name("1x2")
                .selections(Collections.emptyList())
                .build();

        Market market1 = Market
                .builder()
                .name("1x2")
                .selections(Collections.emptyList())
                .build();

        Market market2 = Market
                .builder()
                .id(2)
                .name("Handicap")
                .selections(Collections.emptyList())
                .build();

        List<Market> markets = new ArrayList<>();
        markets.add(market2);

        Event event = Event
                .builder().id(1)
                .name("Dortmund vs Bayern")
                .startTime("Aug 27, 16:30")
                .endTime("Aug 27, 18:00")
                .participants(Collections.emptyList())
                .markets(markets)
                .build();

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(marketRepository.existsByEventIdAndName(1, "1x2")).thenReturn(false);
        when(marketDtoToMarketConverter.convert(marketDTO)).thenReturn(market1);
        when(marketRepository.save(market1)).thenReturn(market1);

        MarketDTO savedMarket = marketService.addMarket(1, marketDTO);

        assertEquals(marketDTO, savedMarket);
    }

    @Test
    void addMarketShouldReturnExceptionNoEventExists() {
        MarketDTO marketDTO = MarketDTO
                .builder()
                .name("1x2")
                .selections(Collections.emptyList())
                .build();

        when(eventRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> marketService.addMarket(1, marketDTO));
    }

    @Test
    void addMarketShouldReturnExceptionMarketAlreadyExists() {
        MarketDTO marketDTO = MarketDTO
                .builder()
                .name("1x2")
                .selections(Collections.emptyList())
                .build();

        Market market1 = Market
                .builder()
                .name("1x2")
                .selections(Collections.emptyList())
                .build();

        List<Market> markets = new ArrayList<>();
        markets.add(market1);

        Event event = Event
                .builder()
                .id(1)
                .name("Dortmund vs Bayern")
                .startTime("Aug 27, 16:30")
                .endTime("Aug 27, 18:00")
                .participants(Collections.emptyList())
                .markets(markets)
                .build();

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(marketRepository.existsByEventIdAndName(1, "1x2")).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> marketService.addMarket(1, marketDTO));
    }

    @Test
    void updateMarketShouldReturnMarket() {
        Market marketFromDb = Market
                .builder()
                .id(1)
                .name("1x2")
                .selections(Collections.emptyList())
                .build();

        Market updatedMarket = Market
                .builder()
                .id(1)
                .name("Top 3")
                .selections(Collections.emptyList())
                .build();

        MarketDTO toUpdateMarketDTO = MarketDTO
                .builder()
                .id(1)
                .name("Top 3")
                .selections(Collections.emptyList())
                .build();

        when(marketRepository.findById(1)).thenReturn(Optional.of(marketFromDb));
        when(marketRepository.save(updatedMarket)).thenReturn(updatedMarket);

        MarketDTO resultedMarketDTO = marketService.updateMarket(toUpdateMarketDTO, 1);

        assertEquals(resultedMarketDTO, toUpdateMarketDTO);
    }

    @Test
    void updateMarketShouldReturnException() {
        MarketDTO toUpdateMarketDTO = MarketDTO
                .builder()
                .id(1)
                .name("1x2")
                .build();

        when(marketRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> marketService.updateMarket(toUpdateMarketDTO, 1));
    }


    @Test
    void deleteMarket() {
        when(marketRepository.existsById(1)).thenReturn(true);

        marketService.deleteMarket(1);

        // check if the method was called
        verify(marketRepository).deleteById(1);
    }


    @Test
    void deleteMarketShouldReturnException() {
        when(marketRepository.existsById(1)).thenReturn(false);

        assertThrows(NoSuchEntityExistsException.class, () -> marketService.deleteMarket(1));
    }

}
