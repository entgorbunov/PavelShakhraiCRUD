package com.crud.validator;

import com.crud.dto.CreateUserDto;
import com.crud.entity.Role;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class CreateUserValidator implements Validator<CreateUserDto> {

    private static final AtomicReference<CreateUserValidator> INSTANCE = new AtomicReference<>();

    public static CreateUserValidator getInstance() {
        INSTANCE.get();
        if (INSTANCE.get() == null) {
            CreateUserValidator validator = new CreateUserValidator();
            if (INSTANCE.compareAndSet(null, validator)) {
                return validator;
            } else {
                return INSTANCE.get();
            }
        }
        return INSTANCE.get();
    }


    @Override
    public ValidationResult isValid(CreateUserDto createUserDto) {
        ValidationResult validationResult = new ValidationResult();
        Optional<Role> role = Role.findByRoleName(String.valueOf(createUserDto.getRole()));
        if (role.isEmpty()) {
            validationResult.addError(Error.of("invalid.role", "Role is invalid"));
        }

        return validationResult;
    }
}
