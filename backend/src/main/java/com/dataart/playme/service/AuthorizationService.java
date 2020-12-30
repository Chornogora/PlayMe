package com.dataart.playme.service;

import com.dataart.playme.model.User;

public interface AuthorizationService {

    AuthorizationStatus authorize(User user, String password);

    enum AuthorizationStatus {
        SUCCESS, WRONG_PASSWORD, ILLEGAL_STATUS
    }
}
