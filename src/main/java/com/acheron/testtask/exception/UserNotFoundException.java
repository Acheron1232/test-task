package com.acheron.testtask.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String email) {
        super("User not found with Email: " + email);
    }

    public UserNotFoundException(Long id) {
        super("User not found with Id: " + id);
    }
}
