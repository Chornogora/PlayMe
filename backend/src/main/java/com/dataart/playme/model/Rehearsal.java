package com.dataart.playme.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "rehearsals")
public class Rehearsal {

    @Id
    private String id;

    private Date startDatetime;

    private Date finishDatetime;

    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Musician creator;

    @ManyToMany
    @JoinTable(name = "rehearsals_musicians",
            joinColumns = @JoinColumn(name = "rehearsal_id"),
            inverseJoinColumns = @JoinColumn(name = "musician_id"))
    private List<Musician> members;
}
