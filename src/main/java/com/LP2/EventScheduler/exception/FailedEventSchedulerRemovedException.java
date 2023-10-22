package com.LP2.EventScheduler.exception;

public class FailedEventSchedulerRemovedException extends RuntimeException {
    public FailedEventSchedulerRemovedException() {
        super("Failed when trying to delete the event scheduler");
    }
}
