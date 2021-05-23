package com.dataart.playme.controller.rest;

import com.dataart.playme.dto.CaptchaDto;
import com.dataart.playme.dto.UserRegistrationDto;
import com.dataart.playme.model.User;
import com.dataart.playme.service.CaptchaService;
import com.dataart.playme.service.UserRegistrationService;
import com.dataart.playme.util.ErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    private final UserRegistrationService registrationService;

    private final CaptchaService captchaService;

    @Autowired
    public SignUpController(UserRegistrationService registrationService, CaptchaService captchaService) {
        this.registrationService = registrationService;
        this.captchaService = captchaService;
    }

    @GetMapping("/captcha")
    public CaptchaDto getCaptcha() {
        return captchaService.generateCaptcha();
    }

    @PostMapping()
    public User createUser(@Valid @RequestBody UserRegistrationDto dto, BindingResult bindingResult) {
        List<String> errors = ErrorUtil.getErrors(bindingResult);
        if (!errors.isEmpty()) {
            sendError(errors);
        }
        if (!captchaService.checkCaptcha(dto.getCaptchaTokenId(), dto.getCaptchaNumber())){
            sendError(Collections.singletonList("wrong_captcha"));
        }
        return registrationService.registerUser(dto);
    }

    private void sendError(List<String> errors){
        Response response = Response
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(errors)
                .build();
        throw new BadRequestException(response);
    }
}
