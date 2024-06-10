package com.crud.dao.impl;

import com.crud.dao.BaseDao;
import com.crud.dataSource.ConnectionManager;
import com.crud.dto.CreateUserDto;
import com.crud.entity.Role;
import com.crud.entity.User;
import com.crud.exceptions.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class UserDao implements BaseDao<User, Long> {
    private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());

    private static final String UPDATE = "UPDATE users SET name = ?, registration_date = ?, login = ?, role_id = ? WHERE id = ?;";

    private static final String DELETE = "DELETE FROM users WHERE id = ?;";
    public static final String INSERT = "INSERT INTO users (name, registration_date, login) VALUES (?, ?, ?);";
    public static final String  FIND_BY_ID = "SELECT id, name, registration_date, login, role_id FROM users WHERE id = ?;";
    public static final String FIND_ALL = "SELECT id, name, registration_date, login, role_id FROM users;";

    private static final String GET_BY_LOGIN = "Select id, name, registration_date, login from users where login = ?;";
    public static final AtomicReference<UserDao> USER_DAO = new AtomicReference<>();

    public static UserDao getInstance() {
        UserDao current = USER_DAO.get();
        if (current == null) {
            UserDao newUserDao = new UserDao();
            if (USER_DAO.compareAndSet(null, newUserDao)) {
                return newUserDao;
            } else {
                return USER_DAO.get();
            }
        }
        return current;
    }

    @Override
    public User update(User entity) {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE users SET ");
        List<Object> params = new ArrayList<>();
        List<String> updateClauses = new ArrayList<>();

        if (entity.getName() != null) {
            updateClauses.add("name = ?");
            params.add(entity.getName());
        }
        if (entity.getRegistrationDate() != null) {
            updateClauses.add("registration_date = ?");
            params.add(entity.getRegistrationDate());
        }
        if (entity.getLogin() != null) {
            updateClauses.add("login = ?");
            params.add(entity.getLogin());
        }
        if (entity.getRole() != null) {
            updateClauses.add("role_id = ?");
            params.add(entity.getRole().getId());
        }
        sqlBuilder.append(String.join(", ", updateClauses))
                .append(" WHERE id = ?;");
        params.add(entity.getId());

        String updateFilterSql = sqlBuilder.toString();

        try(Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(updateFilterSql)){
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }
            preparedStatement.executeUpdate();
            return new User(entity.getId());
        } catch (SQLException e) {
            LOGGER.severe("Error while updating user: " + e.getMessage());
            throw new DaoException("Error while updating user", e);
        }
    }

    public User updateWithDto(CreateUserDto createUserDto) {
        if (createUserDto.getId() == null) {
            throw new DaoException("User id is null");
        }
        StringBuilder sqlBuilder = new StringBuilder("UPDATE users SET ");
        List<String> updateClauses = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (createUserDto.getName() != null) {
            updateClauses.add("name = ?");
            params.add(createUserDto.getName());
        }
        if (createUserDto.getRegistrationDate() != null) {
            updateClauses.add("registration_date = ?");
            params.add(createUserDto.getRegistrationDate());
        }
        if (createUserDto.getLogin() != null) {
            updateClauses.add("login = ?");
            params.add(createUserDto.getLogin());
        }
        sqlBuilder.append(String.join(", ", updateClauses))
                .append(" WHERE id = ?;");
        params.add(createUserDto.getId());

        String updateFilterSql = sqlBuilder.toString();

        try (Connection connection = ConnectionManager.getConnection();
        var preparedStatement = connection.prepareStatement(updateFilterSql)) {
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }
            preparedStatement.executeUpdate();
            return new User(createUserDto.getId());
        } catch (SQLException e) {
            LOGGER.severe("Error while updating user: " + e.getMessage());
            throw new DaoException("Error while updating user", e);
        }
    }

    @Override
    public void save(User entity) {
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setDate(2, Date.valueOf(entity.getRegistrationDate()));
            preparedStatement.setString(3, entity.getLogin());
            var affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                LOGGER.severe("Failed to save user");
                throw new DaoException("Cannot save user");
            }
            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                } else {
                    LOGGER.severe("Failed to save user, no ID generated");
                    throw new DaoException("Cannot save user, no ID generated");
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Failed to save user");
            throw new DaoException("Failed to save user", e);
        }
    }


    @Override
    public void delete(Long id) {
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);
            int executeUpdate = preparedStatement.executeUpdate();
            if (executeUpdate == 0) {
                LOGGER.severe("Failed to delete user");
                throw new DaoException("Failed to delete user");
            }
        } catch (SQLException e) {
            LOGGER.severe("Failed to delete user: " + e.getMessage());
            throw new DaoException("Failed to delete user", e);
        }

    }

    @Override
    public Optional<User> findById(Long id) {
        try(Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(buildUser(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error finding user by id: " + e.getMessage());
            throw new DaoException("Error finding user by id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try(Connection connection = ConnectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL)){
            while(resultSet.next()){
                users.add(buildUser(resultSet));
            }

        } catch (SQLException e) {
            LOGGER.severe("Error while finding all users: " + e.getMessage());
            throw new DaoException("Error while finding all users: " + e.getMessage());
        }
        return users;
    }

    public Optional<User> findByEmailAndPassword(String login) {
        try (Connection connection = ConnectionManager.getConnection();
        var preparedStatement = connection.prepareStatement(GET_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = buildUser(resultSet);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            LOGGER.severe("Error finding user by email: " + e.getMessage());
            throw new DaoException("Error while finding user by login: " + login, e);
        }
    }

    private User buildUser(ResultSet resultSet) {
        try {
            return User.builder()
                    .id(resultSet.getLong("id"))
                    .name(resultSet.getString("name"))
                    .registrationDate(resultSet.getDate("registration_date").toLocalDate())
                    .login(resultSet.getString("login"))
                    .build();


        } catch (SQLException e) {
            LOGGER.severe("Error while building user: " + e.getMessage());
            throw new DaoException("Error while building user: " + e.getMessage());
        }
    }
    private void setUser(User entity, PreparedStatement preparedStatement) {
        try {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setDate(2, Date.valueOf(entity.getRegistrationDate()));
            preparedStatement.setString(3, entity.getLogin());
            preparedStatement.setObject(4, entity.getRole());
        } catch (SQLException e) {
            LOGGER.severe("Error inserting user: " + e.getMessage());
            throw new DaoException("Error inserting user: " + e.getMessage());
        }
    }
}
