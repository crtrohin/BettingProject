package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.dto.MarketDTO;

import java.util.List;

public interface MarketService {
    MarketDTO getMarket(Integer marketId);

    List<MarketDTO> getAllMarkets();

    List<MarketDTO> getMarketsByEventId(Integer eventId);

    MarketDTO addMarket(Integer eventId, MarketDTO market);

    void deleteMarket(Integer marketId);

    MarketDTO updateMarket(MarketDTO market, Integer marketId);

}
