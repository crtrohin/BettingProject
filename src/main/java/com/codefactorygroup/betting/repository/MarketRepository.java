package com.codefactorygroup.betting.repository;

import com.codefactorygroup.betting.domain.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository extends JpaRepository<Market, Integer> {
}
