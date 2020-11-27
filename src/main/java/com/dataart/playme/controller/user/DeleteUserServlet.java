package com.dataart.playme.controller.user;

import com.dataart.playme.model.User;
import com.dataart.playme.service.UserService;
import com.dataart.playme.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

@WebServlet(urlPatterns = "/admin/user/delete/*")
public class DeleteUserServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        String userServiceName = UserService.class.getName();
        this.userService = (UserService) getServletContext().getAttribute(userServiceName);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getPathInfo().substring(1);
        User user = userService.getById(userId);
        userService.deleteUser(user);
        req.getSession().setAttribute(Constants.OPERATION_STATUS_CONTEXT_PARAMETER,
                String.valueOf(Response.Status.OK.getStatusCode()));
    }
}
