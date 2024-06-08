package com.crud.mapper.impl;

import com.crud.dto.UserDtoDB;
import com.crud.entity.User;

import java.util.concurrent.atomic.AtomicReference;

public class UserMapperDB {
    private static final AtomicReference<UserMapperDB> USER_MAPPER_DB = new AtomicReference<>();
    public static UserMapperDB getInstance() {
        if (USER_MAPPER_DB.get() == null) {
            UserMapperDB userMapperDB = new UserMapperDB();
            if (USER_MAPPER_DB.compareAndSet(null, userMapperDB)) {
                return userMapperDB;
            } else {
                return USER_MAPPER_DB.get();
            }
        }
        return USER_MAPPER_DB.get();
    }

    public User toEntity(UserDtoDB userDtoDB) {
        return User.builder()
                .id(userDtoDB.getId())
                .name(userDtoDB.getName())
                .registrationDate(userDtoDB.getRegistrationDate())
                .login(userDtoDB.getLogin())
                .role(userDtoDB.getRole())
                .build();
    }

    public UserDtoDB toDto(User user) {
        return UserDtoDB.builder()
                .id(user.getId())
                .name(user.getName())
                .registrationDate(user.getRegistrationDate())
                .login(user.getLogin())
                .role(user.getRole())
                .build();
    }
}
