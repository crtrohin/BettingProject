package com.codefactorygroup.betting.repository;

import com.codefactorygroup.betting.domain.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Integer> {

    @Query(
            value = "SELECT * FROM competition c WHERE c.sport_id = ?1",
            nativeQuery = true
    )
    List<Competition> findCompetitionsBySportId(Integer sportId);

    @Query(
            value = "SELECT COUNT(c.id) > 0 \n" +
                    "FROM COMPETITION c \n" +
                    "WHERE c.sport_id=?1 AND c.name=?2",
            nativeQuery = true
    )
    boolean existsCompetitionBySportIdAndName(Integer sportId, String name);

}
