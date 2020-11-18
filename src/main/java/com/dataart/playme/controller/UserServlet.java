package com.dataart.playme.controller;

import com.dataart.playme.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/user")
public class UserServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServlet.class);

    private UserService userService;

    @Override
    public void init() {
        String userServiceName = UserService.class.getName();
        userService = (UserService) getServletContext().getAttribute(userServiceName);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LOGGER.info("userServlet#doGet executed");
        userService.getById("ert343");
        resp.getWriter().write("User Servlet");
    }
}
