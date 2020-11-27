package com.dataart.playme.service.impl;

import com.dataart.playme.dto.FilterBean;
import com.dataart.playme.dto.UserDto;
import com.dataart.playme.exception.DatabaseOperationException;
import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.exception.StorageException;
import com.dataart.playme.exception.ValidationException;
import com.dataart.playme.model.User;
import com.dataart.playme.repository.JDBCUserRepository;
import com.dataart.playme.service.UserService;
import com.dataart.playme.service.UserValidationService;
import com.dataart.playme.util.Constants;
import com.dataart.playme.util.Encoder;
import com.dataart.playme.util.JDBCConnector;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public List<User> findFiltered(FilterBean filterBean) {
        try (Connection connection = JDBCConnector.getConnection()) {
            return userRepository.findFiltered(connection, filterBean);
        } catch (DatabaseOperationException e) {
            LOGGER.error("Can't get users");
            throw new StorageException("Can't get users", e);
        } catch (SQLException e) {
            LOGGER.error("Can't close connection properly");
            throw new InternalServerErrorException("Can't close connection", e);
        }
    }

    @Override
    public int getUsersCount(FilterBean filterBean) {
        try (Connection connection = JDBCConnector.getConnection()) {
            return userRepository.getUsersCount(connection, filterBean);
        } catch (DatabaseOperationException e) {
            LOGGER.error("Can't get users");
            throw new StorageException("Can't get users", e);
        } catch (SQLException e) {
            LOGGER.error("Can't close connection properly");
            throw new InternalServerErrorException("Can't close connection", e);
        }
    }

    @Override
    public void addUser(User user) {
        try (Connection connection = JDBCConnector.getConnection()) {
            validateUser(user);

            if (!exists(user)) {
                user.setId(generateId());
                user.setCreationDate(new Date(System.currentTimeMillis()));
                String passwordEncoded = Encoder.encode(user.getPassword());
                user.setPassword(passwordEncoded);
                userRepository.addUser(connection, user);
                return;
            }
            throw new ValidationException(Collections.singletonList("user_exists"));
        } catch (DatabaseOperationException e) {
            LOGGER.error("Can't get user");
            throw new StorageException("Can't get user", e);
        } catch (SQLException e) {
            LOGGER.error("Can't close connection properly");
            throw new InternalServerErrorException("Can't close connection", e);
        }
    }

    @Override
    public void updateUser(String userId, UserDto changes) {
        User original = getById(userId);
        User modified = modifyUser(original, changes);
        validateUser(modified);
        updateUser(modified);
    }

    @Override
    public void deleteUser(User user) throws StorageException {
        String deletedStatus = Constants.get(Constants.DELETED_STATUS_ID);
        if (!user.getStatus().equals(deletedStatus)) {
            String dateFormatted = getDateFormatted();
            user.setLogin(user.getLogin() + Constants.DELETED_USER_MARK + dateFormatted);
            user.setStatus(deletedStatus);
            user.setPassword(StringUtils.EMPTY);
            updateUser(user);
        }
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

    private boolean exists(User user) {
        try (Connection connection = JDBCConnector.getConnection()) {
            Optional<User> found = userRepository.findByLoginOrEmail(connection, user.getLogin(), user.getEmail());
            return found.isPresent();
        } catch (DatabaseOperationException e) {
            LOGGER.error("Can't get user");
            throw new StorageException("Can't get user", e);
        } catch (SQLException e) {
            LOGGER.error("Can't close connection properly");
            throw new InternalServerErrorException("Can't close connection", e);
        }
    }

    private void updateUser(User user) {
        try (Connection connection = JDBCConnector.getConnection()) {
            userRepository.updateUser(connection, user);
        } catch (DatabaseOperationException e) {
            LOGGER.error("Can't update user");
            throw new StorageException("Can't update user", e);
        } catch (SQLException e) {
            LOGGER.error("Can't close connection properly");
            throw new InternalServerErrorException("Can't close connection", e);
        }
    }

    private User modifyUser(User original, UserDto dto) {
        if (!dto.getEmail().equals(original.getEmail())) {
            dto.setStatus(Constants.get(Constants.PENDING_STATUS_ID));
        }

        return new User(
                original.getId(),
                original.getLogin(),
                original.getPassword(),
                dto.getEmail(),
                dto.getBirthdate(),
                original.getCreationDate(),
                original.getModificationDate(),
                dto.getFirstName(),
                dto.getLastName(),
                original.getRole(),
                dto.getStatus()
        );
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

    private String generateId() {
        return UUID.randomUUID().toString();
    }
}
