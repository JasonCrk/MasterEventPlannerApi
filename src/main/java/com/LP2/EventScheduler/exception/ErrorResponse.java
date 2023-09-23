package com.LP2.EventScheduler.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private int statusCode;
    private String message;
}
