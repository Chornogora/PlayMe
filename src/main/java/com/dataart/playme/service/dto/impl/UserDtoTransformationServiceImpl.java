package com.dataart.playme.service.dto.impl;

import com.dataart.playme.dto.UserDto;
import com.dataart.playme.model.User;
import com.dataart.playme.service.dto.UserDtoTransformationService;

public class UserDtoTransformationServiceImpl implements UserDtoTransformationService {

    @Override
    public User transform(UserDto userDto) {
        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setBirthdate(userDto.getBirthdate());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setRole(userDto.getRole());
        user.setStatus(userDto.getStatus());
        return user;
    }
}
