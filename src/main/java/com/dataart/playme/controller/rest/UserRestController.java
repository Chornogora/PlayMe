package com.dataart.playme.controller.rest;

import com.dataart.playme.model.Role;
import com.dataart.playme.model.Status;
import com.dataart.playme.model.User;
import com.dataart.playme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public User getUser(){
        User user = new User();
        user.setLogin("TestUser");
        user.setRole(new Role("123", "user"));
        user.setStatus(new Status("123", "active"));
        return user;
    }
}
