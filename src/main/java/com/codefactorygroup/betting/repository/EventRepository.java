package com.codefactorygroup.betting.repository;

import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Optional<Event> findByNameAndStartTimeAndEndTime(String name,
                                                     String startTime,
                                                     String endTime);
}
