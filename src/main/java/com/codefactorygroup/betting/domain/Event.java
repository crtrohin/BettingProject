package com.codefactorygroup.betting.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="name")
    private String name;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @ManyToMany(cascade =
            {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "event_participants",
            joinColumns = @JoinColumn(name = "event_id",  referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id",  referencedColumnName = "id"))
    @Column(name ="participants")
    private List<Participant> participants = new ArrayList<>();

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void updateParticipants(List<Participant> participants) {
        this.participants.addAll(participants);
    }

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "event_id")
    @Column(name = "markets")
    private List<Market> markets = new ArrayList<>();

    public void setMarkets(List<Market> markets) {
        this.markets.addAll(markets);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return name.equals(event.name) && Objects.equals(startTime, event.startTime) && Objects.equals(endTime, event.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, startTime, endTime);
    }
}
