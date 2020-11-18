package com.dataart.playme.repository;

import com.dataart.playme.model.User;

import java.sql.Connection;
import java.util.Optional;

public interface JDBCUserRepository {

    Optional<User> getById(Connection connection, String id);

    Optional<User> getByLogin(Connection connection, String login);

    void addUser(Connection connection, User user);

    void updateUser(Connection connection, User user);
}
