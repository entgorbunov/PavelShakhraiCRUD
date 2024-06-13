package com.crud.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN(1, "Administrator"),
    USER(2, "User"),
    GUEST(3, "Guest");

    private final int id;
    private final String roleName;

    public static Optional<Role> findById(int id) {
        return Arrays.stream(values())
                .filter(role -> role.getId() == id)
                .findFirst();
    }

    public static Optional<Role> findByRoleName(String roleName) {
        return Arrays.stream(values())
                .filter(role -> role.getRoleName().equalsIgnoreCase(roleName))
                .findFirst();
    }

    public static Optional<Integer> getIdByRoleName(String roleName) {
        return findByRoleName(roleName).map(Role::getId);
    }

}

