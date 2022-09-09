package com.codefactorygroup.betting.repository;

import com.codefactorygroup.betting.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer>{
    @Query(
            value = "SELECT COUNT(e_p.event_id) > 0\n" +
                    "FROM EVENT_PARTICIPANTS e_p\n" +
                    "WHERE e_p.event_id=?2 AND e_p.participant_id=?1",
            nativeQuery = true
    )
    boolean isParticipantLinkedToEvent(Integer participantId, Integer eventId);

    @Query(
            value = "SELECT * \n" +
                    "FROM PARTICIPANT p\n" +
                    "WHERE p.id IN (\n" +
                    "SELECT PARTICIPANT_ID FROM EVENT_PARTICIPANTS ep WHERE ep.event_id = 1)\n",
            nativeQuery = true
    )
    List<Participant> findParticipantsByEventId(Integer eventId);

    boolean existsParticipantByName(String name);
}
