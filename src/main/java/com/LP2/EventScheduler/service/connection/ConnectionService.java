package com.LP2.EventScheduler.service.connection;

import com.LP2.EventScheduler.dto.connection.SendInvitationDTO;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.MessageResponse;

import java.util.UUID;

public interface ConnectionService {
    MessageResponse sendInvitation(SendInvitationDTO invitationData, User authUser);
    MessageResponse acceptInvitation(UUID invitationId, User user);
    MessageResponse removeConnection(UUID connectionId, User authUser);
}
