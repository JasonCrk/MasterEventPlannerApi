package com.LP2.EventScheduler.dto.event;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DeleteEventDTO {
    @NotNull(message = "The event is required")
    private UUID eventId;
}
