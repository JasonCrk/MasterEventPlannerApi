package com.LP2.EventScheduler.dto.event;

import com.LP2.EventScheduler.model.enums.Visibility;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventDTO {

    @Size(max = 100)
    private String name;

    private String description;

    private UUID category;

    @Size(max = 150)
    private String local;

    private Visibility visibility;

    @Future(message = "Must be a time greater than the current one")
    private LocalDateTime realizationDate;
}
