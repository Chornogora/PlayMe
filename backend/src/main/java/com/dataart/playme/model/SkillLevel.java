package com.dataart.playme.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "skill_levels")
@Data
@NoArgsConstructor
public class SkillLevel {

    @Id
    private String id;

    private String name;
}
