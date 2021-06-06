package com.dataart.playme.controller.rest;

import com.dataart.playme.controller.binding.annotation.CurrentMusician;
import com.dataart.playme.controller.binding.annotation.CurrentUser;
import com.dataart.playme.dto.FilterBean;
import com.dataart.playme.dto.UpdateMusicianDto;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Role;
import com.dataart.playme.model.Status;
import com.dataart.playme.model.User;
import com.dataart.playme.service.MusicianService;
import com.dataart.playme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final UserService userService;

    private final MusicianService musicianService;

    @Autowired
    public UserController(UserService userService, MusicianService musicianService) {
        this.userService = userService;
        this.musicianService = musicianService;
    }

    @GetMapping("/me")
    public User currentUser(@CurrentUser User currentUser) {
        return currentUser;
    }

    @GetMapping("/me/musician")
    public Musician currentMusician(@CurrentMusician Musician musician) {
        return musician;
    }

    @GetMapping("/musicians")
    public List<Musician> getUsers(FilterBean filterBean) {
        filterBean.setRoles(Collections.singletonList(Role.RoleName.USER.getValue()));
        filterBean.setStatuses(Collections.singletonList(Status.StatusName.ACTIVE.getValue()));

        return userService.findFiltered(filterBean)
                .stream()
                .map(musicianService::findByUser)
                .collect(Collectors.toList());
    }

    @PatchMapping("/user")
    public Musician updateMusician(@RequestBody @Valid UpdateMusicianDto musician,
                                   @CurrentMusician Musician current) {
        return musicianService.updateMusician(musician, current);
    }
}
