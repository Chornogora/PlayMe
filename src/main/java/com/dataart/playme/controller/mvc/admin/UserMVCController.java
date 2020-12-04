package com.dataart.playme.controller.mvc.admin;

import com.dataart.playme.dto.FilterBean;
import com.dataart.playme.dto.UserDto;
import com.dataart.playme.model.User;
import com.dataart.playme.service.UserService;
import com.dataart.playme.service.dto.UserDtoTransformationService;
import com.dataart.playme.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/users")
/*@SessionAttributes({UserMVCController.VALIDATION_ISSUES_MODEL_ATTRIBUTE,
        UserMVCController.ENTERED_DATA_MODEL_ATTRIBUTE,
        Constants.OPERATION_STATUS_CONTEXT_PARAMETER})*/
public class UserMVCController {

    private static final String ALL_USERS_PATH = "/admin/users";

    private static final String ALL_USERS_FILEPATH = "/admin/main.html";

    private static final String ADD_USER_PATH = "/admin/users/add";

    private static final String ADD_USER_FILEPATH = "/admin/add-user.html";

    private static final String FILTER_BEAN_MODEL_ATTRIBUTE = "filterBean";

    private static final String USERS_MODEL_ATTRIBUTE = "users";

    private static final String USERS_COUNT_MODEL_ATTRIBUTE = "usersCount";

    public static final String ENTERED_DATA_MODEL_ATTRIBUTE = "enteredData";

    public static final String VALIDATION_ISSUES_MODEL_ATTRIBUTE = "validationIssues";

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

    @PostMapping("/add")
    public ModelAndView addUser(@Valid UserDto dto, BindingResult bindingResult, ModelMap model) {
        List<String> errors = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        if (!errors.isEmpty()) {
            dto.setPassword(null);
            model.addAttribute(ENTERED_DATA_MODEL_ATTRIBUTE, dto);
            model.addAttribute(VALIDATION_ISSUES_MODEL_ATTRIBUTE, errors);
            return new ModelAndView(Constants.REDIRECT_PREFIX + ADD_USER_PATH, model);
        }
        User user = userDtoTransformationService.dtoToUser(dto);
        userService.addUser(user);
        model.addAttribute(Constants.OPERATION_STATUS_CONTEXT_PARAMETER,
                String.valueOf(Response.Status.OK.getStatusCode()));
        return new ModelAndView(Constants.REDIRECT_PREFIX + ALL_USERS_PATH, model);
    }
}
