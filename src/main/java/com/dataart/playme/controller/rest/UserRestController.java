package com.dataart.playme.controller.rest;

import com.dataart.playme.dto.FilterBean;
import com.dataart.playme.model.User;
import com.dataart.playme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users/rest")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        FilterBean filterBean = new FilterBean();
        filterBean.setLogin("ir");
        return userService.findFiltered(filterBean);
    }
}
