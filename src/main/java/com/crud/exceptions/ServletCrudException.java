package com.crud.exceptions;

public class ServletCrudException extends RuntimeException {
    public ServletCrudException(Exception e) {
        super(e);
    }
    public ServletCrudException(String message) {
        super(message);
    }
    public ServletCrudException(String message, Exception e) {
        super(message, e);
    }
}
