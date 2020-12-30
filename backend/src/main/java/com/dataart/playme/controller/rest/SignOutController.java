package com.dataart.playme.controller.rest;

import com.dataart.playme.util.Constants;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/signout")
public class SignOutController {

    @PostMapping
    public RedirectView signOut(HttpServletResponse response) {
        String cookieName = Constants.Security.JWT_TOKEN_COOKIE_NAME;
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath(Constants.APPLICATION_PATH);
        response.addCookie(cookie);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(Constants.FRONTEND_AUTHORIZATION_PATH);
        return redirectView;
    }
}
