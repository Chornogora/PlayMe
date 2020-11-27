package com.dataart.playme.repository.impl;

import com.dataart.playme.dto.FilterBean;
import com.dataart.playme.exception.DatabaseOperationException;
import com.dataart.playme.model.User;
import com.dataart.playme.repository.JDBCUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JDBCUserRepositoryImpl implements JDBCUserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCUserRepositoryImpl.class);

    private static final String GET_BY_ID_QUERY = "SELECT users.*, roles.name AS role_name, statuses.name AS status_name " +
            "FROM users JOIN statuses ON users.status_id = statuses.id " +
            "JOIN roles ON users.role_id = roles.id " +
            "WHERE users.id = ?";

    private static final String GET_BY_LOGIN_QUERY = "SELECT users.*, roles.name AS role_name, statuses.name AS status_name " +
            "FROM users JOIN statuses ON users.status_id = statuses.id " +
            "JOIN roles ON users.role_id = roles.id " +
            "WHERE users.login = ?";

    private static final String FIND_BY_LOGIN_OR_EMAIL_QUERY = "SELECT users.*, roles.name AS role_name, statuses.name AS status_name " +
            "FROM users JOIN statuses ON users.status_id = statuses.id " +
            "JOIN roles ON users.role_id = roles.id " +
            "WHERE LOWER(users.login) = LOWER(?) OR users.email = ? LIMIT 1";

    private static final String FILTERED_SEARCH_QUERY_PATTERN = "SELECT users.*, roles.name AS role_name, statuses.name AS status_name " +
            "FROM users JOIN statuses ON users.status_id = statuses.id " +
            "JOIN roles ON users.role_id = roles.id " +
            "WHERE users.login LIKE CONCAT('%%', ?, '%%') " +
            "AND users.email LIKE CONCAT('%%', ?, '%%') " +
            "AND users.first_name LIKE CONCAT('%%', ?, '%%') " +
            "AND users.last_name LIKE CONCAT('%%', ?, '%%') " +
            "AND users.birthdate BETWEEN ? AND ? " +
            "AND users.creation_date BETWEEN ? AND ? " +
            "AND roles.name = ANY (string_to_array(?, ',')) " +
            "AND statuses.name = ANY (string_to_array(?, ',')) " +
            "ORDER BY %s %s " +
            "LIMIT ? OFFSET ?";

    private static final String USERS_COUNT_QUERY_PATTERN = "SELECT COUNT(*) " +
            "FROM users JOIN statuses ON users.status_id = statuses.id " +
            "JOIN roles ON users.role_id = roles.id " +
            "WHERE users.login LIKE CONCAT('%%', ?, '%%') " +
            "AND users.email LIKE CONCAT('%%', ?, '%%') " +
            "AND users.first_name LIKE CONCAT('%%', ?, '%%') " +
            "AND users.last_name LIKE CONCAT('%%', ?, '%%') " +
            "AND users.birthdate BETWEEN ? AND ? " +
            "AND users.creation_date BETWEEN ? AND ? " +
            "AND roles.name = ANY (string_to_array(?, ',')) " +
            "AND statuses.name = ANY (string_to_array(?, ','))";

    private static final String INSERT_QUERY = "INSERT INTO users VALUES(" +
            "?, ?, ?, ?, ?, ?, ?, " +
            "(SELECT id FROM roles WHERE name = ?), " +
            "(SELECT id FROM statuses WHERE name = ?), " +
            "?, null)";

    private static final String UPDATE_QUERY = "UPDATE users SET " +
            "login = ?, password = ?, email = ?, birthdate = ?, first_name = ?, last_name = ?, " +
            "role_id = (SELECT id FROM roles WHERE name = ?), " +
            "status_id = (SELECT id FROM statuses WHERE name = ?) " +
            "WHERE id = ?";

    private static final Map<String, String> SORTING_PARAMETERS = Map.of(
            "id", "users.id",
            "login", "users.login",
            "email", "users.email",
            "firstName", "users.first_name",
            "lastName", "users.last_name",
            "birthdate", "users.birthdate",
            "creationDate", "users.creation_date");

    final Map<String, String> SORTING_TYPES = Map.of(
            "ASC", "ASC",
            "DESC", "DESC");

    @Override
    public Optional<User> getById(Connection connection, String id) {
        try {
            PreparedStatement statement = connection.prepareStatement(GET_BY_ID_QUERY);
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();

            return (resultSet.next()) ? Optional.of(extractUser(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DatabaseOperationException("Can't get user from database", e);
        }
    }

    @Override
    public Optional<User> getByLogin(Connection connection, String login) {
        try {
            PreparedStatement statement = connection.prepareStatement(GET_BY_LOGIN_QUERY);
            statement.setString(1, login);

            ResultSet resultSet = statement.executeQuery();

            return (resultSet.next()) ? Optional.of(extractUser(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DatabaseOperationException("Can't get user from database", e);
        }
    }

    @Override
    public Optional<User> findByLoginOrEmail(Connection connection, String login, String email) {
        try {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_LOGIN_OR_EMAIL_QUERY);
            statement.setString(1, login);
            statement.setString(2, email);

            ResultSet resultSet = statement.executeQuery();

            return (resultSet.next()) ? Optional.of(extractUser(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DatabaseOperationException("Can't get user from database", e);
        }
    }

    @Override
    public List<User> findFiltered(Connection connection, FilterBean filterBean) {
        try {
            String filteredSearchQuery = getFilteredSearchQuery(filterBean);
            PreparedStatement statement = connection.prepareStatement(filteredSearchQuery);

            statement.setString(1, filterBean.getLogin());
            statement.setString(2, filterBean.getEmail());
            statement.setString(3, filterBean.getFirstName());
            statement.setString(4, filterBean.getLastName());
            statement.setDate(5, new Date(filterBean.getBirthdateFrom().getTime()));
            statement.setDate(6, new Date(filterBean.getBirthdateTo().getTime()));
            statement.setDate(7, new Date(filterBean.getCreationDateFrom().getTime()));
            statement.setDate(8, new Date(filterBean.getCreationDateTo().getTime()));
            statement.setString(9, filterBean.getRoles());
            statement.setString(10, filterBean.getStatuses());
            statement.setInt(11, filterBean.getLimit());
            statement.setInt(12, filterBean.getOffset());

            ResultSet resultSet = statement.executeQuery();
            return extractUsers(resultSet);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DatabaseOperationException("Can't get user from database", e);
        }
    }

    @Override
    public int getUsersCount(Connection connection, FilterBean filterBean) {
        try {
            PreparedStatement statement = connection.prepareStatement(USERS_COUNT_QUERY_PATTERN);

            statement.setString(1, filterBean.getLogin());
            statement.setString(2, filterBean.getEmail());
            statement.setString(3, filterBean.getFirstName());
            statement.setString(4, filterBean.getLastName());
            statement.setDate(5, new Date(filterBean.getBirthdateFrom().getTime()));
            statement.setDate(6, new Date(filterBean.getBirthdateTo().getTime()));
            statement.setDate(7, new Date(filterBean.getCreationDateFrom().getTime()));
            statement.setDate(8, new Date(filterBean.getCreationDateTo().getTime()));
            statement.setString(9, filterBean.getRoles());
            statement.setString(10, filterBean.getStatuses());

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DatabaseOperationException("Can't get user from database", e);
        }
    }

    @Override
    public void addUser(Connection connection, User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_QUERY);
            statement.setString(1, user.getId());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getEmail());
            statement.setDate(5, new Date(user.getBirthdate().getTime()));
            statement.setString(6, user.getFirstName());
            statement.setString(7, user.getLastName());
            statement.setString(8, user.getRole());
            statement.setString(9, user.getStatus());
            statement.setDate(10, new Date(System.currentTimeMillis()));

            statement.execute();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DatabaseOperationException("Can't insert user into database", e);
        }
    }

    @Override
    public void updateUser(Connection connection, User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setDate(4, new Date(user.getBirthdate().getTime()));
            statement.setString(5, user.getFirstName());
            statement.setString(6, user.getLastName());
            statement.setString(7, user.getRole());
            statement.setString(8, user.getStatus());
            statement.setString(9, user.getId());

            statement.execute();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DatabaseOperationException("Can't update user", e);
        }
    }

    private List<User> extractUsers(ResultSet resultSet) throws SQLException {
        List<User> users = new LinkedList<>();
        while (resultSet.next()) {
            User user = extractUser(resultSet);
            users.add(user);
        }
        return users;
    }

    private User extractUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getString("id"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));
        user.setBirthdate(resultSet.getDate("birthdate"));
        user.setCreationDate(resultSet.getDate("creation_date"));
        user.setModificationDate(resultSet.getDate("modification_date"));
        user.setRole(resultSet.getString("role_name"));
        user.setStatus(resultSet.getString("status_name"));
        return user;
    }

    private String getFilteredSearchQuery(FilterBean filterBean) {
        String sortingField = SORTING_PARAMETERS.get(filterBean.getSortingField());
        String sortingType = SORTING_TYPES.get(filterBean.getSortingType());
        return String.format(FILTERED_SEARCH_QUERY_PATTERN, sortingField, sortingType);
    }
}
