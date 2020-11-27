package com.dataart.playme.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String id;

    private String login;

    private String password;

    private String email;

    private Date birthdate;

    private Date creationDate;

    private Date modificationDate;

    private String firstName;

    private String lastName;

    private String role;

    private String status;
}
