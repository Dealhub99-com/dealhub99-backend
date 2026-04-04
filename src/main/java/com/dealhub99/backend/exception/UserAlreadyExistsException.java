package com.dealhub99.backend.exception;

public class UserAlreadyExistsException extends ApiException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
