package com.dataart.playme.repository;

import com.dataart.playme.dto.FilterBean;
import com.dataart.playme.model.User;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface JDBCUserRepository {

    Optional<User> getById(Connection connection, String id);

    Optional<User> getByLogin(Connection connection, String login);

    Optional<User> findByLoginOrEmail(Connection connection, String login, String email);

    List<User> findFiltered(Connection connection, FilterBean filterBean);

    int getUsersCount(Connection connection, FilterBean filterBean);

    void addUser(Connection connection, User user);

    void updateUser(Connection connection, User user);
}
