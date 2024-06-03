package com.crud.dto;

import com.crud.entity.Role;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class CreateUserDto {
    Long id;
    String username;
    LocalDate registrationDate;
    String login;
    Role role;

}
