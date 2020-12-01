package com.dataart.playme.repository;

import com.dataart.playme.dto.FilterBean;
import com.dataart.playme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByLogin(String login);

    Optional<User> findByLoginOrEmail(String login, String email);

    @Query(value = "SELECT u FROM User u WHERE u.login LIKE :#{#filterBean.login}")
    List<User> findFiltered(@Param("filterBean") FilterBean filterBean);

    @Query(value = "SELECT COUNT(u) FROM User u WHERE u.login LIKE :#{#filterBean.login}")
    int getUsersCount(FilterBean filterBean);
}
