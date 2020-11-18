package com.dataart.playme.service.impl;

import com.dataart.playme.exception.DatabaseOperationException;
import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.exception.StorageException;
import com.dataart.playme.exception.ValidationException;
import com.dataart.playme.model.User;
import com.dataart.playme.repository.JDBCUserRepository;
import com.dataart.playme.service.UserService;
import com.dataart.playme.service.UserValidationService;
import com.dataart.playme.util.Constants;
import com.dataart.playme.util.JDBCConnector;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final JDBCUserRepository userRepository;

    private final UserValidationService validationService;

    public UserServiceImpl(JDBCUserRepository userRepository, UserValidationService userValidationService) {
        this.userRepository = userRepository;
        validationService = userValidationService;
    }

    @Override
    public User getById(String id) {
        try (Connection connection = JDBCConnector.getConnection()) {
            return userRepository.getById(connection, id)
                    .orElseThrow(() -> new NoSuchRecordException("User was not found"));
        } catch (DatabaseOperationException e) {
            LOGGER.error("Can't get user");
            throw new StorageException("Can't get user", e);
        } catch (SQLException e) {
            LOGGER.error("Can't close connection properly");
            throw new InternalServerErrorException("Can't close connection", e);
        }
    }

    @Override
    public User getByLogin(String login) {
        try (Connection connection = JDBCConnector.getConnection()) {
            return userRepository.getByLogin(connection, login)
                    .orElseThrow(() -> new NoSuchRecordException("User was not found"));
        } catch (DatabaseOperationException e) {
            LOGGER.error("Can't get user");
            throw new StorageException("Can't get user", e);
        } catch (SQLException e) {
            LOGGER.error("Can't close connection properly");
            throw new InternalServerErrorException("Can't close connection", e);
        }
    }

    @Override
    public void addUser(User user) {
        try (Connection connection = JDBCConnector.getConnection()) {
            validateUser(user);
            userRepository.addUser(connection, user);
        } catch (DatabaseOperationException e) {
            LOGGER.error("Can't get user");
            throw new StorageException("Can't get user", e);
        } catch (SQLException e) {
            LOGGER.error("Can't close connection properly");
            throw new InternalServerErrorException("Can't close connection", e);
        }
    }

    @Override
    public void updateUser(User user) {
        try (Connection connection = JDBCConnector.getConnection()) {
            validateUser(user);
            userRepository.updateUser(connection, user);
        } catch (DatabaseOperationException e) {
            LOGGER.error("Can't get user");
            throw new StorageException("Can't get user", e);
        } catch (SQLException e) {
            LOGGER.error("Can't close connection properly");
            throw new InternalServerErrorException("Can't close connection", e);
        }
    }

    @Override
    public void deleteUser(User user) throws StorageException {
        String dateFormatted = getDateFormatted();
        user.setLogin(user.getLogin() + Constants.DELETED_USER_MARK + dateFormatted);
        String deletedStatus = Constants.get(Constants.DELETED_STATUS_ID);
        user.setStatus(deletedStatus);
        user.setPassword(StringUtils.EMPTY);
        updateUser(user);
    }

    @Override
    public void activateUser(User user) throws StorageException {
        String activeStatus = Constants.get(Constants.ACTIVE_STATUS_ID);
        user.setStatus(activeStatus);
        updateUser(user);
    }

    @Override
    public void disableUser(User user) throws StorageException {
        String disabledStatus = Constants.get(Constants.DISABLED_STATUS_ID);
        user.setStatus(disabledStatus);
        user.setPassword(StringUtils.EMPTY);
        updateUser(user);
    }

    private void validateUser(User user) {
        List<String> issues = validationService.validate(user);
        if (!issues.isEmpty()) {
            throw new ValidationException(issues);
        }
    }

    private String getDateFormatted() {
        java.util.Date now = new java.util.Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return dateFormat.format(now);
    }
}
