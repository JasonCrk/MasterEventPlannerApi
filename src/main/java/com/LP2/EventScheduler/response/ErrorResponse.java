package com.LP2.EventScheduler.response;

import lombok.Data;

@Data
public class ErrorResponse {
    private int statusCode;
    private String message;
}
