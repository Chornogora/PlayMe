package com.dataart.playme.service.impl;

import com.dataart.playme.model.User;
import com.dataart.playme.service.UserValidationService;
import com.dataart.playme.util.Constants;
import com.dataart.playme.util.DateUtil;

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
        if (login == null) {
            return Collections.singletonList("User without login");
        }
        List<String> loginIssues = new LinkedList<>();

        String constraintName = Constants.CONSTRAINTS.MAX_LOGIN_LENGTH.getValue();
        int maxLength = Integer.parseInt(Constants.get(constraintName));
        if (maxLength < login.length()) {
            loginIssues.add("Too large login");
        } else {
            constraintName = Constants.CONSTRAINTS.MIN_LOGIN_LENGTH.getValue();
            int minLength = Integer.parseInt(Constants.get(constraintName));
            if (minLength > login.length()) {
                loginIssues.add("Too short login");
            }
        }

        constraintName = Constants.CONSTRAINTS.LOGIN_PATTERN.getValue();
        String pattern = Constants.get(constraintName);
        if (!login.matches(pattern)) {
            loginIssues.add("Invalid symbols found in login, only alpha-numeric and .-_@ symbols are acceptable");
        }

        return loginIssues;
    }

    private List<String> getPasswordIssues(String password) {
        if (password == null) {
            return Collections.singletonList("User without password");
        }
        List<String> passwordIssues = new LinkedList<>();

        String constraintName = Constants.CONSTRAINTS.MAX_PASSWORD_LENGTH.getValue();
        int maxLength = Integer.parseInt(Constants.get(constraintName));
        if (maxLength < password.length()) {
            passwordIssues.add("Too large password");
        }

        constraintName = Constants.CONSTRAINTS.MIN_PASSWORD_LENGTH.getValue();
        int minLength = Integer.parseInt(Constants.get(constraintName));
        if (minLength > password.length()) {
            passwordIssues.add("Too short password");
        }

        return passwordIssues;
    }

    private List<String> getEmailIssues(String email) {
        if (email == null) {
            return Collections.singletonList("User without email");
        }
        List<String> emailIssues = new LinkedList<>();

        if (email.length() > MAX_EMAIL_LENGTH) {
            emailIssues.add("Too large email");
        }

        String constraintName = Constants.CONSTRAINTS.EMAIL_PATTERN.getValue();
        String pattern = Constants.get(constraintName);
        if (!email.matches(pattern)) {
            emailIssues.add("Wrong email format");
        }

        return emailIssues;
    }

    private List<String> getRoleIssues(String role) {
        if (role == null) {
            return Collections.singletonList("User without role");
        }
        List<String> roles = Constants.getByGroup(Constants.ROLE_GROUP_ID);
        return roles.contains(role) ? new LinkedList<>() : Collections.singletonList("Role doesn't exist");
    }

    private List<String> getStatusIssues(String status) {
        if (status == null) {
            return Collections.singletonList("User without status");
        }
        List<String> statuses = Constants.getByGroup(Constants.STATUS_GROUP_ID);
        return statuses.contains(status) ? new LinkedList<>() : Collections.singletonList("Status doesn't exist");
    }

    private List<String> getFirstNameIssues(String firstName) {
        if (firstName == null) {
            return Collections.singletonList("User without first name");
        }
        List<String> firstNameIssues = new LinkedList<>();

        int minLength = Integer.parseInt(Constants.get(Constants.CONSTRAINTS.MIN_NAME_LENGTH.getValue()));
        if (firstName.length() < minLength) {
            firstNameIssues.add("First name is too short");
        } else {
            int maxLength = Integer.parseInt(Constants.get(Constants.CONSTRAINTS.MAX_LOGIN_LENGTH.getValue()));
            if (firstName.length() > maxLength) {
                firstNameIssues.add("First name is too long");
            }
        }

        String namePatternId = Constants.CONSTRAINTS.NAME_PATTERN.getValue();
        String pattern = Constants.get(namePatternId);
        if (!firstName.matches(pattern)) {
            firstNameIssues.add("Invalid symbols found in first name, only alphabetical symbols and \"-\"are acceptable");
        }

        return firstNameIssues;
    }

    private List<String> getLastNameIssues(String lastName) {
        if (lastName == null) {
            return Collections.singletonList("User without last name");
        }
        List<String> lastNameIssues = new LinkedList<>();

        int minLength = Integer.parseInt(Constants.get(Constants.CONSTRAINTS.MIN_NAME_LENGTH.getValue()));
        if (lastName.length() < minLength) {
            lastNameIssues.add("Last name is too short");
        } else {
            int maxLength = Integer.parseInt(Constants.get(Constants.CONSTRAINTS.MAX_LOGIN_LENGTH.getValue()));
            if (lastName.length() > maxLength) {
                lastNameIssues.add("Last name is too long");
            }
        }

        String namePatternId = Constants.CONSTRAINTS.NAME_PATTERN.getValue();
        String pattern = Constants.get(namePatternId);
        if (!lastName.matches(pattern)) {
            lastNameIssues.add("Invalid symbols found in last name, only alphabetical symbols and \"-\"are acceptable");
        }

        return lastNameIssues;
    }

    private List<String> getBirthdateIssues(Date birthdate) {
        if (birthdate == null) {
            return Collections.singletonList("User without birthday");
        }

        String minDateAcceptedId = Constants.CONSTRAINTS.MIN_BIRTHDATE.getValue();
        String minDateAsString = Constants.get(minDateAcceptedId);
        Date minDate = DateUtil.getDateFromString(minDateAsString);

        if (minDate.compareTo(birthdate) < 0) {
            return Collections.singletonList("Too late birthdate");
        }
        return Collections.emptyList();
    }
}
