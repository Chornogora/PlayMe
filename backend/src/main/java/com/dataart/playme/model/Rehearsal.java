package com.dataart.playme.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "rehearsal", fetch = FetchType.LAZY)
    private List<Metronome> metronomes;

    @JsonManagedReference
    @OneToOne(mappedBy = "rehearsal")
    private Record record;

    public List<Metronome> getMetronomes() {
        return metronomes.stream()
                .sorted(Comparator.comparing(Metronome::getCreationDatetime))
                .collect(Collectors.toList());
    }
}
