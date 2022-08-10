package com.codefactorygroup.betting.domain;

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

    public void removeCompetition(Competition competition) {
        this.competitions.remove(competition);
    }

    public void setCompetitions(List<Competition> competitions) {
        this.competitions.addAll(competitions);
    }
}
