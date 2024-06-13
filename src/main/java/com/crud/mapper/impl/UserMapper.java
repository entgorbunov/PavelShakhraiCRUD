package com.crud.mapper.impl;

import com.crud.dto.CreateUserDto;
import com.crud.entity.Role;
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
                .name(createUserDto.getName())
                .registrationDate(createUserDto.getRegistrationDate())
                .login(createUserDto.getLogin())
                .roleId(Role.getIdByRoleName(createUserDto.getRole())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid role name: " + createUserDto.getRole())))
                .build();
    }

    public CreateUserDto toCreateUserDto(User user) {
        return CreateUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .registrationDate(user.getRegistrationDate())
                .login(user.getLogin())
                .role("USER")
                .build();
    }
}
