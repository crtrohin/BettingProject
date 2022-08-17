package com.codefactorygroup.betting.repository;

import com.codefactorygroup.betting.domain.Selection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectionRepository extends JpaRepository<Selection, Integer> {

    @Query(
            value = "SELECT * FROM SELECTION s WHERE s.market_id=?1",
            nativeQuery = true
    )
    List<Selection> findSelectionsByMarketId(Integer marketId);
}
