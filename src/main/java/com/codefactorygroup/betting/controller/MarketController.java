package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.dto.MarketDTO;
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

    @PostMapping("events/{eventId}/markets")
    public MarketDTO addMarket(@PathVariable(name ="eventId") final Integer eventId, @RequestBody final MarketDTO market) {
        return marketService.addMarket(eventId, market);
    }

    @DeleteMapping("/markets/{marketId}")
    public void deleteMarket(@PathVariable(name = "marketId") final Integer marketId) {
        marketService.deleteMarket(marketId);
    }

    @PutMapping("/markets/{marketId}")
    public MarketDTO updateMarket(@RequestBody final MarketDTO market, @PathVariable(name = "marketId") final Integer marketId) {
        return marketService.updateMarket(market, marketId);
    }

}
