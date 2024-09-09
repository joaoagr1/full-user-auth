package com.authentication.module.exceptions.custom;

    public class UserNotVerifiedException extends RuntimeException {
        public UserNotVerifiedException(String message) {
            super(message);
        }
    }
