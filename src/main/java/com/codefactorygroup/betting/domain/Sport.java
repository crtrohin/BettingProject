package com.codefactorygroup.betting.domain;

import com.codefactorygroup.betting.exception.EntityIsAlreadyLinked;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name="sport")

public class Sport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name")
    private String name;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name ="sport_id")
    @Column(name="competitions")
    private List<Competition> competitions = new ArrayList<>();

    public void addCompetition(Competition competition) {
        boolean competitionIsContained = this.competitions.contains(competition);
        if (competitionIsContained) {
            throw new EntityIsAlreadyLinked(String.format("This competition is already part of the sport."));
        } else {
            this.competitions.add(competition);
        }
    }

    public void removeCompetition(Competition competition) {
        this.competitions.remove(competition);
    }

    public void setCompetitions(List<Competition> competitions) {
        this.competitions.addAll(competitions);
    }
}
