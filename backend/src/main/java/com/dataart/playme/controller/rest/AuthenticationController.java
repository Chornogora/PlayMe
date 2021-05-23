package com.dataart.playme.controller.rest;

import com.dataart.playme.model.User;
import com.dataart.playme.security.jwt.JwtTokenProvider;
import com.dataart.playme.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/auth")
    public ResponseEntity<Map<String, String>> authorize(@RequestBody AuthenticationRequest request,
                                                         HttpServletResponse response) {
        try {
            String login = request.getLogin();
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, request.getPassword()));
            User user = (User) authentication.getPrincipal();
            String token = jwtTokenProvider.createToken(login, user.getRole().getName());

            Map<String, String> model = new HashMap<>();
            model.put("login", login);
            model.put("role", user.getRole().getName());
            model.put("token", token);

            setTokenCookie(token, response);

            return new ResponseEntity<>(model, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied or account is locked");
        }
    }

    private void setTokenCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie(Constants.Security.JWT_TOKEN_COOKIE_NAME, token);
        cookie.setPath(Constants.APPLICATION_PATH);
        int expirationTimeSeconds = Integer.parseInt(
                Constants.get(Constants.Security.SESSION_LIFETIME)) / 1000;
        cookie.setMaxAge(expirationTimeSeconds);
        response.addCookie(cookie);
    }
}
