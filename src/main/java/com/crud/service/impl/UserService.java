package com.crud.service.impl;

import com.crud.dao.impl.UserDao;
import com.crud.dto.CreateUserDto;
import com.crud.entity.User;
import com.crud.exceptions.ServiceException;
import com.crud.mapper.impl.UserMapper;
import com.crud.service.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class UserService  implements Service<CreateUserDto, Long> {
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(UserDao.class.getName());
    private static final UserDao USER_DAO = UserDao.getInstance();
    private static final UserMapper USER_MAPPER = UserMapper.getInstance();
    private static final AtomicReference<UserService> INSTANCE = new AtomicReference<>();
    public static UserService getInstance() {
        INSTANCE.get();
        if (INSTANCE.get() == null) {
            UserService userService = new UserService();
            if (INSTANCE.compareAndSet(null, userService)) {
                return userService;
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
        } catch (ServiceException e) {
            LOGGER.severe("Error deleting user: " + e.getMessage());
            throw new ServiceException("Delete user failed", e);
        }
    }

    @Override
    public Long create(CreateUserDto createUserDto) {
        try {
            User user = USER_MAPPER.toEntity(createUserDto);
            USER_DAO.save(user);
            return user.getId();
        } catch (Exception e) {
            LOGGER.severe("Error creating user: " + e.getMessage());
            throw new ServiceException("Error creating user", e);
        }
    }

    @Override
    public CreateUserDto update(CreateUserDto createUserDto) {
        try {
            User user = USER_MAPPER.toEntity(createUserDto);
            User update = USER_DAO.update(user);
            return USER_MAPPER.toCreateUserDto(update);


        }catch (ServiceException e) {
            LOGGER.severe("Error updating user: " + e.getMessage());
            throw new ServiceException("Error updating user", e);
        }
    }

    @Override
    public List<CreateUserDto> findAll() {
        List<CreateUserDto> list = new ArrayList<>();
        try {
            List<User> userDaoAll = USER_DAO.findAll();
            for (User user : userDaoAll) {
                CreateUserDto createUserDto = USER_MAPPER.toCreateUserDto(user);
                list.add(createUserDto);
            }
            return list;
        } catch (ServiceException e) {
            LOGGER.severe("Error finding users: " + e.getMessage());
            throw new ServiceException("Error finding users", e);
        }
    }

    @Override
    public CreateUserDto findById(Long id) {
        try {
            Optional<User> byId = USER_DAO.findById(id);
            return USER_MAPPER.toCreateUserDto(byId.orElseThrow(
                    () -> new ServiceException("User with id " + id + " not found")));

        } catch (ServiceException e) {
            LOGGER.severe("Error finding user: " + e.getMessage());
            throw new ServiceException("Error finding user", e);
        }
    }
}
