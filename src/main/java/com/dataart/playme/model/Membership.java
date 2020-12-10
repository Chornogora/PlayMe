package com.dataart.playme.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "musicians_bands")
@Data
@NoArgsConstructor
public class Membership {

    @EmbeddedId
    private MembershipId id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "musician_id", insertable = false, updatable = false)
    private Musician musician;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "band_id", insertable = false, updatable = false)
    private Band band;

    @ManyToOne
    @JoinColumn(name = "member_status_id")
    private MemberStatus status;

    @Embeddable
    private static class MembershipId implements Serializable {

        @Column(name = "musician_id")
        private String musicianId;

        @Column(name = "band_id")
        private String bandId;
    }
}
