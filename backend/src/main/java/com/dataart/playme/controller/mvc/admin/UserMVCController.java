package com.dataart.playme.controller.mvc.admin;

import com.dataart.playme.dto.EditUserDto;
import com.dataart.playme.dto.FilterBean;
import com.dataart.playme.dto.UserDto;
import com.dataart.playme.exception.NonUniqueException;
import com.dataart.playme.model.User;
import com.dataart.playme.service.UserService;
import com.dataart.playme.service.dto.UserDtoTransformationService;
import com.dataart.playme.util.Constants;
import com.dataart.playme.util.ErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/users")
public class UserMVCController {

    private static final String ALL_USERS_PATH = "/admin/users";

    private static final String ALL_USERS_FILEPATH = "/admin/main.html";

    private static final String ADD_USER_PATH = "/admin/users/add";

    private static final String ADD_USER_FILEPATH = "/admin/add-user.html";

    private static final String EDIT_USER_PATH_PATTERN = "/admin/users/edit/%s";

    private static final String EDIT_USER_FILEPATH = "/admin/edit-user.html";

    private static final String FILTER_BEAN_MODEL_ATTRIBUTE = "filterBean";

    private static final String USERS_MODEL_ATTRIBUTE = "users";

    private static final String USERS_COUNT_MODEL_ATTRIBUTE = "usersCount";

    private static final String ENTERED_DATA_MODEL_ATTRIBUTE = "enteredData";

    private static final String VALIDATION_ISSUES_MODEL_ATTRIBUTE = "validationIssues";

    private static final String USER_MODEL_ATTRIBUTE = "user";

    private final UserService userService;

    private final UserDtoTransformationService userDtoTransformationService;

    @Autowired
    public UserMVCController(UserService userService, UserDtoTransformationService userDtoTransformationService) {
        this.userService = userService;
        this.userDtoTransformationService = userDtoTransformationService;
    }

    @GetMapping
    public String getUsers(FilterBean filterBean, Model model) {
        List<User> users = userService.findFiltered(filterBean);
        List<UserDto> userData = users.stream()
                .map(userDtoTransformationService::userToDto)
                .collect(Collectors.toList());
        int usersCount = userService.getUsersCount(filterBean);

        model.addAttribute(USERS_MODEL_ATTRIBUTE, userData);
        model.addAttribute(FILTER_BEAN_MODEL_ATTRIBUTE, filterBean);
        model.addAttribute(USERS_COUNT_MODEL_ATTRIBUTE, usersCount);

        return ALL_USERS_FILEPATH;
    }

    @GetMapping("/add")
    public String getAddPage(Model model) {
        if (model.getAttribute(ENTERED_DATA_MODEL_ATTRIBUTE) == null) {
            model.addAttribute(ENTERED_DATA_MODEL_ATTRIBUTE, new UserDto());
        }

        if (model.getAttribute(VALIDATION_ISSUES_MODEL_ATTRIBUTE) == null) {
            model.addAttribute(VALIDATION_ISSUES_MODEL_ATTRIBUTE, Collections.emptyList());
        }

        return ADD_USER_FILEPATH;
    }

    @GetMapping("/edit/{userId}")
    public String getEditPage(@PathVariable String userId, Model model) {
        User originalUser = userService.getById(userId);
        model.addAttribute(USER_MODEL_ATTRIBUTE, originalUser);

        if (model.getAttribute(VALIDATION_ISSUES_MODEL_ATTRIBUTE) == null) {
            model.addAttribute(VALIDATION_ISSUES_MODEL_ATTRIBUTE, Collections.emptyList());
        }

        return EDIT_USER_FILEPATH;
    }

    @PostMapping("/add")
    public RedirectView addUser(@Valid UserDto dto, BindingResult bindingResult, RedirectAttributes redirect) {
        List<String> errors = ErrorUtil.getErrors(bindingResult);
        if (!errors.isEmpty()) {
            dto.setPassword(null);
            redirect.addFlashAttribute(ENTERED_DATA_MODEL_ATTRIBUTE, dto)
                    .addFlashAttribute(VALIDATION_ISSUES_MODEL_ATTRIBUTE, errors);
            return new RedirectView(ADD_USER_PATH);
        }
        User user = userDtoTransformationService.dtoToUser(dto);
        userService.addUser(user);
        redirect.addFlashAttribute(Constants.OPERATION_STATUS_CONTEXT_PARAMETER,
                String.valueOf(Response.Status.OK.getStatusCode()));
        return new RedirectView(ALL_USERS_PATH);
    }

    @PostMapping("/edit/{userId}")
    public RedirectView editUser(@PathVariable String userId, @Valid EditUserDto changes,
                                 BindingResult bindingResult, RedirectAttributes redirect) {
        List<String> errors = ErrorUtil.getErrors(bindingResult);
        if (!errors.isEmpty()) {
            redirect.addFlashAttribute(VALIDATION_ISSUES_MODEL_ATTRIBUTE, errors);
            String redirectPath = String.format(EDIT_USER_PATH_PATTERN, userId);
            return new RedirectView(redirectPath);
        }

        try {
            userService.updateUser(userId, changes);
        } catch (NonUniqueException e) {
            redirect.addFlashAttribute(VALIDATION_ISSUES_MODEL_ATTRIBUTE,
                    Collections.singletonList("user_exists"));
            String redirectPath = String.format(EDIT_USER_PATH_PATTERN, userId);
            return new RedirectView(redirectPath);
        }

        redirect.addFlashAttribute(Constants.OPERATION_STATUS_CONTEXT_PARAMETER,
                String.valueOf(Response.Status.OK.getStatusCode()));
        return new RedirectView(ALL_USERS_PATH);
    }

    @DeleteMapping("/delete/{userId}")
    public RedirectView deleteUser(@PathVariable String userId, RedirectAttributes redirect) {
        User user = userService.getById(userId);
        userService.deleteUser(user);
        redirect.addFlashAttribute(Constants.OPERATION_STATUS_CONTEXT_PARAMETER,
                String.valueOf(Response.Status.OK.getStatusCode()));
        return new RedirectView(ALL_USERS_PATH);
    }
}
