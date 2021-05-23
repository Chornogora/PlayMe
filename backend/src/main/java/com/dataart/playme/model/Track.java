package com.dataart.playme.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tracks")
@Data
@NoArgsConstructor
public class Track {

    @Id
    private String id;

    private String fileUrl;

    @JsonIgnoreProperties(value = {"user", "musicianSkills", "memberships"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Musician musician;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;
}
