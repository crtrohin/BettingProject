package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.dto.MarketDTO;

public interface MarketService {
    MarketDTO getMarket(Integer marketId);

    MarketDTO addMarket(MarketDTO market);

    void deleteMarket(Integer marketId);

    MarketDTO updateMarket(MarketDTO market, Integer marketId);
}
