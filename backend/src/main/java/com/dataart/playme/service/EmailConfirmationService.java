package com.dataart.playme.service;

public interface EmailConfirmationService {

    void confirmEmail(String token);
}
