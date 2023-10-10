package com.LP2.EventScheduler.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("The user does not exist");
    }
}
