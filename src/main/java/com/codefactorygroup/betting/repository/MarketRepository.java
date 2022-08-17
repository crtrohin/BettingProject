package com.codefactorygroup.betting.repository;

import com.codefactorygroup.betting.domain.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketRepository extends JpaRepository<Market, Integer> {

    @Query(
            value = "SELECT * FROM market m WHERE m.event_id = ?1",
            nativeQuery = true
    )
    List<Market> findMarketsByEventId(Integer eventId);
}
