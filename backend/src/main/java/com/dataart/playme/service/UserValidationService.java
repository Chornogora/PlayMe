package com.dataart.playme.service;

import com.dataart.playme.model.User;

import java.util.List;

public interface UserValidationService {

    /**
     * method that is used to validate users
     *
     * @param user object to validate
     * @return list of issues found
     */
    List<String> validate(User user);
}
