package com.dataart.playme.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bands")
@Data
@NoArgsConstructor
public class Band {

    @Id
    private String id;

    private String name;

    private Date creationDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "band", fetch = FetchType.LAZY)
    private List<Membership> members;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private BandStatus bandStatus;
}
