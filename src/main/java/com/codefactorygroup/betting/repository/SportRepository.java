package com.codefactorygroup.betting.repository;

import com.codefactorygroup.betting.domain.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SportRepository extends JpaRepository<Sport, Integer> {
    boolean existsByName(String name);

    @Query(value = """
            SELECT * FROM SPORT s
            WHERE s.id IN
            (SELECT sport_id FROM COMPETITION c
            WHERE c.id IN
            (SELECT competition_id FROM EVENT e
            WHERE e.id=?1))
            """, nativeQuery = true)
    Optional<Sport> findSportByEventId(Integer eventId);
}
