package com.LP2.EventScheduler.exception;

public class FailedNotificationSendingException extends RuntimeException {
    public FailedNotificationSendingException() {
        super("Failed notification sending");
    }
}
