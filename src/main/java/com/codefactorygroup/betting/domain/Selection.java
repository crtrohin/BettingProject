package com.codefactorygroup.betting.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "selection")
@NoArgsConstructor
@Builder
@Getter
@Setter
@AllArgsConstructor
@Entity
public class Selection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "odds")
    private Integer odds;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Selection selection = (Selection) o;
        return Objects.equals(id, selection.id) && Objects.equals(name, selection.name) && Objects.equals(odds, selection.odds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, odds);
    }
}
