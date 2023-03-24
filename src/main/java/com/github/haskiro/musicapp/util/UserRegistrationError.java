package com.github.haskiro.musicapp.util;

public class UserRegistrationError extends RuntimeException {
    public UserRegistrationError(String message) {
        super(message);
    }
}
