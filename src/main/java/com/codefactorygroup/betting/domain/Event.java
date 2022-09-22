package com.codefactorygroup.betting.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Column(name = "in_play")
    private Boolean inPlay;

    @ManyToMany(
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            }
    )
    @JoinTable(
            name = "event_participants",
            joinColumns = @JoinColumn(name = "event_id",  referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id",  referencedColumnName = "id"))
    @Column(name ="participants")
    private List<Participant> participants = new ArrayList<>();

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
    }

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "event_id")
    @Column(name = "markets")
    private List<Market> markets = new ArrayList<>();

    public void addMarket(Market market) {
        this.markets.add(market);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(startTime, event.startTime) && Objects.equals(endTime, event.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startTime, endTime);
    }

    public String getShortName() {
        return participants.stream()
                .map(Participant::getName)
                .map(n -> n.substring(0, 3))
                .collect(Collectors.joining("-"));
    }
}
