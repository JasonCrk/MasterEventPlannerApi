package com.LP2.EventScheduler.dto.event;

import com.LP2.EventScheduler.model.enums.Visibility;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateEventDTO {

    @NotBlank(message = "The name is required")
    @Size(max = 100)
    private String name;

    private String description;

    @NotNull(message = "The category is required")
    private UUID category;

    @NotNull(message = "The visibility is required")
    private Visibility visibility;

    @NotBlank(message = "The local is required")
    @Size(max = 150)
    private String local;

    @NotNull(message = "The realization date is required")
    @Future(message = "Must be a time greater than the current one")
    private LocalDateTime realizationDate;

    @NotNull(message = "The finish date is required")
    @Future(message = "Must be a time greater than the current one")
    private LocalDateTime finishDate;
}
