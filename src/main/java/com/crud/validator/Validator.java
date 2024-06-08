package com.crud.validator;

public interface Validator<T> {
    ValidationResult isValid(T t);
}
