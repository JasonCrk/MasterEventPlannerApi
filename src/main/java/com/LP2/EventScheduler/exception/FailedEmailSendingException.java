package com.LP2.EventScheduler.exception;

public class FailedEmailSendingException extends RuntimeException {
    public FailedEmailSendingException() {
        super("Failed email sending");
    }
}
