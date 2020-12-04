package com.dataart.playme.service.dto.impl;

import com.dataart.playme.dto.UserDto;
import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.model.Role;
import com.dataart.playme.model.Status;
import com.dataart.playme.model.User;
import com.dataart.playme.repository.RoleRepository;
import com.dataart.playme.repository.StatusRepository;
import com.dataart.playme.service.dto.UserDtoTransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDtoTransformationServiceImpl implements UserDtoTransformationService {

    private final RoleRepository roleRepository;

    private final StatusRepository statusRepository;

    @Autowired
    public UserDtoTransformationServiceImpl(RoleRepository roleRepository, StatusRepository statusRepository) {
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public User dtoToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setLogin(userDto.getLogin());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setBirthdate(userDto.getBirthdate());
        user.setCreationDate(userDto.getCreationDate());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        Role role = roleRepository.findByName(userDto.getRole())
                .orElseThrow(()-> new NoSuchRecordException("Can't find role"));
        user.setRole(role);

        Status status = statusRepository.findByName(userDto.getStatus())
                .orElseThrow(()-> new NoSuchRecordException("Can't find status"));
        user.setStatus(status);
        return user;
    }

    @Override
    public UserDto userToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setEmail(user.getEmail());
        userDto.setBirthdate(user.getBirthdate());
        userDto.setCreationDate(user.getCreationDate());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setRole(user.getRole().getName());
        userDto.setStatus(user.getStatus().getName());
        return userDto;
    }
}

