package com.dataart.playme.service.impl;

import com.dataart.playme.model.User;
import com.dataart.playme.service.AuthorizationService;
import com.dataart.playme.util.Encoder;

public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public boolean authorize(User user, String password) {
        String passwordHash = Encoder.encode(password);
        return passwordHash.equals(user.getPassword());
    }
}
