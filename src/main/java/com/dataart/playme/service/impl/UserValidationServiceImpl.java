package com.dataart.playme.service.impl;

import com.dataart.playme.model.User;
import com.dataart.playme.service.UserValidationService;

import java.util.Collections;
import java.util.List;

public class UserValidationServiceImpl implements UserValidationService {

    @Override
    public List<String> validate(User user) {
        return Collections.emptyList();
    }
}
