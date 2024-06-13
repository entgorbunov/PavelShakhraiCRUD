package com.crud.service.impl;

import com.crud.dao.impl.UserDao;
import com.crud.dto.UserDtoDB;
import com.crud.entity.User;
import com.crud.exceptions.ServiceException;
import com.crud.exceptions.ServletCrudException;
import com.crud.mapper.impl.UserMapperDB;
import com.crud.service.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class UserServiceDB implements Service<UserDtoDB, Long> {

    private static final Logger LOGGER = Logger.getLogger(UserServiceDB.class.getName());
    private static final UserDao USER_DAO = UserDao.getInstance();
    private static final UserMapperDB USER_MAPPER_DB = UserMapperDB.getInstance();

    private static final AtomicReference<UserServiceDB> INSTANCE = new AtomicReference<>();

    public static UserServiceDB getInstance() {
        INSTANCE.get();
        if (INSTANCE.get() == null) {
            UserServiceDB userServiceDB = new UserServiceDB();
            if (INSTANCE.compareAndSet(null, userServiceDB)) {
                return userServiceDB;
            } else {
                return INSTANCE.get();
            }
        }
        return INSTANCE.get();
    }

    @Override
    public void delete(Long aLong) {
        try {
            USER_DAO.delete(aLong);
        } catch (Exception e) {
            LOGGER.severe("Error deleting user: " + e.getMessage());
            throw new ServiceException("Delete user failed");
        }
    }

    @Override
    public UserDtoDB update(UserDtoDB userDtoDB) {
        try {
            Optional<User> optionalUser = USER_DAO.findById(userDtoDB.getId());
            User user = optionalUser.orElseThrow(() -> new ServiceException("User Not Found"));
            User updated = USER_DAO.update(user);
            return USER_MAPPER_DB.toDto(updated);
        } catch (Exception e) {
            LOGGER.severe("Error updating user: " + e.getMessage());
            throw new ServiceException("Update user failed");
        }
    }

    @Override
    public List<UserDtoDB> findAll() {
        try {
            return USER_DAO.findAll().stream()
                    .map(USER_MAPPER_DB::toDto)
                    .toList();
        } catch (Exception e) {
            LOGGER.severe("Error finding users: " + e.getMessage());
            throw new ServiceException("Find users failed");
        }

    }

    public Optional<UserDtoDB> login(String login) {
        try {
            return USER_DAO.findByLogin(login)
                    .map(USER_MAPPER_DB::toDto);
        } catch (Exception e) {
            LOGGER.severe("Error finding user: " + e.getMessage());
            throw new ServiceException("Find user failed");
        }
    }

    @Override
    public UserDtoDB findById(Long id) {
        return USER_MAPPER_DB.toDto(USER_DAO.findById(id).orElseThrow(() -> new ServiceException("User Not Found")));
    }

    @Override
    public Long create(UserDtoDB userDtoDB) {
        return 0L;
    }


}
