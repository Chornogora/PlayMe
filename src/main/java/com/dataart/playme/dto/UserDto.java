package com.dataart.playme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String login;

    private String password;

    private String email;

    private Date birthdate;

    private String firstName;

    private String lastName;

    private String role;

    private String status;
}
