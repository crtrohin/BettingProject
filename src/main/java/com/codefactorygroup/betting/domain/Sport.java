package com.codefactorygroup.betting.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        this.competitions.add(competition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sport sport = (Sport) o;
        return Objects.equals(name, sport.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
