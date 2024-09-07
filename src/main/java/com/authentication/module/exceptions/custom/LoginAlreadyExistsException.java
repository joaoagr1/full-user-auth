package com.authentication.module.exceptions.custom;

public class LoginAlreadyExistsException extends RuntimeException {

    public LoginAlreadyExistsException(String message) {
        super(message);
    }
}
