package com.dataart.playme.service.impl;

import com.dataart.playme.model.User;
import com.dataart.playme.service.AuthorizationService;
import com.dataart.playme.util.Constants;
import com.dataart.playme.util.Encoder;

public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public AuthorizationStatus authorize(User user, String password) {
        String passwordHash = Encoder.encode(password);
        if (!passwordHash.equals(user.getPassword())) {
            return AuthorizationStatus.WRONG_PASSWORD;
        }

        String activeStatus = Constants.get(Constants.ACTIVE_STATUS_ID);
        if (!activeStatus.equals(user.getStatus())) {
            return AuthorizationStatus.ILLEGAL_STATUS;
        }

        return AuthorizationStatus.SUCCESS;
    }
}
