package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.dto.MarketDTO;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public interface MarketService {
    MarketDTO getMarket(Integer marketId);

    MarketDTO addMarket(MarketDTO market);

    void deleteMarket(Integer marketId);

    MarketDTO updateMarket(MarketDTO market, Integer marketId);
}
