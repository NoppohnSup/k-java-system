package com.example.kjavasystem.transaction.exception;

public class RoleCannotAccessException extends RuntimeException{
    public RoleCannotAccessException(String message) {
        super(message);
    }
}
