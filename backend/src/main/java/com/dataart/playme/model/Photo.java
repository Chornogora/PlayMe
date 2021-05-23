package com.dataart.playme.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "photos")
@Data
@NoArgsConstructor
public class Photo {

    @Id
    private String id;

    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonBackReference
    private Band owner;

    @ManyToMany(mappedBy = "photos")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<Post> posts;
}
