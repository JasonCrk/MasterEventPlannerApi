package com.LP2.EventScheduler.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException() {
        super("The event does not exist");
    }
}
