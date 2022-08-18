package com.codefactorygroup.betting.domain;

import com.codefactorygroup.betting.exception.EntityIsAlreadyLinked;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Table(name = "market")
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "market_id")
    @Column(name = "selections")
    private List<Selection> selections = new ArrayList<>();

    public void addSelection(Selection selection) {
        boolean selectionIsContained = this.selections.contains(selection);
        if (selectionIsContained) {
            throw new EntityIsAlreadyLinked(String.format("This selection is already part of the market"));
        } else {
            this.selections.add(selection);
        }
    }

    public void setSelections(List<Selection> selections) {
        this.selections.addAll(selections);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Market market = (Market) o;
        return Objects.equals(id, market.id) && Objects.equals(name, market.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
