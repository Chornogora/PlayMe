package com.dataart.playme.controller.user;

import com.dataart.playme.dto.UserDto;
import com.dataart.playme.exception.ValidationException;
import com.dataart.playme.model.User;
import com.dataart.playme.service.UserService;
import com.dataart.playme.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/admin/user/edit/*")
public class EditUserServlet extends UserManipulationServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditUserServlet.class);

    private static final String USER_EDITING_PATH_PATTERN = "/PlayMe/admin/user/edit/%s";

    private static final String EDIT_USER_PAGE_PATH = "/admin/edit-user.html";

    private static final String USER_CONTEXT_PARAMETER = "user";

    private UserService userService;

    @Override
    public void init() {
        super.init();
        String userServiceName = UserService.class.getName();
        userService = (UserService) getServletContext().getAttribute(userServiceName);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<String> issues = retrieveIssues(req);
        User user = getOriginalUser(req);

        ServletContext servletContext = req.getServletContext();
        WebContext context = new WebContext(req, resp, servletContext);
        context.setVariable(VALIDATION_ISSUES_ATTRIBUTE, issues);
        context.setVariable(USER_CONTEXT_PARAMETER, user);
        templateEngine.process(EDIT_USER_PAGE_PATH, context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto dto = extractUserDto(req);
        String userId = req.getPathInfo().substring(1);

        try {
            userService.updateUser(userId, dto);
        } catch (ValidationException e) {
            LOGGER.warn("Invalid user data", e);
            req.getSession().setAttribute(VALIDATION_ISSUES_ATTRIBUTE, e.getIssues());
            String redirectString = String.format(USER_EDITING_PATH_PATTERN, userId);
            resp.sendRedirect(redirectString);
            return;
        }
        req.getSession().setAttribute(Constants.OPERATION_STATUS_CONTEXT_PARAMETER,
                String.valueOf(Response.Status.OK.getStatusCode()));
        resp.sendRedirect(Constants.ADMIN_MAIN_PAGE);
    }

    private User getOriginalUser(HttpServletRequest req) {
        String userId = req.getPathInfo().substring(1);
        return userService.getById(userId);
    }
}
