package com.dataart.playme.model;

import lombok.Data;

import java.util.Date;

@Data
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
