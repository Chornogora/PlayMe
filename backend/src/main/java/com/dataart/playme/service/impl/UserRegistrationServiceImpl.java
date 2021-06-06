package com.dataart.playme.service.impl;

import com.dataart.playme.dto.UserDto;
import com.dataart.playme.model.Role;
import com.dataart.playme.model.Status;
import com.dataart.playme.model.User;
import com.dataart.playme.service.EmailConfirmationService;
import com.dataart.playme.service.UserRegistrationService;
import com.dataart.playme.service.UserService;
import com.dataart.playme.service.dto.UserDtoTransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserDtoTransformationService userDtoTransformationService;

    private final UserService userService;

    private final EmailConfirmationService emailConfirmationService;

    @Autowired
    public UserRegistrationServiceImpl(UserDtoTransformationService userDtoTransformationService, UserService userService,
                                       EmailConfirmationService emailConfirmationService) {
        this.userDtoTransformationService = userDtoTransformationService;
        this.userService = userService;
        this.emailConfirmationService = emailConfirmationService;
    }

    @Override
    public User registerUser(UserDto dto) {
        dto.setRole(Role.RoleName.USER.getValue());
        dto.setStatus(Status.StatusName.PENDING.getValue());
        User user = userDtoTransformationService.dtoToUser(dto);
        return registerUser(user);
    }

    @Transactional
    public User registerUser(User user) {
        User createdUser = userService.addUser(user);
        emailConfirmationService.sendConfirmationMessage(createdUser);
        return createdUser;
    }
}
