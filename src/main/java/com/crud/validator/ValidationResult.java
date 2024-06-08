package com.crud.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private final List<Error> errors = new ArrayList<>();

    public void addError(Error error) {
        this.errors.add(error);
    }

    public boolean isValid() {
        return errors.isEmpty();
    }
}
