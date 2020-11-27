package com.dataart.playme.controller.user;

import com.dataart.playme.controller.ThymeleafServlet;
import com.dataart.playme.dto.UserDto;
import com.dataart.playme.util.Constants;
import com.dataart.playme.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public abstract class UserManipulationServlet extends ThymeleafServlet {

    protected static final String VALIDATION_ISSUES_ATTRIBUTE = "validationIssues";

    protected static final String ENTERED_DATA_ATTRIBUTE = "enteredData";

    private static final String LOGIN_PARAMETER = "login";

    private static final String PASSWORD_PARAMETER = "password";

    private static final String EMAIL_PARAMETER = "email";

    private static final String FIRST_NAME_PARAMETER = "first-name";

    private static final String LAST_NAME_PARAMETER = "last-name";

    private static final String BIRTHDATE_PARAMETER = "birthdate";

    private static final String ROLE_PARAMETER = "role";

    private static final String STATUS_PARAMETER = "status";

    protected List<String> retrieveIssues(HttpServletRequest req) {
        List<String> issues = (List<String>) req.getSession().getAttribute(VALIDATION_ISSUES_ATTRIBUTE);
        if (issues == null) {
            issues = Collections.emptyList();
        } else {
            req.getSession().setAttribute(VALIDATION_ISSUES_ATTRIBUTE, null);
        }
        return issues;
    }

    protected UserDto retrieveEnteredData(HttpServletRequest req) {
        UserDto dto = (UserDto) req.getSession().getAttribute(ENTERED_DATA_ATTRIBUTE);
        if (dto == null) {
            dto = new UserDto();
        } else {
            req.getSession().setAttribute(ENTERED_DATA_ATTRIBUTE, null);
        }
        return dto;
    }

    protected UserDto extractUserDto(HttpServletRequest req) {
        String login = req.getParameter(LOGIN_PARAMETER);
        String password = req.getParameter(PASSWORD_PARAMETER);
        String email = req.getParameter(EMAIL_PARAMETER);
        String firstName = req.getParameter(FIRST_NAME_PARAMETER);
        String lastName = req.getParameter(LAST_NAME_PARAMETER);
        Date birthdate = extractDate(req);
        String role = req.getParameter(ROLE_PARAMETER);
        String status = req.getParameter(STATUS_PARAMETER);
        return new UserDto(login, password, email, birthdate, firstName, lastName, role, status);
    }

    private Date extractDate(HttpServletRequest req) {
        String birthdateAsString = req.getParameter(BIRTHDATE_PARAMETER);
        return birthdateAsString == null || birthdateAsString.equals(StringUtils.EMPTY) ? null : DateUtil.getDateFromString(birthdateAsString, Constants.WEB_DATE_FORMAT);
    }
}
