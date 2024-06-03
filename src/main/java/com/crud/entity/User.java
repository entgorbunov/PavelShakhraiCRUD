package com.crud.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private LocalDate registrationDate;
    private String login;
    @Builder.Default
    private Role role = Role.USER;
    public User(Long id) {
        this.id = id;
    }
}

