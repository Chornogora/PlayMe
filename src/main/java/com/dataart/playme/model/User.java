package com.dataart.playme.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@SecondaryTables({
        @SecondaryTable( name = "roles", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id")),
        @SecondaryTable( name = "statuses", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))})
public class User {

    @Id
    private String id;

    private String login;

    private String password;

    private String email;

    private Date birthdate;

    private Date creationDate;

    private Date modificationDate;

    private String firstName;

    private String lastName;

    @Column(name = "name", table = "roles")
    private String role;

    @Column(name = "name", table = "statuses")
    private String status;
}
