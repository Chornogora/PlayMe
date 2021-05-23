package com.dataart.playme.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "records")
@Data
@NoArgsConstructor
public class Record {

    @Id
    private String id;

    private Date startDatetime;

    private Date finishDatetime;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rehearsal_id")
    private Rehearsal rehearsal;

    @JsonManagedReference
    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY)
    private List<Track> tracks;
}
