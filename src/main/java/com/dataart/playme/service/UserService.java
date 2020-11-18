package com.dataart.playme.service;

import com.dataart.playme.model.User;

public interface UserService {

    User getById(String id);

    User getByLogin(String login);

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(User user);

    void activateUser(User user);

    void disableUser(User user);
}
