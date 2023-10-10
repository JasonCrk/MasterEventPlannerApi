package com.LP2.EventScheduler.email;

import java.util.Map;

public interface EmailContent {
    String getBody(Map<String, String> data);
    String getSubject();
}
