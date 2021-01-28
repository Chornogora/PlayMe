package com.dataart.playme.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "metronomes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metronome {

    @Id
    private String id;

    private int tempo;

    private Date creationDatetime;

    @ManyToOne
    @JoinColumn(name = "rehearsal_id")
    @JsonBackReference
    private Rehearsal rehearsal;

    public String toString() {
        return "[" + getClass().getName() + "]";
    }
}
