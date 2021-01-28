package com.dataart.playme.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "musicians")
@Data
@NoArgsConstructor
public class Musician {

    @Id
    private String id;

    private String nickname;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonManagedReference
    @OneToMany(mappedBy = "musician", fetch = FetchType.LAZY)
    private List<MusicianSkill> musicianSkills;

    @OneToMany(mappedBy = "musician", fetch = FetchType.LAZY)
    private List<Membership> memberships;

    public String toString() {
        return "[" + getClass().getName() + "]";
    }
}
