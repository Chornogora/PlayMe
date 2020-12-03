package com.dataart.playme.controller.rest;

import com.dataart.playme.model.User;
import com.dataart.playme.security.jwt.JwtTokenProvider;
import com.dataart.playme.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/auth")
    public ResponseEntity<Map<String, String>> authorize(@RequestBody AuthenticationRequest request) {
        try {
            String login = request.getLogin();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, request.getPassword()));
            User user = userService.getByLogin(login);
            String token = jwtTokenProvider.createToken(login, user.getRole().getName());

            Map<String, String> model = new HashMap<>();
            model.put("login", login);
            model.put("role", user.getRole().getName());
            model.put("token", token);
            return new ResponseEntity<>(model, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
}
