package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.dto.MarketDTO;
import com.codefactorygroup.betting.dto.SelectionDTO;

import java.util.List;

public interface MarketService {
    MarketDTO getMarket(Integer marketId);

    List<MarketDTO> getAllMarkets();

    List<MarketDTO> getMarketsByEventId(Integer eventId);

    MarketDTO addMarket(MarketDTO market);

    void deleteMarket(Integer marketId);

    MarketDTO updateMarket(MarketDTO market, Integer marketId);

    MarketDTO addSelectionToMarket(SelectionDTO selectionDTO, Integer marketId);
}
