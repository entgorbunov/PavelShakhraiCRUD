package com.crud.dto;

import com.crud.entity.Role;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
@Value
@Builder
public class UserDtoDB {
    Long id;
    String name;
    LocalDate registrationDate;
    String login;
    Role role;
}
