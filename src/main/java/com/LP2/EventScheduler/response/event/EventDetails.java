package com.LP2.EventScheduler.response.event;

import com.LP2.EventScheduler.response.category.CategoryResponse;
import com.LP2.EventScheduler.response.user.SimpleUserResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDetails {
    private UUID id;
    private SimpleUserResponse coordinator;
    private String name;
    private String description;
    private String visibility;
    private String status;
    private String realizationDate;
    private String finishDate;
    private CategoryResponse category;
    private String createdAt;
    private boolean participating;
    private String local;
}
