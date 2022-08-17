package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.dto.MarketDTO;
import com.codefactorygroup.betting.dto.SelectionDTO;
import com.codefactorygroup.betting.service.MarketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MarketController {
    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping("/markets/{marketId}")
    public MarketDTO getMarket(@PathVariable(name = "marketId") final Integer marketId) {
        return marketService.getMarket(marketId);
    }

    @GetMapping("/markets")
    public List<MarketDTO> getAllMarkets() {
        return marketService.getAllMarkets();
    }


    @GetMapping("/events/{eventId}/markets")
    public List<MarketDTO> getMarketsByEventId(@PathVariable(name = "eventId") final Integer eventId) {
        return marketService.getMarketsByEventId(eventId);
    }

    @PostMapping("/markets")
    public MarketDTO addMarket(@RequestBody final MarketDTO market) {
        return marketService.addMarket(market);
    }

    @DeleteMapping("/markets/{marketId}")
    public void deleteMarket(@PathVariable(name = "marketId") final Integer marketId) {
        marketService.deleteMarket(marketId);
    }

    @PutMapping("/markets/{marketId}")
    public MarketDTO updateMarket(@RequestBody final MarketDTO market, @PathVariable(name = "marketId") final Integer marketId) {
        return marketService.updateMarket(market, marketId);
    }

    @PutMapping("/markets/{marketId}/selections")
    public MarketDTO addSelectionToMarket(@RequestBody final SelectionDTO selectionDTO, @PathVariable(name = "marketId") final Integer marketId) {
        return marketService.addSelectionToMarket(selectionDTO, marketId);
    }
}
