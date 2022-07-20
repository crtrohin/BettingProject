package com.codefactorygroup.betting.domain;

import lombok.*;

import javax.persistence.*;

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
}
