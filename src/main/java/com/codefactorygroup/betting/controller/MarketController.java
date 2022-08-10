package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.dto.MarketDTO;
import com.codefactorygroup.betting.service.MarketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "markets")
public class MarketController {
    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping("/{marketId}")
    public MarketDTO getMarket(@PathVariable(name = "marketId") final Integer marketId) {
        return marketService.getMarket(marketId);
    }

    @PostMapping
    public MarketDTO addMarket(@RequestBody final MarketDTO market) {
        return marketService.addMarket(market);
    }

    @DeleteMapping("/{marketId}")
    public void deleteMarket(@PathVariable(name = "marketId") final Integer marketId) {
        marketService.deleteMarket(marketId);
    }

    @PutMapping("/{marketId}")
    public MarketDTO updateMarket(@RequestBody final MarketDTO market, @PathVariable(name = "marketId") final Integer marketId) {
        return marketService.updateMarket(market, marketId);
    }
}
