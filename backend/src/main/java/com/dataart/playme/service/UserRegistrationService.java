package com.dataart.playme.service;

import com.dataart.playme.dto.UserDto;
import com.dataart.playme.model.User;

public interface UserRegistrationService {

    User registerUser(UserDto dto);

    User registerUser(User user);
}
