package com.dataart.playme.controller.mvc;

import com.dataart.playme.service.EmailConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class EmailConfirmationController {

    private static final String REDIRECT_ADDRESS = "http://localhost:4200/email_confirmed";

    private final EmailConfirmationService emailConfirmationService;

    @Autowired
    public EmailConfirmationController(EmailConfirmationService emailConfirmationService) {
        this.emailConfirmationService = emailConfirmationService;
    }

    @GetMapping("/email/confirmation/{token}")
    public RedirectView confirmEmail(@PathVariable String token) {
        emailConfirmationService.confirmEmail(token);
        return new RedirectView(REDIRECT_ADDRESS);
    }
}
