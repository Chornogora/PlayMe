package com.dataart.playme.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "skills_musicians")
@Data
@NoArgsConstructor
public class MusicianSkill {

    @EmbeddedId
    private MusicianSkillId id;

    @ManyToOne
    @JoinColumn(name = "skill_id", insertable = false, updatable = false)
    private Skill skill;

    @ManyToOne
    @JoinColumn(name = "skill_level_id", insertable = false, updatable = false)
    private SkillLevel skillLevel;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "musician_id", insertable = false, updatable = false)
    private Musician musician;

    @Embeddable
    private static class MusicianSkillId implements Serializable {

        @Column(name = "skill_id")
        private String skillId;

        @Column(name = "musician_id")
        private String musicianId;
    }
}
