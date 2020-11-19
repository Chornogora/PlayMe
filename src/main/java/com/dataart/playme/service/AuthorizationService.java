package com.dataart.playme.service;

import com.dataart.playme.model.User;

public interface AuthorizationService {

    boolean authorize(User user, String password);
}
