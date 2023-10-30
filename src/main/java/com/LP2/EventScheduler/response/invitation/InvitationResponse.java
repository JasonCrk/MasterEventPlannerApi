package com.LP2.EventScheduler.response.invitation;

import com.LP2.EventScheduler.response.user.SimpleUserResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationResponse {
    private UUID id;
    private SimpleUserResponse user;
    private String notifiedAt;
}
