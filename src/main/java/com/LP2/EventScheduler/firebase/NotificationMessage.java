package com.LP2.EventScheduler.firebase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    private String recipientToken;
    private String title;
    private String image;
    private String message;
    private Map<String, String> data;
}
