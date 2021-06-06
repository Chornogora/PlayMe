package com.dataart.playme.service;

import com.dataart.playme.model.User;

public interface EmailConfirmationService {

    void sendConfirmationMessage(User createdUser);

    void confirmEmail(String token);
}
