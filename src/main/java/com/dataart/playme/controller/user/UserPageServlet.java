package com.dataart.playme.controller.user;

import com.dataart.playme.controller.ThymeleafServlet;
import com.dataart.playme.dto.FilterBean;
import com.dataart.playme.model.User;
import com.dataart.playme.service.UserService;
import com.dataart.playme.util.Constants;
import com.dataart.playme.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/admin/users")
public class UserPageServlet extends ThymeleafServlet {

    private static final String ADMIN_PAGE_PATH = "/admin/main.html";

    private static final String LOGIN_FILTER_PARAMETER = "login-filter";

    private static final String EMAIL_FILTER_PARAMETER = "email-filter";

    private static final String FIRST_NAME_FILTER_PARAMETER = "first-name-filter";

    private static final String LAST_NAME_FILTER_PARAMETER = "last-name-filter";

    private static final String BIRTHDATE_FROM_FILTER_PARAMETER = "birthdate-from-filter";

    private static final String BIRTHDATE_TO_FILTER_PARAMETER = "birthdate-to-filter";

    private static final String CREATION_DATE_FROM_FILTER_PARAMETER = "creation-date-from-filter";

    private static final String CREATION_DATE_TO_FILTER_PARAMETER = "creation-date-to-filter";

    private static final String ROLE_FILTER_PARAMETER = "role-filter";

    private static final String STATUS_FILTER_PARAMETER = "status-filter";

    private static final String SORTING_FIELD_PARAMETER = "sorting-field";

    private static final String SORTING_TYPE_PARAMETER = "sorting-type";

    private static final String LIMIT_PARAMETER = "limit";

    private static final String OFFSET_PARAMETER = "offset";

    private static final int DEFAULT_LIMIT = 10;

    private static final int DEFAULT_OFFSET = 0;

    private static final String DEFAULT_SORTING_FIELD = "id";

    private static final String DEFAULT_SORTING_TYPE = "ASC";

    private static final String USERS_CONTEXT_PARAMETER = "users";

    private static final String FILTER_BEAN_CONTEXT_PARAMETER = "filterBean";

    private static final String USERS_COUNT_CONTEXT_PARAMETER = "usersCount";

    private static final String MULTIPLE_VALUES_PARAMETER_DELIMITER = ",";

    private UserService userService;

    @Override
    public void init() {
        super.init();
        ServletContext servletContext = getServletContext();
        userService = (UserService) servletContext.getAttribute(UserService.class.getName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        FilterBean filterBean = extractFilterBean(req);
        List<User> users = userService.findFiltered(filterBean).stream()
                .peek(user -> user.setPassword(StringUtils.EMPTY))
                .collect(Collectors.toList());
        int usersCount = userService.getUsersCount(filterBean);
        String operationStatus = (String) req.getSession().getAttribute(Constants.OPERATION_STATUS_CONTEXT_PARAMETER);
        req.getSession().removeAttribute(Constants.OPERATION_STATUS_CONTEXT_PARAMETER);

        ServletContext servletContext = req.getServletContext();
        WebContext context = new WebContext(req, resp, servletContext);
        context.setVariable(FILTER_BEAN_CONTEXT_PARAMETER, filterBean);
        context.setVariable(USERS_CONTEXT_PARAMETER, users);
        context.setVariable(USERS_COUNT_CONTEXT_PARAMETER, usersCount);
        context.setVariable(Constants.OPERATION_STATUS_CONTEXT_PARAMETER, operationStatus);
        templateEngine.process(ADMIN_PAGE_PATH, context, resp.getWriter());
    }

    private FilterBean extractFilterBean(HttpServletRequest request) {
        FilterBean filterBean = new FilterBean();

        String login = request.getParameter(LOGIN_FILTER_PARAMETER);
        filterBean.setLogin(login == null ? StringUtils.EMPTY : login);
        String email = request.getParameter(EMAIL_FILTER_PARAMETER);
        filterBean.setEmail(email == null ? StringUtils.EMPTY : email);
        String firstName = request.getParameter(FIRST_NAME_FILTER_PARAMETER);
        filterBean.setFirstName(firstName == null ? StringUtils.EMPTY : firstName);
        String lastName = request.getParameter(LAST_NAME_FILTER_PARAMETER);
        filterBean.setLastName(lastName == null ? StringUtils.EMPTY : lastName);

        String rolesAsString = request.getParameter(ROLE_FILTER_PARAMETER);
        filterBean.setRoles(rolesAsString == null
                ? String.join(MULTIPLE_VALUES_PARAMETER_DELIMITER, Constants.getByGroup(Constants.ROLE_GROUP_ID))
                : rolesAsString);
        String statusesAsString = request.getParameter(STATUS_FILTER_PARAMETER);
        filterBean.setStatuses(statusesAsString == null
                ? String.join(MULTIPLE_VALUES_PARAMETER_DELIMITER, Constants.getByGroup(Constants.STATUS_GROUP_ID))
                : statusesAsString);

        String defaultMinbirthdateConstantName = Constants.Constraints.MIN_BIRTHDATE.getValue();
        Date birthdateFrom = extractDate(request, BIRTHDATE_FROM_FILTER_PARAMETER,
                getDateFromConstant(defaultMinbirthdateConstantName));
        filterBean.setBirthdateFrom(birthdateFrom);

        String defaultMaxBirthdateConstantName = Constants.Constraints.MAX_BIRTHDATE.getValue();
        Date birthdateTo = extractDate(request, BIRTHDATE_TO_FILTER_PARAMETER,
                getDateFromConstant(defaultMaxBirthdateConstantName));
        filterBean.setBirthdateTo(birthdateTo);

        String defaultMinCreationDateConstantName = Constants.Constraints.MIN_CREATION_DATE.getValue();
        Date creationDateFrom = extractDate(request, CREATION_DATE_FROM_FILTER_PARAMETER,
                getDateFromConstant(defaultMinCreationDateConstantName));
        filterBean.setCreationDateFrom(creationDateFrom);

        Date creationDateTo = extractDate(request, CREATION_DATE_TO_FILTER_PARAMETER,
                new Date(System.currentTimeMillis()));
        filterBean.setCreationDateTo(creationDateTo);

        String limitAsString = request.getParameter(LIMIT_PARAMETER);
        filterBean.setLimit(limitAsString == null ? DEFAULT_LIMIT : Integer.parseInt(limitAsString));
        String offsetAsString = request.getParameter(OFFSET_PARAMETER);
        filterBean.setOffset(offsetAsString == null ? DEFAULT_OFFSET : Integer.parseInt(offsetAsString));

        String sortingField = request.getParameter(SORTING_FIELD_PARAMETER);
        filterBean.setSortingField(sortingField == null || sortingField.equals(StringUtils.EMPTY)
                ? DEFAULT_SORTING_FIELD : sortingField);
        String sortingType = request.getParameter(SORTING_TYPE_PARAMETER);
        filterBean.setSortingType(sortingType == null || sortingType.equals(StringUtils.EMPTY)
                ? DEFAULT_SORTING_TYPE : sortingType);

        return filterBean;
    }

    private Date extractDate(HttpServletRequest req, String parameterName, Date defaultDate) {
        String dateAsString = req.getParameter(parameterName);
        if (dateAsString == null || dateAsString.equals(StringUtils.EMPTY)) {
            return defaultDate;
        }
        return DateUtil.getDateFromString(dateAsString, Constants.WEB_DATE_FORMAT);
    }

    private Date getDateFromConstant(String constantName) {
        String constantDate = Constants.get(constantName);
        return DateUtil.getDateFromString(constantDate);
    }
}
