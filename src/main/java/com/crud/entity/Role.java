package com.crud.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN(1, "Administrator"),
    USER(2, "User"),
    GUEST(3, "Guest");

    private final int id;
    private final String roleName;

}

