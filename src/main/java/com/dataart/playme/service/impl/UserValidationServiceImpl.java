package com.dataart.playme.service.impl;

import com.dataart.playme.model.User;
import com.dataart.playme.service.UserValidationService;
import com.dataart.playme.util.Constants;
import com.dataart.playme.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class UserValidationServiceImpl implements UserValidationService {

    private static final int MAX_EMAIL_LENGTH = 320;

    @Override
    public List<String> validate(User user) {
        List<String> issues = new LinkedList<>();
        issues.addAll(getLoginIssues(user.getLogin()));
        issues.addAll(getPasswordIssues(user.getPassword()));
        issues.addAll(getEmailIssues(user.getEmail()));
        issues.addAll(getRoleIssues(user.getRole()));
        issues.addAll(getStatusIssues(user.getStatus()));
        issues.addAll(getFirstNameIssues(user.getFirstName()));
        issues.addAll(getLastNameIssues(user.getLastName()));
        issues.addAll(getBirthdateIssues(user.getBirthdate()));
        return issues;
    }

    private List<String> getLoginIssues(String login) {
        if (login == null || login.equals(StringUtils.EMPTY)) {
            return Collections.singletonList("no_login");
        }
        List<String> loginIssues = new LinkedList<>();

        String constraintName = Constants.Constraints.MAX_LOGIN_LENGTH.getValue();
        int maxLength = Integer.parseInt(Constants.get(constraintName));
        if (maxLength < login.length()) {
            loginIssues.add("long_login");
        } else {
            constraintName = Constants.Constraints.MIN_LOGIN_LENGTH.getValue();
            int minLength = Integer.parseInt(Constants.get(constraintName));
            if (minLength > login.length()) {
                loginIssues.add("short_login");
            }
        }

        constraintName = Constants.Constraints.LOGIN_PATTERN.getValue();
        String pattern = Constants.get(constraintName);
        if (!login.matches(pattern)) {
            loginIssues.add("wrong_login");
        }

        return loginIssues;
    }

    private List<String> getPasswordIssues(String password) {
        if (password == null || password.equals(StringUtils.EMPTY)) {
            return Collections.singletonList("no_password");
        }
        List<String> passwordIssues = new LinkedList<>();

        String constraintName = Constants.Constraints.MAX_PASSWORD_LENGTH.getValue();
        int maxLength = Integer.parseInt(Constants.get(constraintName));
        if (maxLength < password.length()) {
            passwordIssues.add("long_password");
        }

        constraintName = Constants.Constraints.MIN_PASSWORD_LENGTH.getValue();
        int minLength = Integer.parseInt(Constants.get(constraintName));
        if (minLength > password.length()) {
            passwordIssues.add("short_password");
        }

        return passwordIssues;
    }

    private List<String> getEmailIssues(String email) {
        if (email == null || email.equals(StringUtils.EMPTY)) {
            return Collections.singletonList("no_email");
        }
        List<String> emailIssues = new LinkedList<>();

        if (email.length() > MAX_EMAIL_LENGTH) {
            emailIssues.add("long_email");
        }

        String constraintName = Constants.Constraints.EMAIL_PATTERN.getValue();
        String pattern = Constants.get(constraintName);
        if (!email.matches(pattern)) {
            emailIssues.add("email_format");
        }

        return emailIssues;
    }

    private List<String> getRoleIssues(String role) {
        if (role == null || role.equals(StringUtils.EMPTY)) {
            return Collections.singletonList("no_role");
        }
        List<String> roles = Constants.getByGroup(Constants.ROLE_GROUP_ID);
        return roles.contains(role) ? new LinkedList<>() : Collections.singletonList("role_not_exist");
    }

    private List<String> getStatusIssues(String status) {
        if (status == null || status.equals(StringUtils.EMPTY)) {
            return Collections.singletonList("no_status");
        }
        List<String> statuses = Constants.getByGroup(Constants.STATUS_GROUP_ID);
        return statuses.contains(status) ? new LinkedList<>() : Collections.singletonList("status_not_exist");
    }

    private List<String> getFirstNameIssues(String firstName) {
        if (firstName == null) {
            return Collections.singletonList("no_name");
        }
        List<String> firstNameIssues = new LinkedList<>();

        int minLength = Integer.parseInt(Constants.get(Constants.Constraints.MIN_NAME_LENGTH.getValue()));
        if (firstName.length() < minLength) {
            firstNameIssues.add("short_name");
        } else {
            int maxLength = Integer.parseInt(Constants.get(Constants.Constraints.MAX_LOGIN_LENGTH.getValue()));
            if (firstName.length() > maxLength) {
                firstNameIssues.add("long_name");
            }
        }

        String namePatternId = Constants.Constraints.NAME_PATTERN.getValue();
        String pattern = Constants.get(namePatternId);
        if (!firstName.matches(pattern)) {
            firstNameIssues.add("wrong_name");
        }

        return firstNameIssues;
    }

    private List<String> getLastNameIssues(String lastName) {
        if (lastName == null || lastName.equals(StringUtils.EMPTY)) {
            return Collections.singletonList("no_surname");
        }
        List<String> lastNameIssues = new LinkedList<>();

        int minLength = Integer.parseInt(Constants.get(Constants.Constraints.MIN_NAME_LENGTH.getValue()));
        if (lastName.length() < minLength) {
            lastNameIssues.add("short_surname");
        } else {
            int maxLength = Integer.parseInt(Constants.get(Constants.Constraints.MAX_LOGIN_LENGTH.getValue()));
            if (lastName.length() > maxLength) {
                lastNameIssues.add("long_surname");
            }
        }

        String namePatternId = Constants.Constraints.NAME_PATTERN.getValue();
        String pattern = Constants.get(namePatternId);
        if (!lastName.matches(pattern)) {
            lastNameIssues.add("wrong_surname");
        }

        return lastNameIssues;
    }

    private List<String> getBirthdateIssues(Date birthdate) {
        if (birthdate == null) {
            return Collections.singletonList("no_birthdate");
        }

        String minDateAcceptedId = Constants.Constraints.MIN_BIRTHDATE.getValue();
        String minDateAsString = Constants.get(minDateAcceptedId);
        Date minDate = DateUtil.getDateFromString(minDateAsString);

        if (minDate.compareTo(birthdate) > 0) {
            return Collections.singletonList("early_birthdate");
        }

        String maxDateAcceptedId = Constants.Constraints.MAX_BIRTHDATE.getValue();
        String maxDateAsString = Constants.get(maxDateAcceptedId);
        Date maxDate = DateUtil.getDateFromString(maxDateAsString);

        if (maxDate.compareTo(birthdate) < 0) {
            return Collections.singletonList("late_birthdate");
        }
        return Collections.emptyList();
    }
}
