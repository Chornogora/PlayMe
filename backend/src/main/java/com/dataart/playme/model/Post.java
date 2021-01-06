package com.dataart.playme.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
public class Post {

    @Id
    private String id;

    @Column(name = "text")
    private String text;

    private Date creationDatetime;

    @ManyToOne
    @JoinColumn(name = "band_id")
    private Band band;

    @ManyToMany
    @JoinTable(name = "posts_files",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))
    private List<File> files;

    @ManyToMany
    @JoinTable(name = "posts_photos",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id"))
    private List<Photo> photos;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Musician creator;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post",
            cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> comments;
}
