package com.example.auth.exceptions;

    public class UserNotVerifiedException extends RuntimeException {
        public UserNotVerifiedException(String message) {
            super(message);
        }
    }
