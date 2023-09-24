package com.LP2.EventScheduler.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {
        super("The category does not exist");
    }
}
