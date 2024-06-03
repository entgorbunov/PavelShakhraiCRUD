package com.crud.mapper.impl;

import com.crud.dto.CreateUserDto;
import com.crud.entity.User;

import java.util.concurrent.atomic.AtomicReference;

public class UserMapper {
    private static final AtomicReference<UserMapper> INSTANCE = new AtomicReference<>();

    public static UserMapper getInstance() {
        INSTANCE.get();
        if (INSTANCE.get() == null) {
            UserMapper userMapper = new UserMapper();
            if (INSTANCE.compareAndSet(null, userMapper)) {
                return userMapper;
            } else {
                return INSTANCE.get();
            }
        }
        return INSTANCE.get();
    }

    public User toEntity(CreateUserDto createUserDto) {
        return User.builder()
                .id(createUserDto.getId())
                .name(createUserDto.getUsername())
                .registrationDate(createUserDto.getRegistrationDate())
                .login(createUserDto.getLogin())
                .role(createUserDto.getRole())
                .build();
    }

    public CreateUserDto toCreateUserDto(User user) {
        return CreateUserDto.builder()
                .id(user.getId())
                .username(user.getName())
                .registrationDate(user.getRegistrationDate())
                .login(user.getLogin())
                .role(user.getRole())
                .build();
    }
}
