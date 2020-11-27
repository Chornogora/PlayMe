package com.dataart.playme.controller;

import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.model.User;
import com.dataart.playme.service.AuthorizationService;
import com.dataart.playme.service.UserService;
import com.dataart.playme.util.Constants;
import com.dataart.playme.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/auth")
public class AuthorizationServlet extends ThymeleafServlet {

    private static final String LOGIN_PAGE_PATH = "/user/login.html";

    private static final String LOGIN_PARAMETER_NAME = "login";

    private static final String PASSWORD_PARAMETER_NAME = "password";

    private static final String INVALID_LOGIN_OR_PASSWORD_MARK = "invalid_credentials";

    private static final String INVALID_STATUS_MARK = "invalid_status";

    private static final String STATUS_SESSION_ATTRIBUTE = "status";

    private AuthorizationService authorizationService;

    private UserService userService;

    @Override
    public void init() {
        super.init();
        ServletContext servletContext = getServletContext();
        authorizationService = (AuthorizationService) servletContext.getAttribute(AuthorizationService.class.getName());
        userService = (UserService) servletContext.getAttribute(UserService.class.getName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletContext servletContext = req.getServletContext();
        WebContext context = new WebContext(req, resp, servletContext);

        String status = (String) req.getSession().getAttribute(STATUS_SESSION_ATTRIBUTE);
        context.setVariable(status == null ? StringUtils.EMPTY : status, true);
        req.getSession().removeAttribute(status);

        templateEngine.process(LOGIN_PAGE_PATH, context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter(LOGIN_PARAMETER_NAME);
        String password = req.getParameter(PASSWORD_PARAMETER_NAME);

        try {
            User user = userService.getByLogin(login);
            AuthorizationService.AuthorizationStatus status = authorizationService.authorize(user, password);
            switch (status) {
                case WRONG_PASSWORD:
                    sendInvalid(req, resp, INVALID_LOGIN_OR_PASSWORD_MARK);
                    break;
                case ILLEGAL_STATUS:
                    sendInvalid(req, resp, INVALID_STATUS_MARK);
                    break;
                case SUCCESS:
                    SessionUtil.refreshSession(req.getSession());
                    redirectUser(req, resp, user);
            }
        } catch (NoSuchRecordException e) {
            sendInvalid(req, resp, INVALID_LOGIN_OR_PASSWORD_MARK);
        }
    }

    private void redirectUser(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        req.getSession().setAttribute(Constants.ROLE_ATTRIBUTE, user.getRole());
        String redirectLocation = isAdministrator(user) ? Constants.ADMIN_MAIN_PAGE : Constants.USER_MAIN_PAGE;
        resp.sendRedirect(redirectLocation);
    }

    private void sendInvalid(HttpServletRequest req, HttpServletResponse resp, String message) throws IOException {
        req.getSession().setAttribute(STATUS_SESSION_ATTRIBUTE, message);
        String redirectLocation = "/PlayMe/auth";
        resp.sendRedirect(redirectLocation);
    }

    private boolean isAdministrator(User user) {
        String adminRole = Constants.get(Constants.ADMIN_ROLE_ID);
        return adminRole.equals(user.getRole());
    }
}
