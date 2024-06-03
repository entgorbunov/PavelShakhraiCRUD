package com.crud.exceptions;

import java.sql.SQLException;

public class DaoException extends RuntimeException {
    public DaoException(SQLException e) {
        super(e);
    }
    public DaoException(String message) {
        super(message);
    }
    public DaoException(String message, SQLException e) {
        super(message, e);
    }
}
