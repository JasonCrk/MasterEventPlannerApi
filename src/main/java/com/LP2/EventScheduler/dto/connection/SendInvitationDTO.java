package com.LP2.EventScheduler.dto.connection;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendInvitationDTO {

    @NotNull(message = "The user ID is required")
    private UUID user;
}
