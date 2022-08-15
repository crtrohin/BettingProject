package com.codefactorygroup.betting.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// the central domain concept in my betting application
// we are defining here the entity class that represents a participant
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "participant")
@Entity
@Getter
@Setter
@Builder
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "events")
    @ManyToMany(
            mappedBy = "participants"
    )
    private List<Event> events = new ArrayList<>();

    public void addEvent(Event event) {
        events.add(event);
        event.getParticipants().add(this);
    }

    public void removeEvent(Event event) {
        events.remove(event);
        event.getParticipants().remove(this);
    }

    public void remove() {
        for(Event event : new ArrayList<>(events)) {
            removeEvent(event);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
