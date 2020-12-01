package com.dataart.playme.service.impl;

import com.dataart.playme.dto.FilterBean;
import com.dataart.playme.dto.UserDto;
import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.model.User;
import com.dataart.playme.repository.UserRepository;
import com.dataart.playme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchRecordException("User was not found"));
    }

    @Override
    public User getByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new NoSuchRecordException("User was not found"));
    }

    @Override
    public List<User> findFiltered(FilterBean filterBean) {
        return userRepository.findFiltered(filterBean);
    }

    @Override
    public int getUsersCount(FilterBean filterBean) {
        return 0;
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String userId, UserDto changes) {
        return null;
    }

    @Override
    public User deleteUser(User user) {
        return null;
    }

    @Override
    public User activateUser(User user) {
        return null;
    }
}
