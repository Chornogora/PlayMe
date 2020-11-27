package com.dataart.playme.controller;

import com.dataart.playme.util.Constants;
import com.dataart.playme.util.SessionUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/signout")
public class SignOutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        SessionUtil.invalidateSession(req.getSession(), resp);
        resp.sendRedirect(Constants.LOGIN_PAGE_PATH);
    }
}
