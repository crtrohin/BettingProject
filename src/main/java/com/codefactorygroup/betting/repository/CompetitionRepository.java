package com.codefactorygroup.betting.repository;

import com.codefactorygroup.betting.domain.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Integer> {

    @Query(
            value = "SELECT * FROM competition c WHERE c.sport_id = ?1",
            nativeQuery = true
    )
    List<Competition> findCompetitionsBySportId(Integer sportId);

    Optional<Competition> findByName(String name);
}
