package com.dataart.playme.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "musicians_bands")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

    @EmbeddedId
    private MembershipId id;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @ManyToOne
    @JoinColumn(name = "musician_id", insertable = false, updatable = false)
    private Musician musician;

    //@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "band_id", insertable = false, updatable = false)
    private Band band;

    @ManyToOne
    @JoinColumn(name = "member_status_id")
    private MemberStatus status;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MembershipId implements Serializable {

        @Column(name = "musician_id")
        private String musicianId;

        @Column(name = "band_id")
        private String bandId;
    }
}
