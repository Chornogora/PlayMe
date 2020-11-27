package com.dataart.playme.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class FilterBean {

    private String login;

    private String email;

    private String firstName;

    private String lastName;

    private Date birthdateFrom;

    private Date birthdateTo;

    private Date creationDateFrom;

    private Date creationDateTo;

    private String roles;

    private String statuses;

    private String sortingField;

    private String sortingType;

    private int offset;

    private int limit;
}
