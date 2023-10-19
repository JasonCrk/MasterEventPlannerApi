package com.LP2.EventScheduler.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException() {
        super("The account does not exist");
    }
}
