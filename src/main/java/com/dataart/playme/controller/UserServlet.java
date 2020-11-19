package com.dataart.playme.controller;

import com.dataart.playme.service.UserService;
import com.dataart.playme.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
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
    public void init() throws ServletException {
        super.init();
        String userServiceName = UserService.class.getName();
        userService = (UserService) getServletContext().getAttribute(userServiceName);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        SessionUtil.invalidateSession(req.getSession(), resp);
        resp.getWriter().write("userServlet#doGet()");
    }
}
