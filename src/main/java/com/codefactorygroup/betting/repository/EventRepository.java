package com.codefactorygroup.betting.repository;

import com.codefactorygroup.betting.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query(
            value = "SELECT * FROM EVENT e WHERE e.competition_id=?1",
            nativeQuery = true
    )
    List<Event> findEventsByCompetitionId(Integer competitionId);

    @Query(
            value = "SELECT *\n" +
                    "FROM EVENT e\n" +
                    "WHERE e.id IN\n" +
                    "(SELECT EVENT_ID FROM EVENT_PARTICIPANTS ep WHERE ep.participant_id=?1)",
            nativeQuery = true
    )
    List<Event> findEventsByParticipantId(Integer participantId);

    @Query(
            value = "SELECT *\n" +
                    "FROM EVENT e\n" +
                    "WHERE e.id IN\n" +
                    "(SELECT EVENT_ID FROM MARKET m WHERE m.id=?1)",
            nativeQuery = true
    )
    List<Event> findEventsByMarketId(Integer marketId);

    Optional<Event> findByNameAndStartTimeAndEndTime(String name, String startTime, String endTime);

    @Query(
            value = "SELECT COUNT(e.id) > 0 \n" +
                    "FROM EVENT e \n" +
                    "WHERE e.competition_id=?1 AND e.name=?2 AND e.start_time=?3 AND e.end_time=?4",
            nativeQuery = true
    )
    boolean existsEventByCompetitionIdAndNameAndStartTimeAndEndTime(Integer competitionId, String name,
                                                                    String startTime, String endTime);

}
