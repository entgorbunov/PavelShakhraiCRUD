package com.crud.exceptions;

public class DriverException extends RuntimeException {
    public DriverException(String message) {
        super(message);
    }
    public DriverException(String message, Throwable cause) {
        super(message, cause);
    }
    public DriverException(Throwable cause) {
        super(cause);
    }
}
