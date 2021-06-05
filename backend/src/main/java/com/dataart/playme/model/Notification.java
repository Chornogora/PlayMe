package com.dataart.playme.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
public class Notification {

    @Id
    private String id;

    private String title;

    private String text;

    private boolean read = false;

    @ManyToOne
    @JoinColumn(name = "musician_id")
    @JsonBackReference
    private Musician recipient;
}
