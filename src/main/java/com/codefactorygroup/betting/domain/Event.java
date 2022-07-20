package com.codefactorygroup.betting.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany
    @JoinTable(
            name = "event_participants",
            joinColumns = @JoinColumn(name = "event_id",  referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id",  referencedColumnName = "id"))
    @Column(name ="participants")
    private List<Participant> participants = new ArrayList<>();

    public void setParticipants(List<Participant> participants) {
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
}
