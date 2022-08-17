package com.codefactorygroup.betting.repository;

import com.codefactorygroup.betting.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer>{
    @Query(value = """
            SELECT *
            FROM participant pa
            WHERE pa.id IN (SELECT p.id
            FROM participant p
            ORDER BY RAND())
            LIMIT :numberOfParticipants
            """
            ,nativeQuery = true)
    List<Participant> findRandomParticipantsDTO(
            @Param("numberOfParticipants") Integer numberOfParticipants);


    @Query(
            value = "SELECT * \n" +
                    "FROM PARTICIPANT p\n" +
                    "WHERE p.id IN (\n" +
                    "SELECT PARTICIPANT_ID FROM EVENT_PARTICIPANTS ep WHERE ep.event_id = 1)\n",
            nativeQuery = true
    )
    List<Participant> findParticipantsByEventId(Integer eventId);

    Optional<Participant> findByName(String name);
}
