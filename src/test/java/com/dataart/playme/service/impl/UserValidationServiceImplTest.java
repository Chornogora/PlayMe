package com.dataart.playme.service.impl;

import com.dataart.playme.model.User;
import com.dataart.playme.service.UserValidationService;
import com.dataart.playme.util.Constants;
import com.dataart.playme.util.DateUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class UserValidationServiceImplTest {

    private final UserValidationService validationService = new UserValidationServiceImpl();

    private static final String CONSTANTS_FILE = "./src/main/resources/constants.properties";

    private User user;

    @Before
    public void setUp() {
        try (InputStream inputStream = new FileInputStream(CONSTANTS_FILE)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            Constants.setProperties(properties);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        user = getValidUser();
    }

    @Test
    public void shouldNotFindAnyIssues() {
        List<String> issues = validationService.validate(user);

        Assert.assertEquals(0, issues.size());
    }

    @Test
    public void shouldFindTwoLoginIssues() {
        user.setLogin("Test&!");

        List<String> issues = validationService.validate(user);

        Assert.assertEquals(2, issues.size());
    }

    @Test
    public void shouldFindOneIssueCasePasswordIsTooShort() {
        user.setPassword("Some");

        List<String> issues = validationService.validate(user);

        Assert.assertEquals(1, issues.size());
    }

    @Test
    public void shouldFindFourIssuesCauseUserEmpty() {
        User user = new User();

        List<String> issues = validationService.validate(user);

        Assert.assertEquals(8, issues.size());
    }

    @Test
    public void shouldFindOneIssueCauseEmailIsWrong() {
        user.setEmail("^*.test@example.com");

        List<String> issues = validationService.validate(user);

        Assert.assertEquals(1, issues.size());
    }

    @Test
    public void shouldFindOneIssueCauseRoleDoesNotExist() {
        user.setRole("invalid role");

        List<String> issues = validationService.validate(user);

        Assert.assertEquals(1, issues.size());
    }

    @Test
    public void shouldFindOneIssueCauseStatusDoesNotExist() {
        user.setStatus("invalid status");

        List<String> issues = validationService.validate(user);

        Assert.assertEquals(1, issues.size());
    }

    @Test
    public void shouldFindOneIssueCauseBirthdateIsTooLate() {
        user.setBirthdate(new Date(System.currentTimeMillis()));

        List<String> issues = validationService.validate(user);

        Assert.assertEquals(1, issues.size());
    }

    @Test
    public void shouldFindOneIssueCauseFirstNameIsTooShort() {
        user.setFirstName("I");

        List<String> issues = validationService.validate(user);

        Assert.assertEquals(1, issues.size());
    }

    @Test
    public void shouldFindOneIssueCauseLastNameIsTooShort() {
        user.setLastName("I");

        List<String> issues = validationService.validate(user);

        Assert.assertEquals(1, issues.size());
    }

    @Test
    public void shouldFindOneIssueCauseFirstNameContainsInvalidSymbols() {
        user.setLastName("Steve123");

        List<String> issues = validationService.validate(user);

        Assert.assertEquals(1, issues.size());
    }

    @Test
    public void shouldFindOneIssueCauseLastNameContainsInvalidSymbols() {
        user.setLastName("Sten_Lee");

        List<String> issues = validationService.validate(user);

        Assert.assertEquals(1, issues.size());
    }

    private User getValidUser() {
        User user = new User();
        user.setLogin("Somebody");
        user.setPassword("Somebody123");
        user.setEmail("somebody@example.com");
        user.setRole("administrator");
        //The 5th January 2000
        user.setBirthdate(DateUtil.getDateFromString("2000-01-05_00-00-00"));
        user.setStatus("active");
        user.setFirstName("Some");
        user.setLastName("Body");
        return user;
    }
}
