package com.codefactorygroup.betting.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// the central domain concept in my betting application
// we are defining here the entity class that represents a participant
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "participant")
@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;
}
