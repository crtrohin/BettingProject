package com.codefactorygroup.betting.repository;

import com.codefactorygroup.betting.domain.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Integer> {
    Optional<Competition> findCompetitionByName(String name);
}
