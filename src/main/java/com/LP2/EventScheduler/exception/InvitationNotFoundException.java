package com.LP2.EventScheduler.exception;

public class InvitationNotFoundException extends RuntimeException {
    public InvitationNotFoundException() {
        super("The invitation does not exist");
    }
}
