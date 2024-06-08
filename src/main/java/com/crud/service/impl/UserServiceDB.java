package com.crud.service.impl;

import com.crud.dao.impl.UserDao;
import com.crud.dto.UserDtoDB;
import com.crud.entity.User;
import com.crud.exceptions.ServletCrudException;
import com.crud.mapper.impl.UserMapper;
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
        USER_DAO.delete(aLong);
    }

    @Override
    public Long create(UserDtoDB userDtoDB) {
        return 0L;
    }

    @Override
    public UserDtoDB update(UserDtoDB userDtoDB) {
        Optional<User> optionalUser = USER_DAO.findById(userDtoDB.getId());
        User user = optionalUser.orElseThrow(() -> new ServletCrudException("User Not Found"));
        User updated = USER_DAO.update(user);
        return USER_MAPPER_DB.toDto(updated);
    }

    @Override
    public List<UserDtoDB> findAll() {
        return USER_DAO.findAll().stream()
                .map(USER_MAPPER_DB::toDto)
                .toList();
    }

    @Override
    public UserDtoDB findById(Long id) {
        Optional<User> optionalUser = USER_DAO.findById(id);
        User user = optionalUser.orElseThrow(() -> new ServletCrudException("User Not Found"));
        return USER_MAPPER_DB.toDto(user);
    }

    public Optional<UserDtoDB> login(String email, String password) {
        return USER_DAO.findByEmailAndPassword(email, password)
                .map(USER_MAPPER_DB::toDto);
    }
}
