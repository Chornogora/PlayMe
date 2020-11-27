package com.dataart.playme.controller.user;

import com.dataart.playme.dto.UserDto;
import com.dataart.playme.exception.ValidationException;
import com.dataart.playme.model.User;
import com.dataart.playme.service.UserService;
import com.dataart.playme.service.dto.UserDtoTransformationService;
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

@WebServlet(urlPatterns = "/admin/user/add")
public class AddUserServlet extends UserManipulationServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddUserServlet.class);

    private static final String USER_ADDING_PATH = "/PlayMe/admin/user/add";

    private static final String USER_PAGE_PATH = "/admin/add-user.html";

    private UserService userService;

    private UserDtoTransformationService userDtoTransformationService;

    @Override
    public void init() {
        super.init();
        String userServiceName = UserService.class.getName();
        userService = (UserService) getServletContext().getAttribute(userServiceName);
        userDtoTransformationService = (UserDtoTransformationService) getServletContext()
                .getAttribute(UserDtoTransformationService.class.getName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<String> issues = retrieveIssues(req);
        UserDto enteredData = retrieveEnteredData(req);

        ServletContext servletContext = req.getServletContext();
        WebContext context = new WebContext(req, resp, servletContext);
        context.setVariable(VALIDATION_ISSUES_ATTRIBUTE, issues);
        context.setVariable(ENTERED_DATA_ATTRIBUTE, enteredData);
        templateEngine.process(USER_PAGE_PATH, context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto dto = extractUserDto(req);
        User user = userDtoTransformationService.transform(dto);
        try {
            userService.addUser(user);
        } catch (ValidationException e) {
            LOGGER.warn("Invalid user data", e);
            req.getSession().setAttribute(VALIDATION_ISSUES_ATTRIBUTE, e.getIssues());
            dto.setPassword(null);
            req.getSession().setAttribute(ENTERED_DATA_ATTRIBUTE, dto);
            resp.sendRedirect(USER_ADDING_PATH);
            return;
        }
        req.getSession().setAttribute(Constants.OPERATION_STATUS_CONTEXT_PARAMETER,
                String.valueOf(Response.Status.OK.getStatusCode()));
        resp.sendRedirect(Constants.ADMIN_MAIN_PAGE);
    }
}
