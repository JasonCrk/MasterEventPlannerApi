package com.LP2.EventScheduler.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntityWithMessageResponse<T> {
    private String message;
    private T created;
}
