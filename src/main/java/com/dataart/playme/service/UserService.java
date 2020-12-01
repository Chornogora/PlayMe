package com.dataart.playme.service;

import com.dataart.playme.dto.FilterBean;
import com.dataart.playme.dto.UserDto;
import com.dataart.playme.model.User;

import java.util.List;

public interface UserService {

    User getById(String id);

    User getByLogin(String login);

    List<User> findFiltered(FilterBean filterBean);

    int getUsersCount(FilterBean filterBean);

    User addUser(User user);

    User updateUser(String userId, UserDto changes);

    User deleteUser(User user);

    User activateUser(User user);
}
